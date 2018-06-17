package org.zerock.controller;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

/**
 * @Test
 * @author "SeokRae"
 *
 */
public class MySQLConnectionTest {

	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

	private static final String URL = "jdbc:mysql://127.0.0.1:3306/springDB?useSSL=false&serverTimezone=Asia/Seoul";

	private static final String USER = "springDBA";

	private static final String PW = "1234";

	@Test
	public void testConnection() throws Exception {
		// 드라이버
		Class.forName(DRIVER);

		try (Connection con = DriverManager.getConnection(URL, USER, PW)) {
			System.out.println(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
