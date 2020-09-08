package com.sdet.auto.springboot2api;

import com.sdet.auto.springboot2api.controller.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBoot2ApiApplicationTests {

	@Autowired
	private HelloController controller;

	@Test
	public void contexLoads() throws Exception {
		assertNotNull(controller);
	}

}
