package com.bunny.studio.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bunny.studio.backend.model.User;
import com.bunny.studio.backend.model.UserTasks;
import com.bunny.studio.backend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserTasksTests {  

	@LocalServerPort
    int randomServerPort;
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	JdbcTemplate template;
	
	private Long userId;
	
	@BeforeEach
	public void preTest() {
		User saved = repo.save(mockUser());
		userId = saved.getId();
	}
	
	@Test
	public void testGetAllTasks() {
		RestTemplate restTemplate = new RestTemplate();
		@SuppressWarnings("rawtypes")
		ResponseEntity<List> response = restTemplate.exchange(String.format("http://localhost:%s/userTask", randomServerPort), HttpMethod.GET, null, List.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
	}
	
	@Test
	public void testCreateUpdateDeleteTask() throws JsonProcessingException, JSONException {
		String url = String.format("http://localhost:%s/userTask", randomServerPort);
		
		//POST
		UserTasks mock = mockUserTask();
		String obj = new ObjectMapper().writeValueAsString(mock);
		
		JSONObject user = new JSONObject();
		user.put("id", userId);
		
		JSONObject json = new JSONObject(obj);
		json.put("user", user);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		
		HttpEntity<?> entity = new HttpEntity<>(json.toString(), headers);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<UserTasks> response = restTemplate.exchange(url, HttpMethod.POST, entity, UserTasks.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
		Assert.assertNotNull(response.getBody());
		
		Long id = response.getBody().getId();
		
		//GET/{id}
		response = restTemplate.exchange(String.format("%s/%s", url, id), HttpMethod.GET, null, UserTasks.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(id, response.getBody().getId());
		
		//PUT
		response = restTemplate.exchange(String.format("%s/%s", url, id), HttpMethod.PUT, entity, UserTasks.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(mock.getDescription(), response.getBody().getDescription());
		
		//DELETE
		response = restTemplate.exchange(String.format("%s/%s", url, id), HttpMethod.DELETE, null, UserTasks.class);
		
		Assert.assertEquals(200, response.getStatusCodeValue());
	}
	
	
	private UserTasks mockUserTask() {
		User user = new User();
		user.setId(userId);
		
		return UserTasks.builder().description("Finish tests").state(true).build();
	}
	
	private User mockUser() {
		return User.builder().name("Franciele").build();
	}
	
}