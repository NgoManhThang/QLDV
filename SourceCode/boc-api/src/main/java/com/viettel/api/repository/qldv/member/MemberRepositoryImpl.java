package com.viettel.api.repository.qldv.member;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.FilesEntity;
import com.viettel.api.domain.qldv.MemberEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.FilesDto;
import com.viettel.api.dto.qldv.MemberDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;
import com.viettel.api.utils.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@SuppressWarnings("all")
public class MemberRepositoryImpl extends BaseRepository implements MemberRepository {
    Logger logger = LoggerFactory.getLogger(MemberRepositoryImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Datatable searchMember(MemberDto dto) {
        Datatable datatable = new Datatable();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_MEMBER, "get-list-member");
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
    public ResultDto saveData(MemberDto dto) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);
        entityManager = getEntityManager();
        Session session = getSession();
        FilesDto filesDto = new FilesDto();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            if (StringUtils.isStringNullOrEmpty(dto.getUnionMemberId())) {
                dto.setCreateUser(auth.getName());
                dto.setCreateDate(new Timestamp(System.currentTimeMillis()));
                dto.setUpdateUser(auth.getName());
                dto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                long id = (long) session.save(dto.toEntity());
                dto.setUnionMemberId(id);
                //Save file CMT
                if (StringUtils.isNotNullOrEmpty(dto.getFilePathCMT())) {
                    filesDto.setGroupId(id);
                    filesDto.setCreateUser(userName);
                    filesDto.setGroupFile(2L);
                    filesDto.setFileName(dto.getFileNameCMT());
                    filesDto.setFilePath(dto.getFilePathCMT());
                    session.save(filesDto.toEntity());
                }

                //Save file Computer
                if (StringUtils.isNotNullOrEmpty(dto.getFilePathComputer())) {
                    filesDto.setGroupId(id);
                    filesDto.setCreateUser(userName);
                    filesDto.setGroupFile(3L);
                    filesDto.setFileName(dto.getFileNameComputer());
                    filesDto.setFilePath(dto.getFilePathComputer());
                    session.save(filesDto.toEntity());
                }
                resultDto.setId(String.valueOf(id));
            } else {
                MemberEntity entity = entityManager.find(MemberEntity.class, dto.getUnionMemberId());
                if (entity.getUnionMemberId() != null) {
                    entity.setUpdateUser(auth.getName());
                    entity.setFullName(dto.getFullName());
                    entity.setMemberId(dto.getMemberId());
                    entity.setMemberType(dto.getMemberType());
                    entity.setNationalId(dto.getNationalId());
                    entity.setLaptopId(dto.getLaptopId());
                    entityManager.merge(entity);
                }

                //Xóa file CMT
                if (StringUtils.isNotNullOrEmpty(dto.getFileIdDeleteCMT())) {
                    FilesEntity fileDelete = entityManager.find(FilesEntity.class, Long.valueOf(dto.getFileIdDeleteCMT()));
                    if (fileDelete.getFileId() != null) {
                        entityManager.remove(fileDelete);
                    }
                }
                if (StringUtils.isNotNullOrEmpty(dto.getFileIdDeleteComputer())) {
                    FilesEntity fileDelete = entityManager.find(FilesEntity.class, Long.valueOf(dto.getFileIdDeleteComputer()));
                    if (fileDelete.getFileId() != null) {
                        entityManager.remove(fileDelete);
                    }
                }

                //Save or update file CMT
                if (StringUtils.isNotNullOrEmpty(dto.getFilePathCMT())) {
                    filesDto.setFileId(StringUtils.isNotNullOrEmpty(dto.getFileIdCMT()) ? Long.valueOf(dto.getFileIdCMT()) : null);
                    filesDto.setGroupId(Long.valueOf(dto.getUnionMemberId()));
                    filesDto.setCreateUser(userName);
                    filesDto.setGroupFile(2L);
                    filesDto.setFileName(dto.getFileNameCMT());
                    filesDto.setFilePath(dto.getFilePathCMT());
                    session.saveOrUpdate(filesDto.toEntity());
                }

                //Save or update file Computer
                if (StringUtils.isNotNullOrEmpty(dto.getFilePathComputer())) {
                    filesDto.setFileId(StringUtils.isNotNullOrEmpty(dto.getFileIdComputer()) ? Long.valueOf(dto.getFileIdComputer()) : null);
                    filesDto.setGroupId(Long.valueOf(dto.getUnionMemberId()));
                    filesDto.setCreateUser(userName);
                    filesDto.setGroupFile(3L);
                    filesDto.setFileName(dto.getFileNameComputer());
                    filesDto.setFilePath(dto.getFilePathComputer());
                    session.saveOrUpdate(filesDto.toEntity());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultDto;
    }

    @Override
    public MemberDto getDetail(MemberDto dto) {
        String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_MEMBER, "get-detail");
        Map<String, Long> maps = new HashMap<>();
        maps.put("member_id", dto.getUnionMemberId());
        return getNamedParameterJdbcTemplate().queryForObject(sql, maps, BeanPropertyRowMapper.newInstance(MemberDto.class));
    }

    @Override
    public ResultDto delete(MemberDto dto) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);
        try {
            entityManager = getEntityManager();
            Session session = getSession();
            MemberEntity memberEntity = entityManager.find(MemberEntity.class, Long.valueOf(dto.getUnionMemberId()));
            if (memberEntity != null) {
                entityManager.remove(memberEntity);
                List<FilesEntity> lstFile = session.createCriteria(FilesEntity.class)
                        .add(Restrictions.or(Restrictions.eq("groupFile", new Long(2)), Restrictions.eq("groupFile", new Long(3))))
                        .add(Restrictions.eq("groupId", dto.getUnionMemberId())).list();
                for (FilesEntity entity : lstFile) {
                    entityManager.remove(entity);
                }
            }
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            resultDto.setKey(Constants.RESULT.ERROR);
        }
        return resultDto;
    }

    @Override
    public void updateBarCode(MemberDto dto) {
        Session session = getSession();
        StringBuilder sb = new StringBuilder("UPDATE QLDV_UNIONS_MEMBER ");
        sb.append(" SET BAR_CODE_USER = :bar_code_user, BAR_CODE_COMPUTER = :bar_code_computer");
        sb.append(" WHERE UNION_MEMBER_ID = :member_id");

        NativeQuery query = session.createNativeQuery(sb.toString());

        query.setParameter("bar_code_user", dto.getUnionMemberId() + dto.getMemberId());
        query.setParameter("bar_code_computer", dto.getUnionMemberId() + dto.getLaptopId());
        query.setParameter("member_id", dto.getUnionMemberId());

        query.executeUpdate();
    }

    @Override
    public MemberDto countTypeNumPerson(MemberDto dto) {
        String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_QLDV_MEMBER, "count-type-num-person");
        Map<String, Long> maps = new HashMap<>();
        maps.put("union_id", dto.getUnionId());
        return getNamedParameterJdbcTemplate().queryForObject(sql, maps, BeanPropertyRowMapper.newInstance(MemberDto.class));
    }

    @Override
    public void updateNumPersonByUnionId(MemberDto dto) {
        Session session = getSession();
        StringBuilder sb = new StringBuilder("UPDATE QLDV_UNIONS");
        sb.append(" SET VIETNAMESE_NUMBER = :num_person_vn, FOREIGNER_NUMBER = :num_person_nn");
        sb.append(" WHERE UNION_ID = :union_id");

        NativeQuery query = session.createNativeQuery(sb.toString());

        query.setParameter("num_person_vn", dto.getNumPersonVN());
        query.setParameter("num_person_nn", dto.getNumPersonNN());
        query.setParameter("union_id", dto.getUnionId());

        query.executeUpdate();
    }
}
