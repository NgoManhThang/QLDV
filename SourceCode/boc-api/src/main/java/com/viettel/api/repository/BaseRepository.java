package com.viettel.api.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Base repository.
 * 
 * @author Nam, Nguyen Hoai
 */
@Repository
public abstract class BaseRepository<T extends Serializable> {
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	protected Session getSession() {
		Session session = entityManager.unwrap(Session.class);
		return session;
	}
	
	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	public List<T> getListDataBySqlQuery(String sqlQuery,
										Map<String, Object> parameters,
										int page, int pageSize,
										Class<T> mappedClass, Boolean isPaging,
										String sortName, String sortType) {
		if(isPaging) {
			sqlQuery = " SELECT * FROM ( "
					+ " SELECT * FROM ( SELECT a.*, rownum indexRow FROM ( "
					+ sqlQuery 
					+ " ) a WHERE rownum < ((:p_page_number * :p_page_length) + 1 )) WHERE indexRow >= (((:p_page_number-1) * :p_page_length) + 1) "
					+ " ) T_TABLE_NAME, "
					+ " ( SELECT COUNT(*) totalRow FROM ( "
					+ sqlQuery
					+ " ) T_TABLE_TOTAL ) T_TABLE_NAME_TOTAL ";
			sqlQuery = sqlQuery + " ORDER BY " + sortName + " " + sortType;
			parameters.put("p_page_number", page);
	        parameters.put("p_page_length", pageSize);
		}
		List<T> list = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(mappedClass));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> persistentClass, Object orderName, Object orderType) {
		String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ORDER BY NLSSORT(t." + orderName + ", 'nls_sort = Vietnamese') " + orderType;
        return entityManager.createQuery(sqlQuery).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByMultilParam(Class<T> persistentClass, Object... params) {
		String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
		if(params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				if(i % 2 == 0) {
					sqlQuery += " AND t." + params[i] + " = '" + params[i+1] + "' ";
				}
			}
		}
        return entityManager.createQuery(sqlQuery).getResultList();
	}
}
