package org.zerock.test;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.persistence.BoardDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class BoardDAOTest {

	@Inject
	private BoardDAO dao;

	private static Logger logger = LoggerFactory.getLogger(BoardDAOTest.class);

//	@Test
//	public void testCreate() throws Exception {
//
//		BoardVO board = new BoardVO();
//		board.setTitle("새글을 작성");
//		board.setContent("새 글의 내용");
//		board.setWriter("user00");
//		dao.create(board);
//	}
//
//	@Test
//	public void testRead() throws Exception {
//		logger.info(dao.read(1).toString());
//	}
//
//	@Test
//	public void testUpdate() throws Exception {
//		BoardVO board = new BoardVO();
//		board.setBno(1);
//		board.setTitle("modify title");
//		board.setContent("modify content");
//		dao.update(board);
//	}
//
//	@Test
//	public void testDelete() throws Exception {
//		dao.delete(1);
//	}
//
//	@Test
//	public void testListPage() throws Exception {
//		int page = 3;
//		List<BoardVO> list = dao.listPage(page);
//		for (BoardVO boardVO : list) {
//			logger.info(boardVO.getBno() + ":" + boardVO.getTitle());
//		}
//	}

//	@Test
//	public void testListCriteria() throws Exception {
//		Criteria criteria = new Criteria();
//		criteria.setPage(2);
//		criteria.setPerPageNum(20);
//
//		List<BoardVO> list = dao.listCriteria(criteria);
//		for (BoardVO boardVO : list) {
//			logger.info(boardVO.getBno() + ":" + boardVO.getTitle());
//		}
//	}
	
//	@Test
//	public void testURI() throws Exception {
//		UriComponents uriComponents = UriComponentsBuilder.newInstance()
//				.path("/board/read")
//				.queryParam("bno", 12)
//				.queryParam("perPageNum", 20)
//				.build();
//		logger.info("/board/read?bno=12&perPageNum=20");
//		logger.info(uriComponents.toString());
//	}
//	
	
	@Test
	public void testURI2() throws Exception {
		UriComponents uriComponents = 
				UriComponentsBuilder.newInstance()
				.path("/{module}/{page}")
				.queryParam("bno", 12)
				.queryParam("perPageNum", 20)
				.build()
				.expand("board", "read")
				.encode();
		logger.info("/board/read?bno=12&perPageNum=20");
		logger.info(uriComponents.toString());
	}
}
