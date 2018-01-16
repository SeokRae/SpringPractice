package com.spring.persistence;

import com.spring.domain.MemberVO;

public interface MemberDAO {
	
	public String getTime();
	
	public void insertMember(MemberVO vo);
	
	public MemberVO readMember(String userid) throws Exception;
	
	public MemberVO readWithePW(String userid, String userpw) throws Exception;
}
