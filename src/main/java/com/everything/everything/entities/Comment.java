package com.everything.everything.entities;

import lombok.*;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
@ToString(of={"id","author","text"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="note_id")
    private Note note;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="person_id")
    private Person author;

    private Date publishDate;

    @PrePersist
    private void onCreate()
    {
        publishDate=new Date();
    }
}
