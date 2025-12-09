package com.videogamescatalogue.backend.mapper;

import com.videogamescatalogue.backend.dto.external.ApiResponseGenreDto;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.repository.GenreRepository;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GenreProvider {
    private final GenreRepository genreRepository;
    private Map<Genre.Name, Genre> defaultGenres;
    private Map<Genre.Name, String[]> possibleGenreNames = getPossibleNames();

    @PostConstruct
    private void init() {
        defaultGenres = genreRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Genre::getName,
                        genre -> genre)
                );
    }

    @Named("toGenresSet")
    public Set<Genre> toGenresSet(List<ApiResponseGenreDto> apiGenres) {
        Set<Genre> gameGenres = new HashSet<>();

        for (ApiResponseGenreDto apiGenre : apiGenres) {
            String name = apiGenre.name();
            if (name == null) {
                gameGenres.add(defaultGenres.get(Genre.Name.UNKNOWN));
                break;
            }
            String modifiedName = name.trim().toLowerCase();

            Genre genre = getDefaultGenre(modifiedName);

            gameGenres.add(genre);
        }
        return gameGenres;
    }

    private Genre getDefaultGenre(String name) {
        if (compareApiName(possibleGenreNames.get(Genre.Name.ACTION), name)) {
            return defaultGenres.get(Genre.Name.ACTION);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.INDIE), name)) {
            return defaultGenres.get(Genre.Name.INDIE);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.ADVENTURE), name)) {
            return defaultGenres.get(Genre.Name.ADVENTURE);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.RPG), name)) {
            return defaultGenres.get(Genre.Name.RPG);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.STRATEGY), name)) {
            return defaultGenres.get(Genre.Name.STRATEGY);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.SHOOTER), name)) {
            return defaultGenres.get(Genre.Name.SHOOTER);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.CASUAL), name)) {
            return defaultGenres.get(Genre.Name.CASUAL);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.SIMULATION), name)) {
            return defaultGenres.get(Genre.Name.SIMULATION);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.PUZZLE), name)) {
            return defaultGenres.get(Genre.Name.PUZZLE);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.ARCADE), name)) {
            return defaultGenres.get(Genre.Name.ARCADE);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.PLATFORMER), name)) {
            return defaultGenres.get(Genre.Name.PLATFORMER);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.MASS_MULTIPLAYER), name)) {
            return defaultGenres.get(Genre.Name.MASS_MULTIPLAYER);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.RACING), name)) {
            return defaultGenres.get(Genre.Name.RACING);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.SPORTS), name)) {
            return defaultGenres.get(Genre.Name.SPORTS);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.FIGHTING), name)) {
            return defaultGenres.get(Genre.Name.FIGHTING);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.BOARD), name)) {
            return defaultGenres.get(Genre.Name.BOARD);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.CARD), name)) {
            return defaultGenres.get(Genre.Name.CARD);
        } else if (compareApiName(possibleGenreNames.get(Genre.Name.EDUCATIONAL), name)) {
            return defaultGenres.get(Genre.Name.EDUCATIONAL);
        } else {
            return defaultGenres.get(Genre.Name.UNKNOWN);
        }
    }

    private boolean compareApiName(String[] values, String name) {
        return Arrays.stream(values)
                .anyMatch(name::contains);
    }

    private Map<Genre.Name, String[]> getPossibleNames() {
        Map<Genre.Name, String[]> names = new HashMap<>();

        names.put(Genre.Name.ACTION, new String[] {"action"});
        names.put(Genre.Name.INDIE, new String[] {"indie"});
        names.put(Genre.Name.ADVENTURE, new String[] {"adventure"});
        names.put(Genre.Name.RPG, new String[] {"rpg"});
        names.put(Genre.Name.STRATEGY, new String[] {"strategy"});
        names.put(Genre.Name.SHOOTER, new String[] {"shooter"});
        names.put(Genre.Name.CASUAL, new String[] {"casual"});
        names.put(Genre.Name.SIMULATION, new String[] {"simulation"});
        names.put(Genre.Name.PUZZLE, new String[] {"puzzle"});
        names.put(Genre.Name.ARCADE, new String[] {"arcade"});
        names.put(Genre.Name.PLATFORMER, new String[] {"platformer"});
        names.put(Genre.Name.MASS_MULTIPLAYER, new String[] {"multiplayer"});
        names.put(Genre.Name.RACING, new String[] {"racing"});
        names.put(Genre.Name.SPORTS, new String[] {"sports"});
        names.put(Genre.Name.FIGHTING, new String[] {"fighting"});
        names.put(Genre.Name.FAMILY, new String[] {"family"});
        names.put(Genre.Name.BOARD, new String[] {"board"});
        names.put(Genre.Name.CARD, new String[] {"card"});
        names.put(Genre.Name.EDUCATIONAL, new String[] {"educational"});

        return names;
    }
}
