package org.zerock.myapp;

import java.util.Objects;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertNotNull;


//@Log4j2
@Slf4j

@NoArgsConstructor

@AutoConfigureMockMvc
@SpringBootTest
class StudySpringJdbcAppTests {
	@Autowired(required = false)
	private DataSource dataSource;
	
	@Autowired(required = false)
	private JdbcTemplate jdbcTemplate;

	
	@Test
	void contextLoads() {
		log.trace("contextLoads() invoked.");
		
		Objects.requireNonNull(this.dataSource);
		log.info("\t+ this.dataSource: {}", this.dataSource);
		
		assertNotNull(this.jdbcTemplate);
		log.info("\t+ this.jdbcTemplate: {}", this.jdbcTemplate);
	} // contextLoads

} // end class


