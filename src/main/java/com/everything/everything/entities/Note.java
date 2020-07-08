package com.everything.everything.entities;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id","author"})
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;
    private Long authorId;

    @Lob
    private String attachment;
    private String attachmentType;

    @Lob
    private String content;

    private Date creationDate;

    @Transient
    private boolean likedByThisUser;



    @PrePersist
    private void onCreate()
    {
        creationDate=new Date();
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="note_likes",
            joinColumns = {@JoinColumn(name="note_id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")}
            )
    private Set<Person> likes=new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "note_id")
    private Set<Comment> comments=new HashSet<>();











}
