package com.viettel.api.repository.qldv.employee;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.EmployeeEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.DataUtil;
import com.viettel.api.utils.SQLBuilder;
import com.viettel.api.utils.StringUtils;
import com.viettel.api.web.rest.BaseController;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Repository
@Transactional
public class EmployeeRepositoryImpl extends BaseRepository implements EmployeeRepository {
    private Logger logger = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Datatable searchEmployee(EmployeeDto dto) {
        Datatable datatable = new Datatable();
        try {
            StringBuilder sqlQuery = new StringBuilder(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_EMPLOYEE, "search-employee"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("p_user_name", StringUtils.convertUpperParamContains(dto.getUserName()));
            maps.put("p_full_name", StringUtils.convertUpperParamContains(dto.getFullName()));

            List<EmployeeDto> lst = getListDataBySqlQuery(sqlQuery.toString(), maps,
                    dto.getPage(), dto.getPageSize(),
                    EmployeeDto.class, true,
                    dto.getSortName(), dto.getSortType());

            int count = 0;
            if (lst.size() > 0) {
                count = lst.get(0).getTotalRow();
            }

            datatable.setRecordsTotal(count);
            if (dto.getPageSize() > 0) {
                if (count % dto.getPageSize() == 0) {
                    datatable.setDraw(count / dto.getPageSize());
                } else {
                    datatable.setDraw((count / dto.getPageSize()) + 1);
                }
            }
            datatable.setData(lst);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return datatable;
    }

    @Override
    public ResultDto saveData(EmployeeDto dto) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);
        Session session = getSession();
        entityManager = getEntityManager();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            if (StringUtils.isStringNullOrEmpty(dto.getEmployeeId())) {
                dto.setPassword(StringUtils.passwordEncoder().encode(dto.getPassword().trim()));
                dto.setCreateUser(userName);
                dto.setUpdateUser(userName);
                dto.setCreateDate(new Timestamp(System.currentTimeMillis()));
                dto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                Long id = (Long) session.save(dto.toEntity());
                resultDto.setId(String.valueOf(id));
            } else {
                EmployeeEntity entity = entityManager.find(EmployeeEntity.class, Long.valueOf(dto.getEmployeeId()));
                if(entity.getEmployeeId() != null){
                    dto.setUpdateUser(userName);
                    dto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    dto.setPassword(entity.getPassword());
                    entityManager.merge(dto.toEntity());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDto.setKey(Constants.RESULT.ERROR);
        }
        return resultDto;
    }

    @Override
    public EmployeeDto getDetail(EmployeeDto dto) {
        EmployeeDto employeeDto = new EmployeeDto();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_EMPLOYEE, "get-detail");
            Map<String, String> maps = new HashMap<>();
            maps.put("p_id", dto.getEmployeeId());
            List<EmployeeDto> list = getNamedParameterJdbcTemplate().query(sql, maps, BeanPropertyRowMapper.newInstance(EmployeeDto.class));
            if (list != null && list.size() > 0) {
                employeeDto = list.get(0);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return employeeDto;
    }
}
