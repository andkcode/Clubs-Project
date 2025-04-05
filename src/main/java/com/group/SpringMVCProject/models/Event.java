package com.group.SpringMVCProject.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@Entity
@Table(name="events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String type;
    private String photoUrl;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name="club_id", nullable = false)
    private Club club;

    public Event(Long id, String title, String description, LocalDateTime startTime, LocalDateTime endTime, String type, String photoUrl, LocalDateTime createdOn, LocalDateTime updatedOn, Club club) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.photoUrl = photoUrl;
        this.createdOn = LocalDateTime.now();
        this.updatedOn = LocalDateTime.now();
        this.club = club;
    }
}
