package com.culqi.pasarela;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.culqi.pasarela.controller.TokenController;
import com.culqi.pasarela.services.ITokenServices;


import static org.mockito.Mockito.when;

@WebMvcTest(TokenController.class)
class PasarelaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ITokenServices service;

	@Test
	void contextLoads() {
	}

	@Test
	public void executeToken() {
		when(service.generateToken()).thenReturn("DMJz2QjuZLpeaO6J");
	}

}
