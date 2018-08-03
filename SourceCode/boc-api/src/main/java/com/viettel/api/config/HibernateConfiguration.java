package com.viettel.api.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.transaction.jta.JtaTransactionManager;

//@Configuration
public class HibernateConfiguration extends HibernateJpaAutoConfiguration {

	@Value("${spring.jpa.orm}")
	private String orm;
	
    private final Logger log = LoggerFactory.getLogger(HibernateConfiguration.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public HibernateConfiguration(DataSource dataSource, JpaProperties jpaProperties
    		, ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider) {
        super(dataSource, jpaProperties, jtaTransactionManagerProvider, null);
    }

//    @Override
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//    		EntityManagerFactoryBuilder factoryBuilder)
//    {
//        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = super.entityManagerFactory(factoryBuilder);
//        entityManagerFactoryBean.setMappingResources("orm.xml");
//        return entityManagerFactoryBean;
//    }
}
