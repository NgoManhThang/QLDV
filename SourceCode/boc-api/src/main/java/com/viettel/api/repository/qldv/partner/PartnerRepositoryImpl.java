package com.viettel.api.repository.qldv.partner;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.PartnerEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.PartnerDto;
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
public class PartnerRepositoryImpl extends BaseRepository implements PartnerRepository {
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
            maps.put("p_partner_status", dto.getStatus());
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return datatable;
    }

    @Override
    public ResultDto saveData(PartnerDto dto) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);
        Session session = getSession();
        entityManager = getEntityManager();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (StringUtils.isStringNullOrEmpty(dto.getPartnerId())) {
                dto.setCreateUser(auth.getName());
                dto.setUpdateUser(auth.getName());
                dto.setCreateDate(new Timestamp(System.currentTimeMillis()));
                dto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                long id = (long) session.save(dto.toEntity());
                resultDto.setId(String.valueOf(id));
            } else {
                PartnerEntity entity = entityManager.find(PartnerEntity.class, dto.getPartnerId());
                if (entity.getPartnerId() != null) {
                    dto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    dto.setUpdateUser(auth.getName());
                    entityManager.merge(dto.toEntity());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultDto;
    }

    @Override
    public PartnerEntity getDetail(PartnerDto dto) {
        entityManager = getEntityManager();
        PartnerEntity entity = entityManager.find(PartnerEntity.class, dto.getPartnerId());
        return entity;
    }

    @Override
    public ResultDto delete(PartnerDto dto) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);
        entityManager = getEntityManager();
        try {
            PartnerEntity entity = entityManager.find(PartnerEntity.class, dto.getPartnerId());
            if (entity.getPartnerId() != null) {
                entityManager.remove(entity);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultDto;
    }
}
