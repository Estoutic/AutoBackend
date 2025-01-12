package com.drujba.autobackend.db.entities;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, name = "role_name")
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<User> users;

}
