package com.everything.everything.repositories;

import com.everything.everything.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Long> {
    Person findByUsername(String username);
}
