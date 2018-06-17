package org.zerock.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author "SeokRae"
 *
 */
@Controller
public class SampleController2 {
	private static final Logger logger = LoggerFactory.getLogger(SampleController2.class);
	
	/**
	 * 요청 시 msg 이름의 파라미터를 문자열로 처리, 뷰에 전달
	 * @param msg
	 * @return
	 */
	@RequestMapping
	public String doC(@ModelAttribute("msg") String msg) {
		logger.info("doC called ....");
		// 'result'는 결과적으로 /WEB-INF/views/result.jsp 파일 찾아서 실행
		return "result";
	}
}
