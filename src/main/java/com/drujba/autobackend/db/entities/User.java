package com.drujba.autobackend.db.entities;

import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.utils.PasswordEncoder;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    @JsonIgnore
    private String password;

    private String phone;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "is_verified", nullable = true)
    private Boolean is_verified = false;

    public User(UserDto userDto) {
        this.roles = new HashSet<>();
        this.email = userDto.getEmail();
        this.password = PasswordEncoder.getInstance().encode(userDto.getPassword());
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

}
