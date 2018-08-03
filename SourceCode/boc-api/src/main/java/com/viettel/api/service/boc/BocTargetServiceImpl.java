package com.viettel.api.service.boc;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.viettel.api.config.Constants;
import com.viettel.api.dto.CellConfigExport;
import com.viettel.api.dto.ConfigFileExport;
import com.viettel.api.dto.ConfigHeaderExport;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocCodeDto;
import com.viettel.api.dto.boc.BocTargetDto;
import com.viettel.api.dto.boc.CataLocationDto;
import com.viettel.api.dto.boc.KVDto;
import com.viettel.api.repository.boc.BocTargetRepository;
import com.viettel.api.utils.BundleUtils;
import com.viettel.api.utils.CommonExport;
import com.viettel.api.utils.CommonImport;
import com.viettel.api.utils.ErrorInfo;
import com.viettel.api.utils.FilesUtils;
import com.viettel.api.utils.StringUtils;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Service
public class BocTargetServiceImpl implements BocTargetService{
	private final Logger log = LoggerFactory.getLogger(BocTargetServiceImpl.class);
	
	private Map<String, String> mapErrorResult = new HashMap<>();
    private Map<String, String> mapAddObject = new HashMap<>();
    List<BocTargetDto> lstAddOrUpdate = new ArrayList<>();
    protected static List<ErrorInfo> cellErrorList = new ArrayList<>();

    @Autowired
    protected BocTargetRepository bocTargetRepository;
    
    @Override
    public Datatable search(BocTargetDto bocTargetDto) {
        log.debug("Request to search : {}", bocTargetDto);
        
        return bocTargetRepository.search(bocTargetDto);
    }
    
    @Override
    public List<BocCodeDto> getTypeTarget() {
        return bocTargetRepository.getTypeTarget();
    }
    
    @Override
	public List<CataLocationDto> getListProvince() {
    	return bocTargetRepository.getListProvince();
	}
	
	@Override
	public List<CataLocationDto> getListDistrictByProvinceCode(CataLocationDto cataLocationDto) {
		return bocTargetRepository.getListDistrictByProvinceCode(cataLocationDto);
	}
	
	@Override
    public ResultDto delete(BocTargetDto bocTargetDto) {
        log.debug("Request to delete : {}", bocTargetDto);

        return bocTargetRepository.delete(bocTargetDto);
    }
	
	@Override
	public File exportTarget(BocTargetDto bocTargetDto) throws Exception {
		List<BocTargetDto> lstData = bocTargetRepository.getListForExport(bocTargetDto);
        Locale locale = Locale.forLanguageTag("vi");
        List<ConfigFileExport> listConfig = new ArrayList<>();

        ConfigHeaderExport columnSheet1;
        List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
        columnSheet1 = new ConfigHeaderExport("targetMonth", "LEFT", false, 0, 0, new String[]{}, null, "STRING");
        lstHeaderSheet1.add(columnSheet1);
        columnSheet1 = new ConfigHeaderExport("targetName", "LEFT", false, 0, 0, new String[]{}, null, "STRING");
        lstHeaderSheet1.add(columnSheet1);
        columnSheet1 = new ConfigHeaderExport("regionCode", "LEFT", false, 0, 0, new String[]{}, null, "STRING");
        lstHeaderSheet1.add(columnSheet1);
        columnSheet1 = new ConfigHeaderExport("regionName", "LEFT", false, 0, 0, new String[]{}, null, "STRING");
        lstHeaderSheet1.add(columnSheet1);
        columnSheet1 = new ConfigHeaderExport("targetNum", "RIGHT", false, 0, 0, new String[]{}, null, "STRING");
        lstHeaderSheet1.add(columnSheet1);
        columnSheet1 = new ConfigHeaderExport("warning1", "RIGHT", false, 0, 0, new String[]{}, null, "STRING");
        lstHeaderSheet1.add(columnSheet1);
        columnSheet1 = new ConfigHeaderExport("warning2", "RIGHT", false, 0, 0, new String[]{}, null, "STRING");
        lstHeaderSheet1.add(columnSheet1);
        columnSheet1 = new ConfigHeaderExport("createdFullName", "LEFT", false, 0, 0, new String[]{}, null, "STRING");
        lstHeaderSheet1.add(columnSheet1);
        columnSheet1 = new ConfigHeaderExport("createdDateString", "LEFT", false, 0, 0, new String[]{}, null, "STRING");
        lstHeaderSheet1.add(columnSheet1);

        Map<String, String> fieldSplit1 = new HashMap<String, String>();

        ConfigFileExport configSheet1 = new ConfigFileExport(
                lstData
                , BundleUtils.getLangString("exportTarget.sheetNameTarget", locale)
                , BundleUtils.getLangString("exportTarget.titleFile", locale)
                , ""
                , 5
                , 2
                , 9
                , true
                , "exportTarget"
                , lstHeaderSheet1
                , fieldSplit1
                , ""
        );
        configSheet1.setLangKey("vi");

        List<CellConfigExport> lstCellSheet1 = new ArrayList<>();
        CellConfigExport cellSheet1;
        cellSheet1 = new CellConfigExport(5, 0, 0, 0, BundleUtils.getLangString("common.stt", locale), "HEAD", "STRING");
        lstCellSheet1.add(cellSheet1);
        configSheet1.setLstCreatCell(lstCellSheet1);

        listConfig.add(configSheet1);

        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = URLDecoder.decode(path, "UTF-8");
        String fileTemplate = fullPath + "META-INF" + File.separator
                + "templates" + File.separator
                + "TEMPLATE_EXPORT.xlsx";
        String pathArr[] = fullPath.split("/target/classes");
        String rootPath = pathArr[0].substring(0, pathArr[0].lastIndexOf("/"));
        rootPath += File.separator + "report_out" + File.separator;

        File fileExport = CommonExport.exportExcel(
                fileTemplate
                , "DANH_SACH_MUC_TIEU-"
                , listConfig
                , rootPath
                , null
        );
        return fileExport;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public ResultDto importData(MultipartFile uploadfile) {
        ResultDto resultDTO = new ResultDto();
        resultDTO.setKey(Constants.RESULT.SUCCESS);
        try {
            if (uploadfile.isEmpty()) {
                resultDTO.setKey(Constants.RESULT.FILE_IS_NULL);
            } else {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                String fullPath = URLDecoder.decode(classLoader.getResource("").getPath(), "UTF-8");
                String pathArr[] = fullPath.split("/target/classes");
                String rootPath = pathArr[0].substring(0, pathArr[0].lastIndexOf("/"));
                rootPath += File.separator + "file_upload_out" + File.separator;
                String filePath = FilesUtils.saveUploadedFile(uploadfile, rootPath);
                resultDTO = validateFileImport(filePath);
                if (!Constants.RESULT.SUCCESS.equals(resultDTO.getKey())) {
                    return resultDTO;
                }

                File fileImport = new File(filePath);
                // Lay du lieu import
                List<Object[]> lstData = CommonImport.getDataFromExcelFile(
                        fileImport,
                        0,//sheet
                        4,//begin row
                        0,//from column
                        6,//to column
                        1000
                );

                if (lstData.size() > 1500) {
                    resultDTO.setKey(Constants.RESULT.DATA_OVER);
                    return resultDTO;
                }

                lstAddOrUpdate = new ArrayList<>();
                mapAddObject.clear();
                mapErrorResult.clear();
                cellErrorList.clear();

                if (!lstData.isEmpty()) {
                    int row = 4;
                    boolean allTrue = true;

                    for (Object[] obj : lstData) {
                    	BocTargetDto dto = new BocTargetDto();
                        if (obj[1] != null) {
                            dto.setTargetMonth(obj[1].toString().trim());
                            if(obj[1].toString().trim().length() == 4) {
                            	dto.setMonthYear("N");
                            }
                            if(obj[1].toString().trim().length() == 6) {
                            	dto.setMonthYear("T");
                            }
                        } else {
                            dto.setTargetMonth(null);
                        }
                        if (obj[2] != null) {
                            dto.setTargetType(obj[2].toString().trim());
                        } else {
                            dto.setTargetType(null);
                        }
                        if (obj[3] != null) {
                            dto.setRegionCode(obj[3].toString().trim());
                        } else {
                            dto.setRegionCode(null);
                        }
                        if (obj[4] != null) {
                            dto.setTargetNumString(obj[4].toString().trim());
                        } else {
                            dto.setTargetNumString(null);
                        }
                        
                        if (obj[5] != null) {
                            dto.setWarning1String(obj[5].toString().trim());
                        } else {
                            dto.setWarning1String(null);
                        }
                        
                        if (obj[6] != null) {
                            dto.setWarning2String(obj[6].toString().trim());
                        } else {
                            dto.setWarning2String(null);
                        }
                        
                        if (validateImportInfo(dto)) {
                        	DecimalFormat df = new DecimalFormat("0.000");
                        	dto.setTargetNum(df.parse(dto.getTargetNumString()).floatValue());
                        	if(dto.getWarning1String() != null) {
                        		dto.setWarning1(df.parse(dto.getWarning1String()).floatValue());
                        	}
                        	if(dto.getWarning2String() != null) {
                        		dto.setWarning2(df.parse(dto.getWarning2String()).floatValue());
                        	}
                        	if(bocTargetRepository.checkDuplicateImport(dto.getRegionCode(), dto.getTargetMonth(), dto.getTargetType())) {
                        		dto.setTypeInsertOrUpdate("INSERT");
                            } else {
                            	dto.setTypeInsertOrUpdate("UPDATE");
                            }
                            lstAddOrUpdate.add(dto);
                        } else {
                            allTrue = false;
                            cellErrorList.add(new ErrorInfo(row, getValidateResult(obj)));
                        }
                        row++;
                    }
                    if (allTrue) {
                        if (!lstAddOrUpdate.isEmpty()) {
                            resultDTO = bocTargetRepository.insertList(lstAddOrUpdate);
                            if ("SUCCESS".equals(resultDTO.getKey())) {
                                File fileExport = exportFileResult(lstData);

                                resultDTO.setFile(fileExport);
                            }
                        }
                    } else {
                        File fileExport = exportFileResult(lstData);

                        resultDTO.setKey(Constants.RESULT.ERROR);
                        resultDTO.setFile(fileExport);
                    }
                } else {
                    resultDTO.setKey(Constants.RESULT.NODATA);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            resultDTO.setKey(Constants.RESULT.ERROR);
            resultDTO.setMessage(ex.getMessage());
        }

        return resultDTO;
    }
	
	public ResultDto validateFileImport(String path) throws UnsupportedEncodingException {
        ResultDto resultDTO = new ResultDto();
        resultDTO.setKey(Constants.RESULT.SUCCESS);
        if (!path.contains(".xls")) {
            resultDTO.setKey(Constants.RESULT.FILE_TYPE_INVALID);
        } else {
            String path1 = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path1, "UTF-8");
            String templatePathOut = fullPath + "META-INF" + File.separator
                    + "templates" + File.separator + "IMPORT_TARGET.xlsx";
            boolean check = CommonImport.validateFileTmp(path, templatePathOut, 3);
            if (!check) {
                resultDTO.setKey(Constants.RESULT.FILE_INVALID);
            }
        }
        return resultDTO;
    }

    private File exportFileResult(List<Object[]> lstImport) throws Exception {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = URLDecoder.decode(path, "UTF-8");
        String templatePathOut = fullPath + "META-INF" + File.separator
                + "templates" + File.separator + "IMPORT_TARGET.xlsx";

        String pathArr[] = fullPath.split("/target/classes");
        String rootPath = pathArr[0].substring(0, pathArr[0].lastIndexOf("/"));
        rootPath += File.separator + "report_out" + File.separator;
        File fileExport = CommonExport.exportFileResult(
                lstImport
                , cellErrorList
                , templatePathOut
                , rootPath
                , "TARGET_IMPORT_RESULT"
                , 4
                , 7
        );

        return fileExport;
    }

    private String getValidateResult(Object[] obj) {
        return mapErrorResult.get(obj[1]);
    }

    private boolean validateImportInfo(BocTargetDto dto) {
        if (StringUtils.isStringNullOrEmpty(dto.getTargetMonth())) {
            setMapResult(dto, "Thời gian giao chỉ tiêu " + BundleUtils.getLangString("common.notnull"));
            return false;
        } else {
        	if(dto.getTargetMonth().trim().length() == 6) {
        		String year = dto.getTargetMonth().trim().substring(0, 4);
        		String month = dto.getTargetMonth().trim().substring(dto.getTargetMonth().trim().length() - 2);
        		try {
        			Long valueYear = Long.valueOf(year);
					Long valueMonth = Long.valueOf(month);
					if(valueMonth < 1 || valueMonth > 12 || valueYear < 1000) {
						setMapResult(dto, "Thời gian giao chỉ tiêu không đúng định dạng");
		                return false;
					}
				} catch (Exception e) {
					setMapResult(dto, "Thời gian giao chỉ tiêu không đúng định dạng");
	                return false;
				}
        	} else if(dto.getTargetMonth().trim().length() == 4) {
        		try {
        			Long valueYear = Long.valueOf(dto.getTargetMonth().trim());
					if(valueYear < 1000) {
						setMapResult(dto, "Thời gian giao chỉ tiêu không đúng định dạng");
		                return false;
					}
				} catch (Exception e) {
					setMapResult(dto, "Thời gian giao chỉ tiêu không đúng định dạng");
	                return false;
				}
        	} else {
        		setMapResult(dto, "Thời gian giao chỉ tiêu không đúng định dạng");
                return false;
        	}
        }

        if (StringUtils.isStringNullOrEmpty(dto.getTargetType())) {
            setMapResult(dto, "Loại chỉ tiêu " + BundleUtils.getLangString("common.notnull"));
            return false;
        }

        if (StringUtils.isStringNullOrEmpty(dto.getRegionCode())) {
        	setMapResult(dto, "Mã địa bàn " + BundleUtils.getLangString("common.notnull"));
            return false;
        } else {
        	List<KVDto> lstDataCheck = bocTargetRepository.getListProvinceDistrictForExport();
        	Boolean check = false;
        	for (int i = 0; i < lstDataCheck.size(); i++) {
				if(dto.getRegionCode().trim().equals(lstDataCheck.get(i).getKvCode())) {
					check = true;
				}
			}
        	if(!check) {
        		setMapResult(dto, "Mã địa bàn không tồn tại trong hệ thống");
                return false;
        	}
        }
        
        DecimalFormat df = new DecimalFormat("0.000");
        if (dto.getTargetNumString() == null) {
            setMapResult(dto, "Mục tiêu " + BundleUtils.getLangString("common.notnull"));
            return false;
        } else {
        	try {
            	dto.setTargetNum(df.parse(dto.getTargetNumString()).floatValue());
        		Float targetNum = Float.valueOf(dto.getTargetNumString());
				if(targetNum > 999999999F || Float.isInfinite(targetNum)) {
					setMapResult(dto, "Mục tiêu không đúng định dạng");
	                return false;
				}
			} catch (Exception e) {
				setMapResult(dto, "Mục tiêu không đúng định dạng");
                return false;
			}
        }
        
        if (dto.getWarning1String() == null) {
            /*setMapResult(dto, "Cảnh báo mức 1 " + BundleUtils.getLangString("common.notnull"));
            return false;*/
        } else {
        	try {
        		dto.setWarning1(df.parse(dto.getWarning1String()).floatValue());
        		Float warning1 = Float.valueOf(dto.getWarning1String());
				if(warning1 > 999999999F || Float.isInfinite(warning1)) {
					setMapResult(dto, "Cảnh báo mức 1 không đúng định dạng");
	                return false;
				}
			} catch (Exception e) {
				setMapResult(dto, "Cảnh báo mức 1 không đúng định dạng");
                return false;
			}
        }

        if (dto.getWarning2String() == null) {
            /*setMapResult(dto, "Cảnh báo mức 2 " + BundleUtils.getLangString("common.notnull"));
            return false;*/
        } else {
        	try {
        		dto.setWarning2(df.parse(dto.getWarning2String()).floatValue());
        		Float warning2 = Float.valueOf(dto.getWarning2String());
				if(warning2 > 999999999F || Float.isInfinite(warning2)) {
					setMapResult(dto, "Cảnh báo mức 2 không đúng định dạng");
	                return false;
				}
			} catch (Exception e) {
				setMapResult(dto, "Cảnh báo mức 2 không đúng định dạng");
                return false;
			}
        }
        
        List<String> lsTypeTarget = new ArrayList<>();
        List<BocCodeDto> listBocCodeDto = bocTargetRepository.getTypeTarget();
        for (int i = 0; i < listBocCodeDto.size(); i++) {
        	lsTypeTarget.add(listBocCodeDto.get(i).getDecode());
		}
        if (!lsTypeTarget.contains(dto.getTargetType())) {
            setMapResult(dto, "Loại chỉ tiêu không chính xác");
            return false;
        } else {
        	for (int i = 0; i < listBocCodeDto.size(); i++) {
            	if(listBocCodeDto.get(i).getDecode().equals(dto.getTargetType())) {
            		dto.setTargetType(listBocCodeDto.get(i).getCode());
            	}
    		}
        }
        
        return true;
    }

    private void setMapResult(BocTargetDto dto, String propertie) {
        mapErrorResult.put(dto.getTargetMonth(), propertie);
    }
    
    @Override
	public List<KVDto> getListProvinceDistrictForExport() {
    	return bocTargetRepository.getListProvinceDistrictForExport();
	}
}
