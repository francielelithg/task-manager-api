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

import com.bunny.studio.backend.model.UserTasks;
import com.bunny.studio.backend.repository.UserTasksRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/userTasks")
@Api(value = "Tasks")
@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 404, message = "Task not found"),
        @ApiResponse(code = 500, message = "Server was unable to process the request")
})
public class UserTasksController {
	
	@Autowired
	private UserTasksRepository repo;
	
	@GetMapping
	@ApiOperation(value = "Retrieve all tasks", response = Iterable.class)
	public Iterable<UserTasks> findAll(){
	   return repo.findAll();
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(path = {"/{id}"})
	@ApiOperation(value = "Get task by id", response = ResponseEntity.class)
	public ResponseEntity findById(@ApiParam(value = "Id of the task", required = true) @PathVariable long id){
	   return repo.findById(id)
			   .map(record -> ResponseEntity.ok().body(record))
	           .orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@ApiOperation(value = "Create a new task", response = UserTasks.class)
	public UserTasks create(@ApiParam(value = "Information of the task to be created", required = true) @RequestBody UserTasks task){
	   return repo.save(task);
	}
	
	@PutMapping(value="/{id}")
	@ApiOperation(value = "Update an existing task", response = ResponseEntity.class)
	public ResponseEntity<UserTasks> update(@ApiParam(value = "Id of the task", required = true) @PathVariable("id") long id, @ApiParam(value = "Information of the task to be updated", required = true) @RequestBody UserTasks task){
		return repo.findById(id)
		           .map(record -> {
		               record.setDescription(task.getDescription());
		               record.setState(task.getState());
		               UserTasks updated = repo.save(record);
		               return ResponseEntity.ok().body(updated);
		           }).orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(path ={"/{id}"})
	@ApiOperation(value = "Delete a task", response = ResponseEntity.class)
	public ResponseEntity<?> delete(@ApiParam(value = "Id of the task to be deleted", required = true) @PathVariable long id) {
	   return repo.findById(id)
	           .map(record -> {
	               repo.deleteById(id);
	               return ResponseEntity.ok().build();
	           }).orElse(ResponseEntity.notFound().build());
	}

}