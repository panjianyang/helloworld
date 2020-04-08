package com.itheima.datasource;/*
 * @description: $param$
 * @date: $date$ $time$
 * @auther: pjy
 */

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
// 配置mybatis的接口类放的地方
@MapperScan(basePackages = "com.itheima.mapper.sqlserver", sqlSessionTemplateRef = "sqlserverSqlSessionTemplate")
public class SqlserverDataSourceConfig {
    // 将这个对象放入Spring容器中
    @Bean(name = "sqlserverDataSource")
    // 表示这个数据源是默认数据源

    // 读取application.properties中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    public DataSource getSqlserverDateSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "sqlserverSqlSessionFactory")
    // 表示这个数据源是默认数据源

    // @Qualifier表示查找Spring容器中名字为test1DataSource的对象
    public SqlSessionFactory sqlserverSqlSessionFactory(@Qualifier("sqlserverDataSource") DataSource datasource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/sqlserver/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "sqlserverTransactionManager")
    @Primary
    public DataSourceTransactionManager master2TransactionManager(@Qualifier("sqlserverDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("sqlserverSqlSessionTemplate")
    // 表示这个数据源是默认数据源

    public SqlSessionTemplate mysqlsqlsessiontemplate(
            @Qualifier("sqlserverSqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }
}
