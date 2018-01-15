package com.spring.web;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

public class MySQLConnection {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String AWS = "mysqlinstance.c4otsabdm6jt.ap-northeast-2.rds.amazonaws.com";
	private static final String URL = "jdbc:mysql://" + AWS + ":3306/snsdb?useSSL=false";
	private static final String USER = "sdba";
	private static final String PW = "1234qwer";
	@Test
	public void testConnection() throws Exception {
		Class.forName(DRIVER);
		try (Connection con = DriverManager.getConnection(URL, USER, PW)) {
			System.out.println(con);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
