package com.soen.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soen.demo.models.Person;

public interface PersonRepository extends JpaRepository<Person, Integer>{

	List<Person> findByName(String name);
	List<Person> findByRoomId(int roomId);
	
}
