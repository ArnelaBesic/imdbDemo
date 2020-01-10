package com.demo.imdb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;

@SpringBootTest
class ImdbApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void convertimg() {
		File file = new File("/home/arnela/Projects/imdb/src/main/resources/images/musk.jpg");
		byte[] bFile = new byte[(int) file.length()];
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long i=bFile.length;
	}

}
