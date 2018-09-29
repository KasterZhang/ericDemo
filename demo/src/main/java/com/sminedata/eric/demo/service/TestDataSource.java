package com.sminedata.eric.demo.service;

import java.sql.Statement;

import com.sminedata.eric.demo.service.SmineDataSource;
import com.sminedata.eric.demo.service.SmineDESUtil;
import com.zaxxer.hikari.HikariConfig;

import java.sql.Connection;
import java.sql.ResultSet;

public class TestDataSource {
    private static SmineDataSource ds;
    public static void main(String[] args) throws Exception {
        String filePath="src\\main\\resources\\hikariCpPool.properties";
        String passwd = "zksj2018";

        HikariConfig config = SmineDESUtil.decodeProperties(filePath, passwd);
        // HikariDataSource ds = new HikariDataSource(config);
        System.out.println(config.getDataSourceProperties().toString());
        ds = new SmineDataSource();
        ds.setConfig(config);
        ds.init(10, 50);
        // System.out.println(ds.reader_PW);
        Connection conn1 = ds.getConnection();
        Connection conn2 = ds.getConnection();
        Statement stmt1 = conn1.createStatement();
        ResultSet rs1 = stmt1.executeQuery("select * from processing_code limit 0,1");
        rs1.next();
        System.out.println(rs1.getLong(1)); //4489

        Statement stmt2 = conn2.createStatement();
        ResultSet rs2 = stmt2.executeQuery("SELECT CONNECTION_ID()");
        rs2.next();
        System.out.println(rs2.getLong(1)); //4489 (reused same connection)
        //close connection
        conn1.close();
        conn2.close();
    }
}