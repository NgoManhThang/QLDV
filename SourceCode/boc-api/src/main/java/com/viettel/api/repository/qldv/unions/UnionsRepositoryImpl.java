package com.viettel.api.repository.qldv.unions;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;
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
}
