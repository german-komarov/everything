package com.everything.everything.services;


import com.everything.everything.entities.Note;
import com.everything.everything.entities.Person;
import com.everything.everything.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class NoteService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PersonService personService;

    @Autowired
    private NoteRepository noteRepository;


    public List<Note> getAllNotes()
    {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id)
    {
        if(noteRepository.findById(id).isPresent())
        {
            return noteRepository.findById(id).get();
        }

        else
        {
            return null;
        }
    }


    public Note getNoteByAuthor(String author)
    {
        List<Note> notes=entityManager.createQuery("select note from Note note where note.author like :paramAuthor",Note.class)
                .setParameter("paramAuthor",author).getResultList();

        if(!notes.isEmpty())
        {
            return notes.get(0);
        }
        else
        {
            return null;
        }
    }



    public Page<Note> getAllNotesFromChannels (Long id, Pageable pageable)
    {
        Person person=personService.getUserById(id);
        Set<Person> people=person.getSubscriptions();
        if(people.isEmpty())
        {
            return new PageImpl<Note>(new ArrayList<Note>(),pageable,0);
        }
        List<String> usernames=new ArrayList<>();
        for(Person p:people)
        {
            usernames.add(p.getUsername());
        }
        List<Note> notes=entityManager.createQuery("select note from Note note where note.author in :paramList order by note.creationDate desc ",Note.class)
                .setParameter("paramList",usernames).getResultList();

        Page<Note> page=new PageImpl<Note>(notes,pageable,notes.size());
        return page;
    }

    public List<Note> getAllNotesOfThisUser(Person person)
    {
        return entityManager.createQuery("select note from Note note where note.author like :paramUsername",Note.class)
                .setParameter("paramUsername",person.getUsername())
                .getResultList();
    }


    public void saveNote(Note note)
    {
        noteRepository.save(note);
    }

}
