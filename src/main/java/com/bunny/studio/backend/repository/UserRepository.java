package com.bunny.studio.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bunny.studio.backend.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{}