package com.blum.springbanking.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "AuthorityEntity")
@Table(name = "authorities")
public class Authority implements Serializable
{
    private long authority_id;
    private String authority_name;
    private Set<Role> roles = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    public long getId()
    {
        return this.authority_id;
    }
    public void setId(long authority_id)
    {
        this.authority_id = authority_id;
    }

    @Column(name = "authority_name")
    public String getName()
    {
        return this.authority_name;
    }
    public void setName(String authority_name)
    {
        this.authority_name = authority_name;
    }

    @ManyToMany(mappedBy = "authorities", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles;}

}