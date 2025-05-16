package com.group.SpringMVCProject.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @JsonManagedReference("user-role")
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    @JsonManagedReference("user-club")
    private List<Club> clubs = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonManagedReference("user-created-clubs")
    private List<Club> createdClubs = new ArrayList<>();
}