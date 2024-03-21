package com.config.database;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.*;

/**
 * @ClassName: DataSourceConfig
 * @author: zqz
 * @date: 2024/1/4 16:58
 */
@Configuration
@MapperScan(basePackages = "com.**.mapper.master", sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {

    static final String MAPPER_LOCATION = "classpath*:mybatis/mapper/master/*.xml";

    @Value("${master.datasource.url}")
    private String url;

    @Value("${master.datasource.username}")
    private String username;

    @Value("${master.datasource.password}")
    private String password;

    @Value("${master.datasource.driver-class-name}")
    private String driverClassName;

    @Resource
    private LogFilter logFilter;


    @Bean("masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource masterDataSource() {

        DruidDataSource dataSource = new DruidDataSource();
        String connectionInitSqls = "SET NAMES utf8mb4";
        /**
         * StringTokenizer类：根据自定义字符为分界符进行拆分
         */
        StringTokenizer tokenizer = new StringTokenizer(connectionInitSqls, ";");
        //设置参数
        dataSource.setConnectionInitSqls(Collections.list(tokenizer));  //设置编码格式
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);

        //监听
        List<Filter> filters = new ArrayList<>();
        filters.add(logFilter);
        dataSource.setProxyFilters(filters);  //设置自定义Filter  //这里要设置一个filter的配置类 不加入bean的话会读取报错

        return dataSource;
    }

    /**
     * 注解@Primary表示的是主数据源头
     * 通过将 @Qualifier 注解与我们想要使用的特定 Spring bean 的名称一起进行装配
     */
    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }

    /**
     * mybatis-plus将不会自动帮我们注入SqlSessionFactory，而使用我们自己定义的SqlSessionFactory。
     *
     * @param masterDataSource
     * @return
     */
    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource) throws Exception {

        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(masterDataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        factoryBean.setConfiguration(configuration);
        //指定xml路径
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MasterDataSourceConfig.MAPPER_LOCATION));
        factoryBean.setPlugins(new PaginationInterceptor(), new OptimisticLockerInterceptor());

        return factoryBean.getObject();
    }

    public static void main(String[] args) {
        Integer a = 1,b =1;
        int c = 1,d = 1;
        System.out.println(a == 1);
        System.out.println(c == d);
    }

}
