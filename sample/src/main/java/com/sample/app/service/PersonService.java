package com.sample.app.service;
import com.sample.app.entity.Person;
import com.sample.app.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService  {
    private final PersonRepository personRepository;
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    public Person createOrUpdate(Person person){
        return personRepository.save(person);
    }

    public Person delete(Long id){
        Optional<Person> person = personRepository.findById(id);
        personRepository.delete(person.get());
        return person.get();
    }

    public Iterable<Person> getAll(){
        return personRepository.findAll();
    }

  }
