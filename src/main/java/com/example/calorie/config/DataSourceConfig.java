//package com.example.calorie.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DataSourceConfig {
//
//	@Bean
//	@ConfigurationProperties("spring.datasource")
//	public DataSourceProperties dataSourceProperties() {
//		return new DataSourceProperties();
//	}
//
//	@Bean
//	public DataSource dataSource(DataSourceProperties props) {
//		return props.initializeDataSourceBuilder().build();
//	}
//
//}
