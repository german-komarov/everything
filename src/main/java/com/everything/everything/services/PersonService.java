package com.everything.everything.services;

import com.everything.everything.entities.Person;
import com.everything.everything.entities.Role;
import com.everything.everything.repositories.PersonRepository;
import com.everything.everything.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

@Service
@Transactional
public class PersonService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RegistrationMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Person person=personRepository.findByUsername(username);

        if(person==null)
        {
            throw new UsernameNotFoundException("User not found");
        }

        if(person.getIsActivated()!=1)
        {
            throw new UsernameNotFoundException("User not activated");
        }

        return person;
    }


    public Person getUserById(Long id)
    {
        if(personRepository.findById(id).isPresent())
        {
            return personRepository.findById(id).get();
        }

        else {
            return null;
        }
    }


    public Person getUserByUsername(String username)
    {
        List<Person> people=entityManager.createQuery("select person from Person person where " +
                "person.username like :paramUsername",Person.class)
                .setParameter("paramUsername",username)
                .getResultList();

        if(!people.isEmpty())
        {
            return people.get(0);
        }

        else
        {
            return null;
        }
    }


    public Person getUserByEmail(String email)
    {
        List<Person> people=entityManager.createQuery("select person from Person person where " +
                "person.email like :paramEmail",Person.class)
                .setParameter("paramEmail",email)
                .getResultList();

        if(!people.isEmpty())
        {
            return people.get(0);
        }

        else
        {
            return null;
        }
    }

    public Person getUserByActivationCode(String activationCode)
    {
        List<Person> people=entityManager.createQuery("select person from Person person where " +
                "person.activationCode like :paramActivationCode",Person.class)
                .setParameter("paramActivationCode",activationCode)
                .getResultList();

        if(!people.isEmpty())
        {
            return people.get(0);
        }

        else
        {
            return null;
        }
    }



    public void saveUser(Person person)
    {
        personRepository.save(person);
    }



    public boolean deleteUser(long userId) {
        if (personRepository.findById(userId).isPresent()) {
            personRepository.deleteById(userId);
            return true;
        }
        return false;
    }



    public String registerUser(Person person) {

        if(!person.getPassword().equals(person.getConfirmPassword()))
        {
            return "Passwords are not equal";
        }

        if(this.getUserByUsername(person.getUsername())!=null)
        {
            return "Username is taken";
        }

        if(this.getUserByEmail(person.getEmail())!=null)
        {
            return "Email is taken";
        }

        person.setStatus("");
        person.setDescription("");
        person.setIsActivated(0);
        person.setActivationCode(UUID.randomUUID().toString());
        person.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        //Green avatar
        person.setAvatar("iVBORw0KGgoAAAANSUhEUgAAB3oAAAN7CAIAAAAstAwIAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAABuLSURBVHhe7dihAYBAEMCwA8kW7D8fHvMbXGViukOv53sHAAAAAAB27lMAAAAAAFiwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAACNjNAAAAAAAE7GYAAAAAAAJ2MwAAAAAAAbsZAAAAAICA3QwAAAAAQMBuBgAAAAAgYDcDAAAAABCwmwEAAAAAWJv5Ac0bCBbQ/C3OAAAAAElFTkSuQmCC");
        this.saveUser(person);
        String message= String.format("Hello %s.\n\nWelcome to StudentZ. " +
                        "Go to this reference to activate your account https://everything-for-you.herokuapp.com/registration/activate/%s\n\nIf you didn't try to register, please just ignore this message",
                person.getUsername(),person.getActivationCode());
        mailSender.send(person.getEmail(),"Registration",message);

        return "OK";

    }


    public boolean subscribe(Person channel,Person subscriber)
    {
        Set<Person> subscribersOfTheChannel=channel.getSubscribers();
        if(subscribersOfTheChannel.contains(subscriber)||channel.equals(subscriber))
        {
            return false;
        }
        subscribersOfTheChannel.add(subscriber);
        channel.setSubscribers(subscribersOfTheChannel);
        this.saveUser(channel);
        return true;
    }

    public boolean unsubscribe(Person channel,Person subscriber)
    {
        Set<Person> subscribersOfTheChannel=channel.getSubscribers();
        if(!subscribersOfTheChannel.contains(subscriber)||channel.equals(subscriber))
        {
            return false;
        }
        subscribersOfTheChannel.remove(subscriber);
        channel.setSubscribers(subscribersOfTheChannel);
        this.saveUser(channel);
        return true;
    }


    public void addContact(Person firstPerson,Person secondPerson)
    {

        //Get current contacts of two people
        Set<Person> contactsOfFirstPerson=firstPerson.getContacts();
        Set<Person> contactsOfSecondPerson=secondPerson.getContacts();

        //Adding people to each other's contacts
        contactsOfFirstPerson.add(secondPerson);
        contactsOfSecondPerson.add(firstPerson);

        //Setting contacts to corresponding users
        firstPerson.setContacts(contactsOfFirstPerson);
        secondPerson.setContacts(contactsOfSecondPerson);

        //Saving users
        this.saveUser(firstPerson);
        this.saveUser(secondPerson);
    }

    public void deleteContact(Person firstPerson,Person secondPerson)
    {

        //Get current contacts of two people
        Set<Person> contactsOfFirstPerson=firstPerson.getContacts();
        Set<Person> contactsOfSecondPerson=secondPerson.getContacts();

        //Adding people to each other's contacts
        contactsOfFirstPerson.remove(secondPerson);
        contactsOfSecondPerson.remove(firstPerson);

        //Setting contacts to corresponding users
        firstPerson.setContacts(contactsOfFirstPerson);
        secondPerson.setContacts(contactsOfSecondPerson);

        //Saving users
        this.saveUser(firstPerson);
        this.saveUser(secondPerson);
    }



    public boolean sendMailAgain(String mail)
    {
        Person person=this.getUserByEmail(mail);
        if(person.getIsActivated()==0)
        {
            person.setActivationCode(UUID.randomUUID().toString());
            this.saveUser(person);
            String message= String.format("Hello %s.\n\nWelcome to StudentZ. " +
                            "Go to this reference to activate your account https://everything-for-you.herokuapp.com/registration/activate/%s\n\nIf you didn't try to register, please just ignore this message",
                    person.getUsername(),person.getActivationCode());
            mailSender.send(person.getEmail(),"Registration",message);
            return true;
        }
        else
        {
            return false;
        }
    }

}
