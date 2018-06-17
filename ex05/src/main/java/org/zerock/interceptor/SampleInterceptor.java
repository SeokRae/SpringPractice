package org.zerock.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author "SeokRae"
 * 1. HandlerInterceptorAdapter를 상속해서 사용하는 클래스들을 보관하는 'org.zerock.interceptor' 패키지 생성
 * 2. SampleInterceptor를 스프링에서 인식 시키기 위해서 servlet-context.xml에 설정을 추가
 * 3. Controller에 간단한 메소드를 작성
 */
public class SampleInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("post handle.....");
		
		Object result = modelAndView.getModel().get("result");
		
		if(result != null) {
			request.getSession().setAttribute("result", result);
			response.sendRedirect("/doA");
		}
	}
	/**
	 * preHandler()의 경우 리턴타입이 boolean으로 설계
	 * 이를 이용하여 다음 Interceptor나 대상 컨트롤러를 호출하도록 할 것인지를 결정
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("pre handle.....");
		
		HandlerMethod method = (HandlerMethod) handler;
		Method methodObj = method.getMethod();
		
		System.out.println("Bean : " + method.getBean());
		System.out.println("Method : " + methodObj);
		
		return true;
	}
	
}
