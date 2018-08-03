select 
 bm.BOC_MODULE_ID bocModuleId,
 bm.MODULE_GROUP moduleGroup,
 bc.DECODE moduleGroupName,
 bm.MODULE_NAME moduleName,
 bm.JSON_PARAM jsonParam,
 bm.SERVICE service
from boc_module bm
left join boc_code bc on bc.CODE = bm.MODULE_GROUP 
where bm.MODULE_GROUP in (:p_list_boc_code)
order by NLSSORT(bm.MODULE_NAME, 'nls_sort = Vietnamese')