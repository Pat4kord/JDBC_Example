package org.pat4kord;

import org.pat4kord.DAO.manipulationSQL.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
@PropertySource("classpath:db/properties")
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    @Value("${driverClassName}")
    private String driverClassName;
    @Value("${url}")
    private String url;
    @Value("${userDB}")
    private String userName;
    @Value("${password}")
    private String password;
    @Lazy
    @Bean
    public DataSource dataSource(){
        try {
            SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
            Class<? extends Driver> driver
                    = (Class<? extends Driver>) Class.forName(driverClassName);

            dataSource.setDriverClass(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);

            return dataSource;
        }catch (Exception exception){
            logger.error("Data source not received: " + exception.getLocalizedMessage());
            return null;
        }
    }
    @Bean
    @DependsOn("dataSource")
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    @DependsOn("dataSource")
    public SelectAllCountry selectAllCountry(){
        return new SelectAllCountry(dataSource());
    }

    @Bean
    @DependsOn("dataSource")
    public SelectByNameCountry selectByNameCountry(){
        return new SelectByNameCountry(dataSource());
    }

    @Bean
    @DependsOn("dataSource")
    public SelectCityById selectCityById(){
        return new SelectCityById(dataSource());
    }

    @Bean
    @DependsOn("dataSource")
    public StoredFunctionGetCityNameById storedFunctionGetCityNameById(){
        return new StoredFunctionGetCityNameById(dataSource());
    }

    @Bean
    @DependsOn("dataSource")
    public UpdateCountry updateCountry(){
        return new UpdateCountry(dataSource());
    }
}
