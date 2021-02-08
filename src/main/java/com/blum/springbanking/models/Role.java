package com.blum.springbanking.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "RoleEntity")
@Table(name = "roles")
public class Role implements Serializable
{
    private long role_id;
    private String name;
    private Set<Authority> authorities = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    public long getId()
    {
        return this.role_id;
    }
    public void setId(long role_id)
    {
        this.role_id = role_id;
    }

    @Column(name = "role_name")
    public String getName()
    { return this.name; }
    public void setName(String role_name)
    {
        this.name = role_name;
    }

    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable
            (
                    name = "roles_authorities",
                    joinColumns = @JoinColumn(name="role_id"),
                    inverseJoinColumns = @JoinColumn(name="authority_id")
            )
    public Set<Authority> getAuthorities()
    { return authorities; }
    public void setAuthorities(Set<Authority> authorities)
    { this.authorities = authorities; }
}
