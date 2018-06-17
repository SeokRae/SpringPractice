package org.zerock.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SampleController4 {
	private static final Logger logger = LoggerFactory.getLogger(SampleController4.class);
	
	/**
	 * 리다이렉트 시 사용 객체
	 * @param rttr
	 * @return
	 */
	@RequestMapping("/doE")
	public String doE(RedirectAttributes rttr) {
		logger.info("doE called but redirect to /doF");
		
		rttr.addFlashAttribute("msg", "This is the Message!! with redirected");
		return "redirect:/doF";
	}
	
	/**
	 * 
	 * @param msg
	 */
	@RequestMapping("/doF")
	public void doF(@ModelAttribute String msg) {
		logger.info("doF called ....");
	}
	
}
