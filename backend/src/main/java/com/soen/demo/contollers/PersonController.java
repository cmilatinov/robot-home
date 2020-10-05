package com.soen.demo.contollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soen.demo.models.Person;
import com.soen.demo.repositories.PersonRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PersonController {

	@Autowired
	PersonRepository personRepository;
	
	@GetMapping("/persons")
	public List<Person> getAllPersons() {
		return personRepository.findAll();
	}
	
	
	
}
