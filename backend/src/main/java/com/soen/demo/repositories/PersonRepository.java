package com.soen.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soen.demo.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{

}
