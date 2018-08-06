package com.viettel.api.repository.qldv.employee;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;
import com.viettel.api.utils.StringUtils;
import com.viettel.api.web.rest.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Repository
@Transactional
public class EmployeeRepositoryImpl extends BaseRepository implements EmployeeRepository {
    private Logger logger = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

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
}
