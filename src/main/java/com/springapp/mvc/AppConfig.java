package com.springapp.mvc;

import com.springapp.mvc.data.UserRepositry;
import com.springapp.mvc.interceptors.CheckAuthorizedInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import java.beans.PropertyVetoException;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 15/7/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */

@Configuration
@ComponentScan(basePackages = "com.springapp.mvc")
//@PropertySource(value = "classpath:/application.properties")
@EnableWebMvc
@EnableTransactionManagement
public class AppConfig extends WebMvcConfigurerAdapter{



    @Bean
    public JdbcTemplate jdbcTemplate() throws PropertyVetoException {

        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName("DataSource");
        source.setServerName("localhost");
        source.setPortNumber(5432);
        source.setDatabaseName("twitter");
        source.setUser("postgres");
        source.setPassword("postgres");
        source.setMaxConnections(10);
        return new JdbcTemplate(source);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertiesConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        CheckAuthorizedInterceptor interceptor = new CheckAuthorizedInterceptor();
        registry.addInterceptor(interceptor).addPathPatterns( "/" );
    }
}
