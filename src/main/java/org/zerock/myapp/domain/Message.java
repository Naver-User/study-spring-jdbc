package org.zerock.myapp.domain;

import lombok.Data;


@Data
//@Value
public class Message {	// Message 테이블에 대한 읽기전용 클래스
	private Integer id;
	private String text;
	

} // end class

