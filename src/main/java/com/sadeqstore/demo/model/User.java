package com.sadeqstore.demo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    private String name;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bought_Ps"
                ,joinColumns = {@JoinColumn(name = "userID")})
    private Set<Product> boughtPs=new HashSet<>();

    public Set<Product> getBoughtPs() {
        return boughtPs;
    }

    public void setBoughtPs(Set<Product> boughtPs) {
        this.boughtPs = boughtPs;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

}
