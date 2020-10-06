package com.soen.demo.contollers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.soen.demo.dto.PersonDTO;
import com.soen.demo.models.Person;
import com.soen.demo.repositories.PersonRepository;

@RestController
@RequestMapping("/api")
public class PersonController {

	@Autowired
	PersonRepository personRepository;

//	**** single person actions *********
	
	@GetMapping("/person/{id}")
	public Optional<Person> getPerson(@PathVariable("id") int id) {
		return personRepository.findById(id);
	}
	
	@PostMapping("/person")
	public void addPerson(@RequestBody PersonDTO dto) {
		Person person = new Person(dto.getName(), dto.getRoomId());
		personRepository.save(person);
	}
	
	@DeleteMapping("/person/{id}")
	public void deletePerson(@PathVariable("id") int id) {
		personRepository.deleteById(id);
	}
	
	@PutMapping("/person/{id}")
	public void updatePerson(@RequestBody PersonDTO dto, @PathVariable("id") int id){
		Optional<Person> p = personRepository.findById(id);
		if (p.isPresent()) {
			p.get().setName(dto.getName());
			p.get().setRoomId(dto.getRoomId());
			personRepository.save(p.get());
		}
		else {
			personRepository.save(new Person(dto.getName(), dto.getRoomId()));
		}
		
	}

//	**** many person actions *********
	
	@GetMapping("/persons")
	public List<Person> getAllPerson() {
		return personRepository.findAll();
	}
	
	@DeleteMapping("/persons")
	public void deleteAllPerson() {
		personRepository.deleteAll();
	}
	
}
