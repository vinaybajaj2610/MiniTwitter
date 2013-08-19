package com.springapp.mvc;

//import com.springapp.mvc.interceptors.CheckAuthorizedInterceptor;
import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.interceptors.ApiAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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

    @Autowired
    private UserRepository userRepository;

    @Bean
    public static JdbcTemplate jdbcTemplate() throws PropertyVetoException {

        PGPoolingDataSource source = new PGPoolingDataSource();
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
        ApiAuthenticationInterceptor interceptor = new ApiAuthenticationInterceptor(userRepository);
        registry.addInterceptor(interceptor).addPathPatterns( "/api/fetchUserPosts", "/api/fetchUserPosts/**", "/api/createPost", "/api/createPost/**","/api/fetchUsersNewsFeed","/api/fetchUsersNewsFeed/**");
    }

    @Bean
    public Jedis jedis() {
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);
        return jedisPool.getResource();
    }

}
