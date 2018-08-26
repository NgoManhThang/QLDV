package com.viettel.api.repository.qldv.validate;

import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class ValidateRepositoryImpl extends BaseRepository implements ValidateRepository{
    Logger logger = LoggerFactory.getLogger(ValidateRepositoryImpl.class);

    @Override
    public List<UnionsDto> getEmployeeExistInUnions(UnionsDto dto) {
        List<UnionsDto> lst = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_VALIDATE, "get-employee-exist-in-unions");
            Map<String, String> maps = new HashMap<>();
            maps.put("p_id", dto.getEmployeeId());
            lst = getNamedParameterJdbcTemplate().query(sql, maps, BeanPropertyRowMapper.newInstance(UnionsDto.class));
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return lst;
    }

    @Override
    public List<UnionsDto> getPartnerExistInUnions(UnionsDto dto) {
        List<UnionsDto> lst = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_VALIDATE, "get-partner-exist-in-unions");
            Map<String, String> maps = new HashMap<>();
            maps.put("p_id", String.valueOf(dto.getPartnerId()));
            lst = getNamedParameterJdbcTemplate().query(sql, maps, BeanPropertyRowMapper.newInstance(UnionsDto.class));
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return lst;
    }
}
