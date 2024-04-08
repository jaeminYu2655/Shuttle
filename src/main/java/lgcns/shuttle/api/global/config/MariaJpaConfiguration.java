package lgcns.shuttle.api.global.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(
        transactionManagerRef = "jpaTxManager",
        basePackages = "lgcns.shuttle.api.**.repository"
)
public class MariaJpaConfiguration {

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(@Qualifier("mariaDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPackagesToScan("lgcns.shuttle.**.entity");
        factory.setDataSource(dataSource);
        factory.setPersistenceUnitName("MySQL");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        factory.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.show_sql", false);  // sql은 log4j로 출력 org.hibernate.SQL=DEBUG
        properties.put("hibernate.globally_quoted_identifiers", true);  // 예약어 컬럼명 사용 허용
        factory.setJpaPropertyMap(properties);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean(name= "jpaTxManager")
    public PlatformTransactionManager jpaTxManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager;
    }
}