package com.viettel.api.repository.boc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.api.domain.boc.BocFilesEntity;
import com.viettel.api.domain.boc.BocRoleEntity;
import com.viettel.api.domain.boc.BocRoleTargetEntity;
import com.viettel.api.domain.boc.BocUnitEntity;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocConstantDto;
import com.viettel.api.dto.boc.BocFilesDto;
import com.viettel.api.dto.boc.BocRoleDto;
import com.viettel.api.dto.boc.BocRoleTargetDto;
import com.viettel.api.dto.boc.BocUnitDto;
import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;

/**
 * Created by VTN-PTPM-NV04 on 2/7/2018.
 */
@SuppressWarnings("rawtypes")
@Repository
@Transactional
public class CommonRepositoryImpl extends BaseRepository implements CommonRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonRepositoryImpl.class);
	
	@PersistenceContext
    private EntityManager entityManager;

    @Override
    public ResultDto getDBSysDate() {
        ResultDto data = new ResultDto();
        try {
            String strQuery = "SELECT sysdate systemDate FROM DUAL";

            List<ResultDto> list = getNamedParameterJdbcTemplate().query(strQuery, BeanPropertyRowMapper.newInstance(ResultDto.class));

            if (list != null && list.size() > 0){
                data = list.get(0);
            }
        } catch (Exception e){
        	LOGGER.error(e.getMessage(), e);
        }
        return data;
    }
    
    @Override
    public BocFilesDto getFileById(Long fileId) {
    	BocFilesDto data = new BocFilesDto();
    	entityManager = getEntityManager();
        try {
        	BocFilesEntity bocFilesEntity = entityManager.find(BocFilesEntity.class, fileId);
        	if(bocFilesEntity != null) {
        		data.setFileId(bocFilesEntity.getFileId());
            	data.setGroupId(bocFilesEntity.getGroupId());
            	data.setGroupFile(bocFilesEntity.getGroupFile());
            	data.setFileName(bocFilesEntity.getFileName());
            	data.setFilePath(bocFilesEntity.getFilePath());
            	data.setFileSize(bocFilesEntity.getFileSize());
            	data.setCreateUser(bocFilesEntity.getCreateUser());
            	data.setCreateDate(bocFilesEntity.getCreateDate());
        	}
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return data;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<BocRoleDto> getListBocRole() {
    	List<BocRoleDto> listResult = new ArrayList<>();
        try {
        	List<BocRoleEntity> list = findAll(BocRoleEntity.class, "roleName", "ASC");
        	for (int i = 0; i < list.size(); i++) {
        		BocRoleDto bocRoleDto = new BocRoleDto();
        		bocRoleDto.setRoleId(list.get(i).getRoleId());
        		if(list.get(i).getParentRoleId() != null) {
        			bocRoleDto.setParent(list.get(i).getParentRoleId().toString());
        		}
        		bocRoleDto.setLabel(list.get(i).getRoleName());
        		bocRoleDto.setCode(list.get(i).getRoleId().toString());
        		bocRoleDto.setSelected(false);
        		bocRoleDto.setParentRoleId(list.get(i).getParentRoleId());
        		listResult.add(bocRoleDto);
			}
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return listResult;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<BocUnitDto> getListBocUnit() {
    	List<BocUnitDto> listResult = new ArrayList<>();
        try {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!bocUserDto.getListUnit().isEmpty()) {
    			unitCode = bocUserDto.getListUnit().get(0);
        	} else {
        		return listResult;
        	}
        	List<BocUnitEntity> list = new ArrayList<>();
        	if(bocUserDto.getRegionLevel() == 0) {
        		list = findAll(BocUnitEntity.class, "unitName", "ASC");
        	} else {
        		String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-unit-by-user");
            	Map<String, Object> parameters = new HashMap<>();
                parameters.put("p_unit_code", unitCode);
                list = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocUnitEntity.class));
        	}
        	for (int i = 0; i < list.size(); i++) {
        		BocUnitDto bocUnitDto = new BocUnitDto();
        		bocUnitDto.setUnitId(list.get(i).getUnitId());
        		if(list.get(i).getParentUnitId() != null) {
        			bocUnitDto.setParent(list.get(i).getParentUnitId().toString());
        		}
        		bocUnitDto.setLabel(list.get(i).getDescription());
        		bocUnitDto.setCode(list.get(i).getUnitId().toString());
        		bocUnitDto.setSelected(false);
        		bocUnitDto.setParentUnitId(list.get(i).getParentUnitId());
        		listResult.add(bocUnitDto);
			}
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return listResult;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<BocRoleTargetDto> getListBocRoleTarget() {
    	List<BocRoleTargetDto> listResult = new ArrayList<>();
        try {
        	List<BocRoleTargetEntity> list = findAll(BocRoleTargetEntity.class, "roleTargetName", "ASC");
        	for (int i = 0; i < list.size(); i++) {
        		BocRoleTargetDto bocRoleTargetDto = new BocRoleTargetDto();
        		bocRoleTargetDto.setRoleTargetId(list.get(i).getRoleTargetId());
        		if(list.get(i).getParentRoleTargetId() != null) {
        			bocRoleTargetDto.setParent(list.get(i).getParentRoleTargetId().toString());
        		}
        		bocRoleTargetDto.setLabel(list.get(i).getRoleTargetName());
        		bocRoleTargetDto.setCode(list.get(i).getRoleTargetId().toString());
        		bocRoleTargetDto.setSelected(false);
        		bocRoleTargetDto.setParentRoleTargetId(list.get(i).getParentRoleTargetId());
        		listResult.add(bocRoleTargetDto);
			}
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return listResult;
    }
    
	@Override
    public List<BocConstantDto> getListBocConstant(String constantType) {
    	List<BocConstantDto> listResult = new ArrayList<>();
        try {
        	String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-constant-by-type");
        	Map<String, Object> parameters = new HashMap<>();
            parameters.put("constantType", constantType);
            listResult = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocConstantDto.class));
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return listResult;
    }
    
    @SuppressWarnings("deprecation")
	@Override
    @Scheduled(fixedDelay = 60000)
    public void keepAlive() {
        String sql = "SELECT 1 FROM DUAL";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.executeUpdate();
    }
}
