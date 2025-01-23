package com.drujba.autobackend.db.entities.auth;

import com.drujba.autobackend.db.entities.Report;
import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.utils.PasswordEncoder;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    @Column(name = "is_verified", nullable = true)
    private Boolean isVerified = false;

    private Boolean isActive;

    public User(UserDto userDto) {
        this.roles = new HashSet<>();
        this.reports = new ArrayList<>();
        this.email = userDto.getEmail();
        isActive = true;
        this.password = PasswordEncoder.getInstance().encode(userDto.getPassword());
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

}
