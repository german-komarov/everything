package com.everything.everything.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id","username","email","firstName","lastName"})
public class Person  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private String username;
    private String firstName;
    private String lastName;
    private String email;

    private String status;



    @Lob
    private String description;


    private String password;

    @Transient
    private String confirmPassword;


    @Lob
    private String avatar;


    private String activationCode;
    private int isActivated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_subscriptions",
            joinColumns = {@JoinColumn(name="channel_id")},
            inverseJoinColumns = {@JoinColumn(name="subscriber_id")}
    )
    private Set<Person> subscribers=new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_subscriptions",
            joinColumns = {@JoinColumn(name="subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name="channel_id")}
    )
    private Set<Person> subscriptions=new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_contacts",
            joinColumns = {@JoinColumn(name="first_id")},
            inverseJoinColumns = {@JoinColumn(name="second_id")}
    )
    private Set<Person> contacts=new HashSet<>();



    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="outgoing_id")
    private Set<Contact> from =new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="incoming_id")
    private Set<Contact> to =new HashSet<>();


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }





}
