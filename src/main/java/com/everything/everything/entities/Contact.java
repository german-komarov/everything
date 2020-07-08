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
public class Contact {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="outgoing_id")
    private Person from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="incoming_id")
    private Person to;

    private Date time;

    private int isAccepted;

    @PrePersist
    private void onCreate()
    {
        time=new Date();
    }


}
