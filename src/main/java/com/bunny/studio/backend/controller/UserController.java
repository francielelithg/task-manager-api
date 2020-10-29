package com.bunny.studio.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bunny.studio.backend.model.User;
import com.bunny.studio.backend.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
@Api(value = "Users")
@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 500, message = "Server was unable to process the request")
})
public class UserController {
	
	@Autowired
	private UserRepository repo;
	
	@GetMapping
	@ApiOperation(value = "Retrieve all users", response = Iterable.class)
	public Iterable<User> findAll(){
	   return repo.findAll();
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(path = {"/{id}"})
	@ApiOperation(value = "Get user by id", response = ResponseEntity.class)
	public ResponseEntity findById(@ApiParam(value = "Id of the user", required = true) @PathVariable long id){
	   return repo.findById(id)
			   .map(record -> ResponseEntity.ok().body(record))
	           .orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@ApiOperation(value = "Create a new user", response = User.class)
	public User create(@ApiParam(value = "Information of the user to be created", required = true) @RequestBody User usuario){
	   return repo.save(usuario);
	}

	@PutMapping(value="/{id}")
	@ApiOperation(value = "Update an existing user", response = ResponseEntity.class)
	public ResponseEntity<User> update(@ApiParam(value = "Id of the user", required = true) @PathVariable("id") long id, @ApiParam(value = "Information of the user to be updated", required = true) @RequestBody User usuario){
		return repo.findById(id)
		           .map(record -> {
		               record.setName(usuario.getName());
		               User updated = repo.save(record);
		               return ResponseEntity.ok().body(updated);
		           }).orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(path ={"/{id}"})
	@ApiOperation(value = "Delete a user", response = ResponseEntity.class)
	public ResponseEntity<?> delete(@ApiParam(value = "Id of the user to be deleted", required = true) @PathVariable long id) {
	   return repo.findById(id)
	           .map(record -> {
	               repo.deleteById(id);
	               return ResponseEntity.ok().build();
	           }).orElse(ResponseEntity.notFound().build());
	}
	
}