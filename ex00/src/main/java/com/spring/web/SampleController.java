package com.spring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.spring.web.SampleController;

/**
 * @Controller 어노테이션은 이 클래스를 컨트롤러로 설정하는 어노테이션
 * @RequestMapping을 이용해서 특정한 URI 경로에 해당하면 메소드를 실행
 * @author SeokRae
 *
 */
@Controller
public class SampleController {

	private static final Logger logger = LoggerFactory.getLogger(SampleController.class);
	
	@RequestMapping("doA")
	public void doA() {
		logger.info("doA called.....");
	}

	@RequestMapping("doB")
	public void doB() {
		logger.info("doB called.....");
	}
	
}
