package com.group.SpringMVCProject.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="clubs")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String photoUrl;
    private List<String> category;
    private String description;
    private List<String> tags;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_club",
            joinColumns = {@JoinColumn(name = "club_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private List<UserEntity> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @JsonBackReference("user-created-clubs")
    private UserEntity createdBy;

    @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
    @JsonManagedReference("club-event")
    private List<Event> events = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="city_id")
    @JsonBackReference("city-clubs")
    private City city;

    public Club(Long id, String title, String photoUrl, String description, LocalDateTime createdOn, LocalDateTime updatedOn, UserEntity createdBy, List<Event> events) {
        this.id = id;
        this.title = title;
        this.photoUrl = photoUrl;
        this.description = description;
        this.createdOn = LocalDateTime.now();
        this.updatedOn = LocalDateTime.now();
        this.createdBy = createdBy;
        this.events = events;
    }
}