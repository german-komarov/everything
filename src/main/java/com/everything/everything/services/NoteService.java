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
import java.util.Optional;
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





    public void saveNote(Note note)
    {
        noteRepository.save(note);
    }

    public void deleteNoteById(Long id)
    {
        noteRepository.deleteById(id);
    }

    public List<Note> getAllNotes()
    {
        return noteRepository.findAll();
    }


    public Note getNoteById(Long id)
    {
        Optional<Note> note=noteRepository.findById(id);
        return note.orElse(null);
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



    public List<Note> getAllNotesFromChannels (Long id)
    {
        Person person=personService.getUserById(id);
        Set<Person> people=person.getSubscriptions();
        if(people.isEmpty())
        {
            return new ArrayList<Note>();
        }
        List<String> usernames=new ArrayList<>();
        for(Person p:people)
        {
            usernames.add(p.getUsername());
        }
        List<Note> notes=entityManager.createQuery("select note from Note note where note.author in :paramList order by note.creationDate desc ",Note.class)
                .setParameter("paramList",usernames).getResultList();

        return notes;
    }

    public List<Note> getAllNotesOfThisUser(Long id)
    {
        Person person=personService.getUserById(id);
        return entityManager.createQuery("select note from Note note where note.author like :paramUsername order by note.creationDate desc ",Note.class)
                .setParameter("paramUsername",person.getUsername())
                .getResultList();
    }


    public boolean likeNote(Long noteId,Long personId)
    {
        Note note=this.getNoteById(noteId);
        Person person=personService.getUserById(personId);
        Set<Person> likes=note.getLikes();
        if(likes.contains(person))
        {
            return false;
        }
        likes.add(person);
        note.setLikes(likes);
        this.saveNote(note);
        return true;

    }


    public boolean unlikeNote(Long noteId,Long personId)
    {
        Note note=this.getNoteById(noteId);
        Person person=personService.getUserById(personId);
        Set<Person> likes=note.getLikes();
        if(!likes.contains(person))
        {
            return false;
        }
        likes.remove(person);
        note.setLikes(likes);
        this.saveNote(note);
        return true;

    }
}
