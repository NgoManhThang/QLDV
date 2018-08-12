package com.viettel.api.repository.qldv.partner;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.PartnerDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;
import com.viettel.api.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Repository
@Transactional
public class PartnerRepositoryImpl extends BaseRepository implements PartnerRepository{
    Logger logger = LoggerFactory.getLogger(PartnerRepositoryImpl.class);

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Datatable seachPartner(PartnerDto dto) {
        Datatable datatable = new Datatable();
        try {
            StringBuilder sql = new StringBuilder(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_PARTNER, "search-partner"));
            Map<String, String> maps = new HashMap<>();
            maps.put("p_partner_code", StringUtils.convertUpperParamContains(dto.getPartnerCode()));
            maps.put("p_partner_name", StringUtils.convertUpperParamContains(dto.getPartnerName()));
            maps.put("p_partner_type", dto.getPartnerType());
            List<PartnerDto> list = getListDataBySqlQuery(sql.toString(),
                    maps, dto.getPage(), dto.getPageSize(),
                    PartnerDto.class, true,
                    dto.getSortName(), dto.getSortType());

            int count = 0;
            if (list.size() > 0) {
                count = list.get(0).getTotalRow();
            }

            datatable.setRecordsTotal(count);
            if (dto.getPageSize() > 0) {
                if (count % dto.getPageSize() == 0) {
                    datatable.setDraw(count / dto.getPageSize());
                } else {
                    datatable.setDraw((count / dto.getPageSize()) + 1);
                }
            }
            datatable.setData(list);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return datatable;
    }
}
