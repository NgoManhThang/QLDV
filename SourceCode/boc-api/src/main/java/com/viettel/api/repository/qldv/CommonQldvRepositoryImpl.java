package com.viettel.api.repository.qldv;

import com.viettel.api.domain.qldv.FilesEntity;
import com.viettel.api.dto.qldv.CodeDecodeDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.dto.qldv.PlaceDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Repository
@Transactional
public class CommonQldvRepositoryImpl extends BaseRepository implements CommonQldvRepository {

    Logger logger = LoggerFactory.getLogger(CommonQldvRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CodeDecodeDto> search(CodeDecodeDto dto) {
        List<CodeDecodeDto> list = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_COMMON, "search_by_code_group");
            Map<String, String> maps = new HashMap<>();
            maps.put("p_group_list", dto.getCodeGroup());
            list = getNamedParameterJdbcTemplate().query(sql, maps, BeanPropertyRowMapper.newInstance(CodeDecodeDto.class));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<PlaceDto> getPlaceById(PlaceDto dto) {
        List<PlaceDto> lst = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_COMMON, "get-place-by-id");
            Map<String, String> maps = new HashMap<>();
            lst = getNamedParameterJdbcTemplate().query(sql, maps, BeanPropertyRowMapper.newInstance(PlaceDto.class));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return lst;
    }

    @Override
    public List<EmployeeDto> getEmployeeByIdOrUserName(EmployeeDto dto) {
        List<EmployeeDto> lst = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_COMMON, "get-employee-by-id-user-name");
            Map<String, Long> maps = new HashMap<>();
            maps.put("p_id", dto.getEmployeeId());
            lst = getNamedParameterJdbcTemplate().query(sql, maps, BeanPropertyRowMapper.newInstance(EmployeeDto.class));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return lst;
    }

    @Override
    public FilesEntity getFileById(Long id) {
        entityManager = getEntityManager();
        FilesEntity filesEntity = entityManager.find(FilesEntity.class, id);
        return filesEntity;
    }

    @Override
    public EmployeeDto getEmployeeByUserName(String userName) {
        String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_COMMON, "get-employee-by-user-name");
        Map<String, String> maps = new HashMap<>();
        maps.put("p_user_name", userName);
        return getNamedParameterJdbcTemplate().queryForObject(sql, maps, BeanPropertyRowMapper.newInstance(EmployeeDto.class));
    }
}
