package com.everything.everything.repositories;

import com.everything.everything.entities.Contact;
import com.everything.everything.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact,Long> {
    Optional<Contact> findContactByFromAndTo(Person from,Person to);
}
