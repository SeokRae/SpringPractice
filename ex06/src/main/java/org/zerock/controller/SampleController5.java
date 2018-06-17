package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zerock.domain.ProductVO;

/**
 * 
 * @author "SeokRae"
 *
 */
@Controller
public class SampleController5 {
	
	/**
	 * JSON 객체데이터생성 및 반환
	 * @return
	 */
	@RequestMapping("/doJSON")
	public @ResponseBody ProductVO doJSON() {
		ProductVO vo = new ProductVO("샘플상품", 30000);
		return vo;
	}
}
