package com.viettel.api.repository.boc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.boc.BocFilesEntity;
import com.viettel.api.domain.boc.BocRoleUserEntity;
import com.viettel.api.domain.boc.BocUnitUserEntity;
import com.viettel.api.domain.boc.BocUserEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocFilesDto;
import com.viettel.api.dto.boc.BocRoleDto;
import com.viettel.api.dto.boc.BocRoleTargetDto;
import com.viettel.api.dto.boc.BocRoleUserDto;
import com.viettel.api.dto.boc.BocUnitDto;
import com.viettel.api.dto.boc.BocUnitUserDto;
import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.DataUtil;
import com.viettel.api.utils.FilesUtils;
import com.viettel.api.utils.SQLBuilder;
import com.viettel.api.utils.StringUtils;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Repository
@Transactional
@SuppressWarnings("rawtypes")
public class BocUserRepositoryImpl extends BaseRepository implements BocUserRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(BocUserRepositoryImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
	@Override
    public BocUserDto getUserByUserName(String userName) {
    	BocUserDto bocUserDto = new BocUserDto();
    	try{
    		String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-list-user-by-userName");
        	
        	Map<String, Object> parameters = new HashMap<>();
            parameters.put("userName", userName);
            List<BocUserDto> list = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocUserDto.class));
            
	        if (list != null && list.size() > 0){
	        	bocUserDto = list.get(0);
	        }
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }

        return bocUserDto;
    }
    
	@Override
    public List<BocRoleDto> getListRoleByUserName(String userName) {
    	List<BocRoleDto> listResult = new ArrayList<>();
    	try{
    		String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-list-role-by-userName");
        	
        	Map<String, Object> parameters = new HashMap<>();
            parameters.put("userName", userName);
            listResult = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocRoleDto.class));
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }
        return listResult;
    }
	
	@Override
    public List<BocUnitDto> getListUnitByUserName(String userName) {
    	List<BocUnitDto> listResult = new ArrayList<>();
    	try{
    		String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-list-unit-by-userName");
        	
        	Map<String, Object> parameters = new HashMap<>();
            parameters.put("userName", userName);
            listResult = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocUnitDto.class));
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }
        return listResult;
    }
	
	@Override
    public List<BocRoleTargetDto> getListRoleTargetByUserName(String userName) {
    	List<BocRoleTargetDto> listResult = new ArrayList<>();
    	try{
    		String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-list-role-target-by-userName");
        	
        	Map<String, Object> parameters = new HashMap<>();
            parameters.put("userName", userName);
            listResult = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocRoleTargetDto.class));
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }
        return listResult;
    }
	
	public List<BocRoleUserDto> getListRoleUserByUserId(Long userId) {
    	List<BocRoleUserDto> listResult = new ArrayList<>();
    	try{
    		String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-list-role-user-by-userId");
        	Map<String, Object> parameters = new HashMap<>();
            parameters.put("userId", userId);
            listResult = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocRoleUserDto.class));
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }
        return listResult;
    }
	
	public void deleteAndInsertBocRoleByUser(BocUserDto bocUserDto) {
		entityManager = getEntityManager();
		Session session = getSession();
    	try{
            List<BocRoleUserDto> listBocRoleUserDto = getListRoleUserByUserId(bocUserDto.getUserId());
    		for (int i = 0; i < listBocRoleUserDto.size(); i++) {
    			BocRoleUserEntity e = entityManager.find(BocRoleUserEntity.class, listBocRoleUserDto.get(i).getRoleUserId());
                if (e != null){
                	entityManager.remove(e);
                }
			}
    		for (int j = 0; j < bocUserDto.getListRole().size(); j++) {
    			BocRoleUserEntity bocRoleUserEntity = new BocRoleUserEntity();
    			bocRoleUserEntity.setUserId(bocUserDto.getUserId());
    			bocRoleUserEntity.setRoleId(Long.valueOf(bocUserDto.getListRole().get(j)));
    			session.save(bocRoleUserEntity);
			}
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }
    }
	
	public List<BocUnitUserDto> getListUnitUserByUserId(Long userId) {
    	List<BocUnitUserDto> listResult = new ArrayList<>();
    	try{
    		String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-list-unit-user-by-userId");
        	Map<String, Object> parameters = new HashMap<>();
            parameters.put("userId", userId);
            listResult = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocUnitUserDto.class));
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }
        return listResult;
    }
	
	public void deleteAndInsertBocUnitByUser(BocUserDto bocUserDto) {
		entityManager = getEntityManager();
		Session session = getSession();
    	try{
            List<BocUnitUserDto> listBocUnitUserDto = getListUnitUserByUserId(bocUserDto.getUserId());
    		for (int i = 0; i < listBocUnitUserDto.size(); i++) {
    			BocUnitUserEntity e = entityManager.find(BocUnitUserEntity.class, listBocUnitUserDto.get(i).getUnitUserId());
                if (e != null){
                	entityManager.remove(e);
                }
			}
    		for (int j = 0; j < bocUserDto.getListUnit().size(); j++) {
    			BocUnitUserEntity bocUnitUserEntity = new BocUnitUserEntity();
    			bocUnitUserEntity.setUserId(bocUserDto.getUserId());
    			bocUnitUserEntity.setUnitId(Long.valueOf(bocUserDto.getListUnit().get(j)));
    			session.save(bocUnitUserEntity);
			}
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public Datatable search(BocUserDto bocUserDto) {
        Datatable dataReturn = new Datatable();
        try{
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto dto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!dto.getListUnit().isEmpty()) {
    			unitCode = dto.getListUnit().get(0);
        	} else {
        		return dataReturn;
        	}
        	String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-data-table-user");
        	
            if (StringUtils.isNotNullOrEmpty(bocUserDto.getFullName())){
            	sqlQuery += " AND (LOWER(BU.FULL_NAME) LIKE :fullName)";
            }
            if (StringUtils.isNotNullOrEmpty(bocUserDto.getUserName())){
            	sqlQuery += " AND (LOWER(BU.USER_NAME) LIKE :userName)";
            }
            if (StringUtils.isNotNullOrEmpty(bocUserDto.getPhoneNumber())){
            	sqlQuery += " AND (LOWER(BU.PHONE_NUMBER) LIKE :phoneNumber)";
            }
            if (StringUtils.isNotNullOrEmpty(bocUserDto.getEmail())){
            	sqlQuery += " AND (LOWER(BU.EMAIL) LIKE :email)";
            }
            
        	Map<String, Object> parameters = new HashMap<>();
        	
            if (StringUtils.isNotNullOrEmpty(bocUserDto.getFullName())){
            	parameters.put("fullName", StringUtils.convertLowerParamContains(bocUserDto.getFullName()));
            }
            if (StringUtils.isNotNullOrEmpty(bocUserDto.getUserName())){
            	parameters.put("userName", StringUtils.convertLowerParamContains(bocUserDto.getUserName()));
            }
            if (StringUtils.isNotNullOrEmpty(bocUserDto.getPhoneNumber())){
            	parameters.put("phoneNumber", StringUtils.convertLowerParamContains(bocUserDto.getPhoneNumber()));
            }
            if (StringUtils.isNotNullOrEmpty(bocUserDto.getEmail())){
            	parameters.put("email", StringUtils.convertLowerParamContains(bocUserDto.getEmail()));
            }
            parameters.put("p_unit_code", unitCode);
	        List<BocUserDto> list = getListDataBySqlQuery(sqlQuery, parameters,
				bocUserDto.getPage(), bocUserDto.getPageSize(),
				BocUserDto.class, true,
				bocUserDto.getSortName(), bocUserDto.getSortType());
	        
            int count = 0;
            if(list.isEmpty()) {
            	dataReturn.setRecordsTotal(count);
            } else {
            	count = list.get(0).getTotalRow();
            	dataReturn.setRecordsTotal(list.get(0).getTotalRow());
            }
            if (bocUserDto.getPageSize() > 0){
                if (count % bocUserDto.getPageSize() == 0){
                    dataReturn.setDraw(count / bocUserDto.getPageSize());
                }
                else {
                    dataReturn.setDraw((count / bocUserDto.getPageSize()) + 1);
                }
            }

            dataReturn.setData(list);
        } catch (Exception ex){
        	LOGGER.error(ex.getMessage(), ex);
        }

        return dataReturn;
    }
    
    @Override
    public ResultDto delete(Long userId) {
        ResultDto resultDTO = new ResultDto();
        entityManager = getEntityManager();
        try {
            BocUserEntity e = entityManager.find(BocUserEntity.class, userId);
            if (e != null){
            	entityManager.remove(e);
            	List<BocRoleUserDto> listBocRoleUserDto = getListRoleUserByUserId(userId);
        		for (int i = 0; i < listBocRoleUserDto.size(); i++) {
        			BocRoleUserEntity bocRoleUserEntity = entityManager.find(BocRoleUserEntity.class, listBocRoleUserDto.get(i).getRoleUserId());
                    if (bocRoleUserEntity != null){
                    	entityManager.remove(bocRoleUserEntity);
                    }
    			}
        		List<BocUnitUserDto> listBocUnitUserDto = getListUnitUserByUserId(userId);
        		for (int i = 0; i < listBocUnitUserDto.size(); i++) {
        			BocUnitUserEntity bocUnitUserEntity = entityManager.find(BocUnitUserEntity.class, listBocUnitUserDto.get(i).getUnitUserId());
                    if (bocUnitUserEntity != null){
                    	entityManager.remove(bocUnitUserEntity);
                    }
    			}
                resultDTO.setKey(Constants.RESULT.SUCCESS);
            } else{
                resultDTO.setKey(Constants.RESULT.NODATA);
            }
        }
        catch (Exception ex) {
            resultDTO.setKey(Constants.RESULT.ERROR);
            resultDTO.setMessage(ex.getMessage());
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultDTO;
    }
    
    @Override
    public BocUserDto getDetail(Long userId) {
        BocUserDto data = new BocUserDto();
        try {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto dto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!dto.getListUnit().isEmpty()) {
    			unitCode = dto.getListUnit().get(0);
        	} else {
        		return data;
        	}
        	String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-data-detail-user-by-userId");
        	
        	Map<String, Object> parameters = new HashMap<>();
            parameters.put("userId", userId);
            parameters.put("p_unit_code", unitCode);
            List<BocUserDto> list = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocUserDto.class));

            if (list != null && list.size() > 0){
                data = list.get(0);
            }
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return data;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public ResultDto save(List<MultipartFile> files, BocUserDto bocUserDto) {
        ResultDto resultDTO = new ResultDto();
        resultDTO.setKey(Constants.RESULT.SUCCESS);

        entityManager = getEntityManager();
        Session session = getSession();
        ResourceBundle resource = ResourceBundle.getBundle("config/globalConfig");
        String uploadFolder = resource.getString("FOLDER_UPLOAD");
        try {
        	bocUserDto.setRoleTarget(StringUtils.convertArrayToString(bocUserDto.getListRoleTarget()));
            resultDTO = validateData(bocUserDto);
            if (Constants.RESULT.SUCCESS.equals(resultDTO.getKey())){
            	if(bocUserDto.getWorkingDateString() != null && !"".equals(bocUserDto.getWorkingDateString())) {
            		bocUserDto.setWorkingDate(DataUtil.date2Timestamp(DataUtil.ddMMyyyyToDate(bocUserDto.getWorkingDateString())));
            	}
                // Them moi
                if (bocUserDto.getUserId() == null){
                	bocUserDto.setPassword(StringUtils.passwordEncoder().encode(bocUserDto.getPassword().trim()));
                	long id = (long) session.save(bocUserDto.toEntity());
                	List<BocFilesDto> listBocFilesDto = FilesUtils.saveMultipleUploadedFile(files, uploadFolder);
                    for (int i = 0; i < listBocFilesDto.size(); i++) {
                    	listBocFilesDto.get(i).setCreateUser(bocUserDto.getCreateUser());
                    	listBocFilesDto.get(i).setGroupFile(1L);
                    	listBocFilesDto.get(i).setGroupId(id);
                    	listBocFilesDto.get(i).setCreateDate(bocUserDto.getCreateDate());
                    	session.save(listBocFilesDto.get(i).toEntity());
					}
                    resultDTO.setId(String.valueOf(id));
                    bocUserDto.setUserId(id);
                }
                // Cap nhat
                else{
                	BocUserEntity bocUserEntity = entityManager.find(BocUserEntity.class, bocUserDto.getUserId());
                    if (bocUserEntity != null){
                    	List<BocFilesEntity> listBocFilesEntity = findByMultilParam(BocFilesEntity.class, 
                    			"groupFile", 1L, 
                    			"groupId", bocUserEntity.getUserId());
                    	if(files.size() > 0) {
                    		for (int i = 0; i < listBocFilesEntity.size(); i++) {
                    			entityManager.remove(listBocFilesEntity.get(i));
                    			FilesUtils.deleteFileByPath(listBocFilesEntity.get(i).getFilePath(), uploadFolder);
    						}
                    		List<BocFilesDto> listBocFilesDto = FilesUtils.saveMultipleUploadedFile(files, uploadFolder);
                    		for (int j = 0; j < listBocFilesDto.size(); j++) {
                    			listBocFilesDto.get(j).setCreateUser(bocUserDto.getUpdateUser());
                            	listBocFilesDto.get(j).setGroupFile(1L);
                            	listBocFilesDto.get(j).setGroupId(bocUserDto.getUserId());
                            	listBocFilesDto.get(j).setCreateDate(bocUserDto.getUpdateDate());
                    			session.save(listBocFilesDto.get(j).toEntity());
							}
                    	}
                    	if(bocUserDto.getFileId() == null) {
                    		for (int i = 0; i < listBocFilesEntity.size(); i++) {
                    			entityManager.remove(listBocFilesEntity.get(i));
                    			FilesUtils.deleteFileByPath(listBocFilesEntity.get(i).getFilePath(), uploadFolder);
    						}
                    	}
                    	if(bocUserDto.getPassword() == null) {
                    		bocUserDto.setPassword(bocUserEntity.getPassword());
                    	} else {
                    		bocUserDto.setPassword(StringUtils.passwordEncoder().encode(bocUserDto.getPassword().trim()));
                    	}
                        entityManager.merge(bocUserDto.toEntity());
                    } else{
                        resultDTO.setKey(Constants.RESULT.NODATA);
                    }
                }
                deleteAndInsertBocRoleByUser(bocUserDto);
                deleteAndInsertBocUnitByUser(bocUserDto);
            }
        }
        catch (Exception ex) {
            resultDTO.setKey(Constants.RESULT.ERROR);
            resultDTO.setMessage(ex.getMessage());
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultDTO;
    }

    @SuppressWarnings("unchecked")
	public ResultDto validateData(BocUserDto bocUserDto) {
        ResultDto resultDTO = new ResultDto();
        resultDTO.setKey(Constants.RESULT.SUCCESS);
        
        if (StringUtils.isStringNullOrEmpty(bocUserDto.getUserName())){
            resultDTO.setKey(Constants.RESULT.ERROR);
            resultDTO.setMessage("userName is null");

            return resultDTO;
        }
        
        List<BocUserEntity> listCheckUserName = findByMultilParam(BocUserEntity.class,
        		"userName", bocUserDto.getUserName());
        
        if(bocUserDto.getUserId() == null) {
        	if (!listCheckUserName.isEmpty()) {
        		resultDTO.setKey(Constants.RESULT.DUPLICATE);
                resultDTO.setMessage("USERNAME");
                return resultDTO;
        	}
        } else {
        	BocUserEntity e = entityManager.find(BocUserEntity.class, bocUserDto.getUserId());
        	if(!bocUserDto.getUserName().equals(e.getUserName()) && !listCheckUserName.isEmpty()) {
        		resultDTO.setKey(Constants.RESULT.DUPLICATE);
                resultDTO.setMessage("USERNAME");
                return resultDTO;
    		}
        }
        
        if (StringUtils.isStringNullOrEmpty(bocUserDto.getFullName())){
            resultDTO.setKey(Constants.RESULT.ERROR);
            resultDTO.setMessage("fullName is null");

            return resultDTO;
        }

        /*if (StringUtils.isStringNullOrEmpty(bocUserDto.getPhoneNumber())){
            resultDTO.setKey(Constants.RESULT.ERROR);
            resultDTO.setMessage("phoneNumber is null");

            return resultDTO;
        }*/

        if (StringUtils.isStringNullOrEmpty(bocUserDto.getEmail())){
            resultDTO.setKey(Constants.RESULT.ERROR);
            resultDTO.setMessage("email is null");

            return resultDTO;
        }
        
        if(bocUserDto.getUserId() == null) {
        	if(StringUtils.isStringNullOrEmpty(bocUserDto.getPassword())){
                resultDTO.setKey(Constants.RESULT.ERROR);
                resultDTO.setMessage("password is null");

                return resultDTO;
            }
        }
        
        if("CNTT".equals(bocUserDto.getCompany())) {
        	if(checkIsExistPositionOfUnitCNTT(bocUserDto)) {
            	resultDTO.setKey(Constants.RESULT.DUPLICATE);
                resultDTO.setMessage("UNIT");
                return resultDTO;
            }
        } else {
        	if(checkIsExistPositionOfUnit(bocUserDto)) {
            	resultDTO.setKey(Constants.RESULT.DUPLICATE);
                resultDTO.setMessage("UNIT");
                return resultDTO;
            }
        }
        
        return resultDTO;
    }
    
    public Boolean checkIsExistPositionOfUnitCNTT(BocUserDto bocUserDto) {
    	Boolean check = false;
    	try{
    		if(bocUserDto.getPosition() != null && bocUserDto.getCompany() != null) {
    			String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-list-check-user-cntt");
            	Map<String, Object> parameters = new HashMap<>();
                parameters.put("position", bocUserDto.getPosition());
                parameters.put("company", bocUserDto.getCompany());
                List<BocUserDto> list = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocUserDto.class));
                
    	        if (list != null && list.size() > 0){
    	        	if(bocUserDto.getUserId() == null) {
    	        		if("GD".equals(bocUserDto.getPosition())) {
	        				check = true;
	        			}
    	        	} else {
    	        		if(bocUserDto.getUserId().longValue() != list.get(0).getUserId().longValue()) {
    	        			if("GD".equals(bocUserDto.getPosition())) {
    	        				check = true;
    	        			}
    	        		}
    	        	}
    	        }
    		} else {
    			check = false;
    		}
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }

        return check;
    }
    
    public Boolean checkIsExistPositionOfUnit(BocUserDto bocUserDto) {
    	Boolean check = false;
    	try{
    		if(bocUserDto.getPosition() != null && bocUserDto.getCompany() != null && bocUserDto.getListUnit().size() > 0) {
    			String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_USER, "get-list-check-user");
            	Map<String, Object> parameters = new HashMap<>();
                parameters.put("position", bocUserDto.getPosition());
                parameters.put("company", bocUserDto.getCompany());
                parameters.put("unitId", bocUserDto.getListUnit().get(0));
                List<BocUserDto> list = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocUserDto.class));
                
    	        if (list != null && list.size() > 0){
    	        	if(bocUserDto.getUserId() == null) {
    	        		if("GD".equals(bocUserDto.getPosition())) {
	        				check = true;
	        			}
    	        	} else {
    	        		if(bocUserDto.getUserId().longValue() != list.get(0).getUserId().longValue()) {
    	        			if("GD".equals(bocUserDto.getPosition())) {
    	        				check = true;
    	        			}
    	        		}
    	        	}
    	        }
    		} else {
    			check = false;
    		}
    	} catch (Exception ex){
    		LOGGER.error(ex.getMessage(), ex);
        }

        return check;
    }
}
