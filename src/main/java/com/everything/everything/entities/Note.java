package com.everything.everything.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    @Lob
    private String attachment;
    private String attachmentType;

    @Lob
    private String content;

    private Date creationDate;


    @PrePersist
    private void onCreate()
    {
        creationDate=new Date();
    }






}
