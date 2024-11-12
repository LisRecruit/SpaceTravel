package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @ManyToOne( cascade = CascadeType.ALL)  //fetch = FetchType.LAZY
    @PrimaryKeyJoinColumn
    @MapsId
//    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;
    @ManyToOne( cascade = CascadeType.ALL) //fetch = FetchType.LAZY
//    @PrimaryKeyJoinColumn
//    @MapsId
    @JoinColumn(name = "from_planet_id", referencedColumnName = "id", nullable = false)
    private Planet fromPlanetId;
    @ManyToOne( cascade = CascadeType.ALL)
//    @PrimaryKeyJoinColumn
//    @MapsId
    @JoinColumn(name = "to_planet_id", referencedColumnName = "id", nullable = false)
    private Planet toPlanetId;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
