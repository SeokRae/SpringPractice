package org.zerock.controller;

import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author "SeokRae"
 * @Description 테스트 코드를 실행 시 스프링이 로딩되도록 하는 부분
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class DataSourceTest {
	
	@Inject
	private DataSource ds;
	
	@Test
	public void testConnection() throws Exception {
		try(Connection con = ds.getConnection()) {
			System.out.println(con);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
