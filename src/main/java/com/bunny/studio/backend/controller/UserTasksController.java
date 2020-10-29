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

@RestController
@RequestMapping("/userTasks")
public class UserTasksController {
	
	@Autowired
	private UserTasksRepository repo;
	
	@GetMapping
	public Iterable<UserTasks> findAll(){
	   return repo.findAll();
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(path = {"/{id}"})
	public ResponseEntity findById(@PathVariable long id){
	   return repo.findById(id)
			   .map(record -> ResponseEntity.ok().body(record))
	           .orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public UserTasks create(@RequestBody UserTasks task){
	   return repo.save(task);
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<UserTasks> update(@PathVariable("id") long id, @RequestBody UserTasks task){
		return repo.findById(id)
		           .map(record -> {
		               record.setDescription(task.getDescription());
		               record.setState(task.getState());
		               UserTasks updated = repo.save(record);
		               return ResponseEntity.ok().body(updated);
		           }).orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(path ={"/{id}"})
	public ResponseEntity<?> delete(@PathVariable long id) {
	   return repo.findById(id)
	           .map(record -> {
	               repo.deleteById(id);
	               return ResponseEntity.ok().build();
	           }).orElse(ResponseEntity.notFound().build());
	}

}