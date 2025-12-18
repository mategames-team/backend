package com.videogamescatalogue.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "games")
@SQLDelete(sql = "UPDATE games SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long apiId;

    @Column(nullable = false)
    private String name;

    private Integer year;

    private String backgroundImage;

    @ManyToMany
    @JoinTable(
            name = "games_platforms",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"game_id", "platform_id"})
    )
    private Set<Platform> platforms = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "games_genres",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"game_id", "genre_id"})
    )
    private Set<Genre> genres = new HashSet<>();

    private BigDecimal apiRating;

    private String description;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    @Getter
    public enum SpecificationKey {
        NAME("name"),
        YEAR("year"),
        PLATFORMS("platforms"),
        GENRES("genres");

        private final String value;

        SpecificationKey(String value) {
            this.value = value;
        }
    }
}
