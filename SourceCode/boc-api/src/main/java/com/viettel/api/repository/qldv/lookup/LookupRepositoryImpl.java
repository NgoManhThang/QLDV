package com.viettel.api.repository.qldv.lookup;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.MemberInOutEntity;
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
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
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
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_LOOKUP, "get-list-member");
            Map<String, String> maps = new HashMap<>();
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

    @Override
    public LookupDto scanBarcode(LookupDto dto) {
        StringBuilder sb = new StringBuilder(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_LOOKUP, "scan-barcode"));
        if ("OUT".equals(dto.getTypeScan())) {
            sb.append(" INNER JOIN (SELECT UNION_MEMBER_ID FROM (select UNION_MEMBER_ID from QLDV_MEMBER_INOUT ORDER BY TIME_IN DESC) WHERE ROWNUM = 1) T7 ON T1.UNION_MEMBER_ID = T7.UNION_MEMBER_ID");
        }
        sb.append(" WHERE (T1.BAR_CODE_USER = :bar_code OR T1.BAR_CODE_COMPUTER = :bar_code)");
        Map<String, String> maps = new HashMap<>();
        maps.put("bar_code", dto.getBarCode());
        LookupDto lookupDto = getNamedParameterJdbcTemplate().queryForObject(sb.toString(), maps, BeanPropertyRowMapper.newInstance(LookupDto.class));
        return lookupDto;
    }

    @Override
    public void save(LookupDto dto) {
        Session session = getSession();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        MemberInOutEntity entity = new MemberInOutEntity();
        entity.setUnionMemberId(dto.getUnionMemberId());
        entity.setUnionId(dto.getUnionId());
        entity.setUserIn(userName);
        session.save(entity);
    }

    @Override
    public void update(LookupDto dto) {
        Session session = getSession();
        entityManager = getEntityManager();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        String sql = "SELECT UNION_MEMBER_INOUT_ID FROM (select UNION_MEMBER_INOUT_ID from QLDV_MEMBER_INOUT WHERE UNION_MEMBER_ID = :p_id ORDER BY TIME_IN DESC) WHERE ROWNUM = 1";
        NativeQuery<BigDecimal> query = session.createNativeQuery(sql);
        query.setParameter("p_id", dto.getUnionMemberId());
        long id = query.uniqueResult().longValue();

        MemberInOutEntity entity = entityManager.find(MemberInOutEntity.class, id);
        entity.setUserOut(userName);
        entityManager.merge(entity);
    }

}
