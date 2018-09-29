package com.sminedata.eric.demo.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


/**
 * Copyright 2018-2018 Beijing SmartMining Data & Tech Co.,Ltd.
 *
 * @creator zijian.zhang
 * @createTime 2018/9/13 10:37
 * @description
 */
public class SmineDataSource {

    private HikariConfig config;
    private HikariDataSource ds;

    public HikariConfig getConfig() {
        return config;
    }

    public void setConfig(HikariConfig config) {
        this.config = config;
    }

    public void init(int Minimum,int Maximum) throws Exception {
        
        // SecretKey key = SmineDESUtil.generateKey("zksj2018");
        // GetProperties prop =  new GetProperties();
        // prop.setConfigUrl("G:/workspace/IntelliJ/ericSmine/src/main/resources/config/hikari-mariadb-target.properties");
        // config.setPassword(SmineDESUtil.toString(SmineDESUtil.decrypt(prop.getValue("dataSource.password"), key.getEncoded())));
        // config.setUsername(SmineDESUtil.toString(SmineDESUtil.decrypt(prop.getValue("dataSource.user"), key.getEncoded())));
        // config.setDataSourceClassName(SmineDESUtil.toString(SmineDESUtil.decrypt(prop.getValue("dataSourceClassName"), key.getEncoded())));
        // config.addDataSourceProperty("portNumber",SmineDESUtil.toString(SmineDESUtil.decrypt(prop.getValue("dataSource.portNumber"), key.getEncoded())));
        // config.addDataSourceProperty("serverName",SmineDESUtil.toString(SmineDESUtil.decrypt(prop.getValue("dataSource.serverName"), key.getEncoded())));
        // config.addDataSourceProperty("databaseName",SmineDESUtil.toString(SmineDESUtil.decrypt(prop.getValue("dataSource.databaseName"), key.getEncoded())));

        // System.out.println(config.getDataSourceProperties());
        // HikariConfig config = new HikariConfig(configUrl);
        // config.setUsername(reader_acc);
        // config.setPassword(reader_PW);

        //If your driver supports JDBC4 we strongly recommend not setting this property
        // config.setConnectionTestQuery("SELECT 1");
        config.setAutoCommit(true);
        //pool lazy conn count
        config.setMinimumIdle(Minimum);
        //pool max count
        config.setMaximumPoolSize(Maximum);
        ds = new HikariDataSource(config);
    }

    /**
     * destroy pool
     */
    public void close(){
        ds.close();
    }

    /**
     * get connection
     * @return
     */
    public Connection getConnection(){
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
//            ds.resumePool();
            return null;
        }
    }

}
