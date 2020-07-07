package com.everything.everything.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id","from","to"})
@ToString(of = {"id","from","to","time"})
public class ContactRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String from;
    private String to;

    private Date time;

    private int isAccepted;

    @PrePersist
    private void onCreate()
    {
        time=new Date();
    }


}
