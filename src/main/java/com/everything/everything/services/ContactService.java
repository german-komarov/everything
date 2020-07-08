package com.everything.everything.services;


import com.everything.everything.entities.Contact;
import com.everything.everything.entities.Person;
import com.everything.everything.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public void saveContact(Contact contact)
    {
        contactRepository.save(contact);
    }

    public void deleteContactById(Long id)
    {
        contactRepository.deleteById(id);
    }

    public Contact getContactByFromAndTo(Person from,Person to)
    {
        Optional <Contact> contact=contactRepository.findContactByFromAndTo(from,to);
        return contact.orElse(null);
    }

    public Contact getContactById(Long id)
    {
        Optional<Contact> contact=contactRepository.findById(id);
        return contact.orElse(null);
    }






}
