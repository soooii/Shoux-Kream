package com.shoux_kream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

//어플 전체 실행 테스트 X
//@SpringBootTest(classes = ShouxKreamApplicationTests.class)
@TestPropertySource(properties = {
		"S3_SHOUXKREAM_ACCESS_KEY=test",
		"S3_SHOUXKREAM_SECRET_KEY=test",
		"S3_SHOUXKREAM_BUCKET=test",
		"JWT_SECRET_KEY=ZB67NHMI84Y20zQVFSf8nZfChJoG6rZ+16x8HgzWuw8="
})
@Import(MockS3Config.class)
@ActiveProfiles("test")
class ShouxKreamApplicationTests {

	@Test
	void contextLoads() {
	}

}
