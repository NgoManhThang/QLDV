package com.viettel.api.repository.qldv.member;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.MemberEntity;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.MemberDto;
import com.viettel.api.repository.BaseRepository;
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

@Repository
@Transactional
public class MemberRepositoryImpl extends BaseRepository implements MemberRepository {
    Logger logger = LoggerFactory.getLogger(MemberRepositoryImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public ResultDto saveData(MemberDto dto) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);
        entityManager = getEntityManager();
        Session session = getSession();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (StringUtils.isStringNullOrEmpty(dto.getUnionMemberId())) {
                dto.setCreateUser(auth.getName());
                dto.setCreateDate(new Timestamp(System.currentTimeMillis()));
                dto.setUpdateUser(auth.getName());
                dto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                long id = (long) session.save(dto.toEntity());
                resultDto.setKey(String.valueOf(id));
            } else {
                MemberEntity entity = entityManager.find(MemberEntity.class, dto.getUnionMemberId());
                if (entity.getUnionMemberId() != null) {
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
}
