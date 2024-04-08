package lgcns.shuttle.api.global.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {
    private final Environment env;

    @Bean(name = "mariaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.maria")
    public LazyConnectionDataSourceProxy mariaDataSource() {
        return getDataSourceProperties("spring.datasource.hikari.maria");
    }

    private LazyConnectionDataSourceProxy getDataSourceProperties(String prefix) {
        String jdbcUrl = env.getProperty(prefix + ".url");
        String userName = env.getProperty(prefix + ".username");
        String password = env.getProperty(prefix + ".password");
        String driverClassName = env.getProperty(prefix + ".driver-class-name");

        HikariDataSource dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(jdbcUrl)
                .username(userName)
                .password(password)
                .driverClassName(driverClassName)
                .build();

        return new LazyConnectionDataSourceProxy(dataSource);
    }
}
