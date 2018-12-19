package com.viettel.api.repository.qldv.lookup;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.UnionsEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.LookupDto;
import com.viettel.api.dto.qldv.MemberDto;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;
import com.viettel.api.utils.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@Repository
public class LookupRepositoryImpl extends BaseRepository implements LookupRepository {
    Logger logger = LoggerFactory.getLogger(LookupRepositoryImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Datatable search(LookupDto dto) {
        Datatable datatable = new Datatable();
        try {
            StringBuilder sql = new StringBuilder(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_LOOKUP,
                    "search-lookup"));
            Map<String, String> maps = new HashMap<>();
            maps.put("union_name", StringUtils.convertUpperParamContains(dto.getUnionName()));
            maps.put("from_date_from", dto.getFromDateFrom());
            maps.put("from_date_to", dto.getFromDateTo());
            maps.put("to_date_from", dto.getToDateFrom());
            maps.put("to_date_to", dto.getToDateTo());
            maps.put("partner_id_list", StringUtils.stringJoinCommand(dto.getLstPartnerId()));

            List<LookupDto> list = getListDataBySqlQuery(sql.toString(),
                    maps, dto.getPage(),
                    dto.getPageSize(),
                    LookupDto.class,
                    true,
                    dto.getSortName(),
                    dto.getSortType());

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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return datatable;
    }

    @Override
    public Datatable searchMember(MemberDto dto) {
        Datatable datatable = new Datatable();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_LOOKUP, "get-list-member");             Map<String, String> maps = new HashMap<>();
            maps.put("union_id", String.valueOf(dto.getUnionId()));
            List<MemberDto> list = getListDataBySqlQuery(sql, maps,
                    dto.getPage(), dto.getPageSize(), MemberDto.class, true,
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return datatable;
    }

}
