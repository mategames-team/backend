package com.videogamescatalogue.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "platforms")
@SQLDelete(sql = "UPDATE platforms SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Getter
@Setter
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private GeneralName generalName;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public enum GeneralName {
        PC("PC"),
        PLAYSTATION("PlayStation"),
        XBOX("Xbox"),
        NINTENDO_SWITCH("Nintendo Switch"),
        MOBILE("Mobile"),
        MAC("Mac"),
        LINUX("Linux"),
        SEGA("SEGA"),
        ATARI("Atari"),
        CLASSIC_CONSOLE("Console"),
        UNKNOWN("Unknown");

        private final String value;

        GeneralName(String value) {
            this.value = value;
        }
    }
}
