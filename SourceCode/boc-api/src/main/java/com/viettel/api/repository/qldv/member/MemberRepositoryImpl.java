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
import org.hibernate.Session;
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
@Transactional
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
}
