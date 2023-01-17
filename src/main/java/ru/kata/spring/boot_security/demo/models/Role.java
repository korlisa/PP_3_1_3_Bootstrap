package ru.kata.spring.boot_security.demo.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Data
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    public Role() {
    }
    public Role(String role) {
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        setName(role);
    }
    @Override
    public String getAuthority() {
        return getName();
    }
}
