package com.bunny.studio.backend;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bunny.studio.backend.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.junit.Assert;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserTests {  

	@LocalServerPort
    int randomServerPort;
	
	@Test
	public void testGetAllUsers() {
		RestTemplate restTemplate = new RestTemplate();
		@SuppressWarnings("rawtypes")
		ResponseEntity<List> response = restTemplate.exchange(String.format("http://localhost:%s/user", randomServerPort), HttpMethod.GET, null, List.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
	}
	
	@Test
	public void testCreateUpdateDeleteUser() throws JsonProcessingException {
		String url = String.format("http://localhost:%s/user", randomServerPort);
		
		//POST
		User mock = mockUser();
		String json = new ObjectMapper().writeValueAsString(mock);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		
		HttpEntity<?> entity = new HttpEntity<>(json, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.POST, entity, User.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
		Assert.assertNotNull(response.getBody());
		
		Long id = response.getBody().getId();
		
		//GET/{id}
		response = restTemplate.exchange(String.format("%s/%s", url, id), HttpMethod.GET, null, User.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(id, response.getBody().getId());
		
		//PUT
		response = restTemplate.exchange(String.format("%s/%s", url, id), HttpMethod.PUT, entity, User.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(mock.getName(), response.getBody().getName());
		
		//DELETE
		response = restTemplate.exchange(String.format("%s/%s", url, id), HttpMethod.DELETE, null, User.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
	}
	
	private User mockUser() {
		return User.builder().name("Franciele").build();
	}
	
}