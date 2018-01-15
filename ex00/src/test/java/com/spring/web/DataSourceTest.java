package com.spring.web;

import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 테스트 코드 실행 시 스프링이 로딩되도록 하는 부분
 * @ContextConfiguration의 locations 속성 경로에 xml 파일을 이용해서 스프링 로딩
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class DataSourceTest {
	@Inject
	private DataSource ds;
	@Test
	public void testConnection() {
		try(Connection con = ds.getConnection()){
			System.out.println(con);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
