package com.springapp.mvc;

//import com.springapp.mvc.interceptors.CheckAuthorizedInterceptor;
import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.interceptors.ApiAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource(value = "classpath:/application.properties")
@EnableWebMvc
@EnableTransactionManagement
public class AppConfig extends WebMvcConfigurerAdapter{

    @Autowired
    private UserRepository userRepository;

    @Bean
    public static JdbcTemplate jdbcTemplate(@Value("${db.serverName}") String serverName,
                                            @Value("${db.portNumber}") String portNumber,
                                            @Value("${db.databaseName}") String databaseName,
                                            @Value("${db.maxConnections}") String maxConnections,
                                            @Value("${db.user}") String username,
                                            @Value("${db.password}") String password,
                                            @Value("${jedis.maxIdle}") String maxIdle,
                                            @Value("${jedis.maxActive}") String maxActive,
                                            @Value("${jedis.PortNumber}") String jedisPort,
                                            @Value("${jedis.serverAddress}") String jedisServerAddress ) throws PropertyVetoException {

        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setServerName(serverName);
        source.setPortNumber(Integer.parseInt(portNumber));
        source.setDatabaseName(databaseName);
        source.setUser(username);
        source.setPassword(password);
        source.setMaxConnections(Integer.parseInt(maxConnections));
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
    public static JedisPool jedisPool(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(5);
        poolConfig.setMaxActive(50);
        return new JedisPool(poolConfig,"localhost", 6379);
    }
    @Bean
    public static Jedis jedis(){
        return jedisPool().getResource();
    }
}
