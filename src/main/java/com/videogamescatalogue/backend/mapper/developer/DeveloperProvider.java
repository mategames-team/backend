package com.videogamescatalogue.backend.mapper.developer;

import com.videogamescatalogue.backend.dto.external.ApiResponseDeveloperDto;
import com.videogamescatalogue.backend.dto.internal.developer.DeveloperDto;
import com.videogamescatalogue.backend.model.Developer;
import com.videogamescatalogue.backend.repository.DeveloperRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeveloperProvider {
    private final DeveloperMapper developerMapper;
    private final DeveloperRepository developerRepository;

    @Named("toDevelopersSet")
    public Set<Developer> toDevelopersSet(List<ApiResponseDeveloperDto> developers) {
        List<Long> developerApiIds = developers.stream()
                .map(ApiResponseDeveloperDto::id)
                .toList();

        List<Developer> existingDevelopers = developerRepository.findAllByApiIdIn(developerApiIds);

        Map<Long, Developer> existingDevelopersMap = existingDevelopers.stream()
                .collect(Collectors.toMap(
                        Developer::getApiId,
                        d -> d
                ));

        Set<Developer> developersSet = developers.stream()
                .map(d -> existingDevelopersMap.getOrDefault(
                        d.id(),
                        developerMapper.toModel(d)))
                .collect(Collectors.toSet());

        return developersSet;
    }

    @Named("toDeveloperDtosSet")
    public Set<DeveloperDto> toDeveloperDtosSet(Set<Developer> developers) {
        return developerMapper.toDtoSet(developers);
    }
}
