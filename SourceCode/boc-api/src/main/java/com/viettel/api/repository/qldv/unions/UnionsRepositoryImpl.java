package com.viettel.api.repository.qldv.unions;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.UnionsEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
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

@SuppressWarnings("rawtypes")
@Repository
@Transactional
public class UnionsRepositoryImpl extends BaseRepository implements UnionsRepository {
    Logger logger = LoggerFactory.getLogger(UnionsRepositoryImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Datatable search(UnionsDto dto) {
        Datatable datatable = new Datatable();
        try {
            StringBuilder sql = new StringBuilder(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_UNIONS, "search-unions"));
            Map<String, String> maps = new HashMap<>();
            maps.put("union_name", StringUtils.convertUpperParamContains(dto.getUnionName()));
            maps.put("from_date_from", dto.getFromDateFrom());
            maps.put("from_date_to", dto.getFromDateTo());
            maps.put("to_date_from", dto.getToDateFrom());
            maps.put("to_date_to", dto.getToDateTo());
            maps.put("partner_id_list", StringUtils.stringJoinCommand(dto.getLstPartnerId()));
            maps.put("union_type_code", StringUtils.stringJoinCommand(dto.getLstUnionType()));
            maps.put("union_status_code", StringUtils.stringJoinCommand(dto.getLstUnionStatus()));

            List<UnionsDto> list = getListDataBySqlQuery(sql.toString(),
                    maps, dto.getPage(),
                    dto.getPageSize(),
                    UnionsDto.class,
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
    public ResultDto saveData(UnionsDto dto) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);
        entityManager = getEntityManager();
        Session session = getSession();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (StringUtils.isStringNullOrEmpty(dto.getUnionId())) {
                dto.setCreateUser(auth.getName());
                dto.setUpdateUser(auth.getName());
                dto.setCreateDate(new Timestamp(System.currentTimeMillis()));
                dto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                if (dto.getVietnameseNumber() == null) {
                    dto.setVietnameseNumber(0l);
                }
                if (dto.getForeignerNumber() == null) {
                    dto.setForeignerNumber(0l);
                }
                long id = (long) session.save(dto.toEntity());
                resultDto.setId(String.valueOf(id));
            } else {
                UnionsEntity entity = entityManager.find(UnionsEntity.class, dto.getUnionId());
                if (entity.getUnionId() != null) {
                    dto.setUpdateUser(auth.getName());
                    dto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    entityManager.merge(dto.toEntity());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultDto;
    }

    @Override
    public UnionsEntity getDetail(UnionsDto dto) {
        entityManager = getEntityManager();
        UnionsEntity unionsEntity = entityManager.find(UnionsEntity.class, dto.getUnionId());
        return unionsEntity;
    }
}
