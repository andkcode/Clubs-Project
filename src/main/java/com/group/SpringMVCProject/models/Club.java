package com.group.SpringMVCProject.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String photoUrl;
    private String category;
    private String description;
    private Integer members;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy;

    @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Event> events = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="city_id")
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
