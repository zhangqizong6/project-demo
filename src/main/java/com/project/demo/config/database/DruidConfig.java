package com.project.demo.config.database;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: DruidConfig
 * @author: zqz
 * @date: 2024/1/5 14:13
 */
@Configuration
public class DruidConfig {

    @Bean(name = "slf4jLogFilter")
    public Slf4jLogFilter logFilter() {
        Slf4jLogFilter logFilter = new Slf4jLogFilter();
        logFilter.setResultSetLogEnabled(false);
        logFilter.setConnectionLogEnabled(false);
        logFilter.setStatementLogEnabled(true);
        logFilter.setStatementExecutableSqlLogEnable(true);
        return logFilter;
    }
}
