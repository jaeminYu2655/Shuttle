package lgcns.shuttle.api.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
@MapperScan(basePackages = {"lgcns.shuttle.api.**.mapper"})
public class MariaMybatisConfiguration {

    private final ApplicationContext applicationContext;

    @Bean(name="mariaSqlSessionFactory")
    public SqlSessionFactory mariaSqlSessionFactory(@Qualifier("mariaDataSource") DataSource dataSource) throws Exception {
        org.apache.ibatis.session.Configuration mybatisConfiguration = getMybatisConfiguration();

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfiguration(mybatisConfiguration);
        sqlSessionFactoryBean.setMapperLocations(resolveMapperLocations());

        return sqlSessionFactoryBean.getObject();
    }

    public Resource[] resolveMapperLocations() {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<String> mapperLocations = new ArrayList<>();
        mapperLocations.add("classpath:mapper/*/*.xml");
        mapperLocations.add("classpath:mapper/*/*.xml");
        List<Resource> resources = new ArrayList<>();
        if (!mapperLocations.isEmpty()) {
            for (String mapperLocation : mapperLocations) {
                try {
                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                } catch (IOException e) {
                    log.error("Mybatis resources Get Exception Occur", e);
                }
            }
        }
        return resources.toArray(new Resource[resources.size()]);
    }

    @Bean(name = "mariaSqlSessionTemplate")
    public SqlSessionTemplate oracleSqlSessionTemplate(@Qualifier("mariaSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    private org.apache.ibatis.session.Configuration getMybatisConfiguration() {
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();

        config.setDefaultExecutorType(ExecutorType.SIMPLE);
        config.setDefaultFetchSize(1000); // fetchSize 1000건
        config.setDefaultStatementTimeout(60000); // 60초
        config.setCacheEnabled(false);
        config.setLazyLoadingEnabled(false);
        config.setMultipleResultSetsEnabled(true);
        config.setUseGeneratedKeys(false);
        config.setMapUnderscoreToCamelCase(true);
        config.setLogPrefix("[Oracle]");

        return config;
    }
}