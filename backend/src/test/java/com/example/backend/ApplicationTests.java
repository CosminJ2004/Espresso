package com.example.backend;

import com.example.backend.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestConfig.class)
class ApplicationTests {
	@Test
	void contextLoads() {
	}
}
