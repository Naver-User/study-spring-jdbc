package org.zerock.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.myapp.domain.Message;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


//@Log4j2
@Slf4j

@NoArgsConstructor

@RequestMapping("/messages/")			// Base URI
@RestController
public class MessageController {	// POJO
	@Autowired private JdbcTemplate jdbcTemplate;
	
	// Spring JDBC 의 핵심인 JdbcTemplate 빈의 사용법
//	this.jdbcTemplate.query(sql, rowMapper);	// DQL
//	this.jdbcTemplate.update(sql);				// DML
	
	
	@GetMapping("/findAllMessages")
	List<Message> findAllMessages() {
		log.trace("findAllMessages() invoked.");
		
		// MESSAGE 테이블 전체를 조회해서, List 컬렉션 생성/반환 With Spring JDBC
		final String sql = "SELECT * FROM message ORDER BY id DESC";
		
		// <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException;
		// T mapRow(ResultSet rs, int rowNum) throws SQLException;
		
		RowMapper<Message> rowMapper = (rs, i) -> {
			log.info("\t+ RowMapper::mapRow({}, {}) invoked.", rs, i);
			
			Integer id = rs.getInt("id");
			String text = rs.getString("text");
				
//			return new Message(id, text);
			Message m = new Message();
			m.setId(id);
			m.setText(text);
			
			return m;
		};
		
		List<Message> list = this.jdbcTemplate.<Message>query(sql, rowMapper);	// .query		
		
		return list;	// List<Message> -> JSON 으로 변환되어 응답메시지 바디에 담겨서 나감
	} // findAllMessages
	
	
	// MESSAGE 테이블에 입력할 새로운 데이터를, 클라이언트에서
	// HTTP request 의 body에 JSON 형식으로 보내오면, 이를 받아다가
	// Spring JDBC를 이용해서, MESSAGE 테이블에 입력하는 핸들러
	@PostMapping("/insertMessage")
	Boolean insertMessage(
		// 아래 어노테이션은 HTTP request 메시지의 바디의 컨텐츠를 추출해서
		// 매개변수에 넣어라!!! 는 역할을 수행하는 어노테이션입니다.
		// 단, 아래의 어노테이션은 JSON일 때에만 적용가능한 어노테이션입니다!!! (***)
		// 즉, 요청메시지의 바디에 있는 XML 또는 JSON 같은 다른 형식의 데이터를
		// 매개변수로 지정된 타입의 자바객체로 "역변환(De-serialize)"해서 넣어주는
		// 기능을 수행합니다. 때문에, 요청메시지의 바디에 JSON (or XML)로 전송되어야 합니다.
		@RequestBody
		Message message
	) {
		log.trace("insertMessage({}) invoked.", message);
		
		// 역변환되어 수신된 메시지 객체를, Spring JDBC의 JdbcTemplate 빈을
		// 이용해서, DML문으로 테이블에 저장
		String sql = "INSERT INTO message (text) VALUES(?)";
		
		// 바인딩변수(?)에 지정할 값은, 두번째 가변인자인 매개변수로 지정해주시면 됩니다.
		int affectedRows = this.jdbcTemplate.update(sql, message.getText());
		
		return (affectedRows == 1);	// true: 성공, false: 실패
	} // insertMessage
	

} // end class
