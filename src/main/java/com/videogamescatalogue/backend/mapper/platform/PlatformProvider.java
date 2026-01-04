package com.videogamescatalogue.backend.mapper.platform;

import com.videogamescatalogue.backend.dto.external.ApiPlatformWrapper;
import com.videogamescatalogue.backend.dto.external.ApiResponsePlatformDto;
import com.videogamescatalogue.backend.dto.internal.platform.PlatformDto;
import com.videogamescatalogue.backend.model.Platform;
import com.videogamescatalogue.backend.repository.PlatformRepository;
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

@Component
@RequiredArgsConstructor
public class PlatformProvider {
    private final PlatformRepository platformRepository;
    private final PlatformMapper platformMapper;
    private Map<Platform.GeneralName, Platform> defaultPlatforms;
    private Map<Platform.GeneralName, String[]> possiblePlatformNames = getPossibleNames();

    @PostConstruct
    private void init() {
        defaultPlatforms = platformRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Platform::getGeneralName,
                        platform -> platform)
                );
    }

    @Named("toPlatformsSet")
    public Set<Platform> toPlatformSet(List<ApiPlatformWrapper> apiPlatformWrappers) {
        List<ApiResponsePlatformDto> platforms = apiPlatformWrappers.stream()
                .map(ApiPlatformWrapper::platform)
                .toList();
        Set<Platform> gamePlatforms = new HashSet<>();

        for (ApiResponsePlatformDto apiPlatform : platforms) {
            String name = apiPlatform.name();
            if (name == null) {
                gamePlatforms.add(defaultPlatforms.get(Platform.GeneralName.UNKNOWN));
                break;
            }
            String modifiedName = name.trim().toLowerCase();
            Platform platform = getDefaultPlatform(modifiedName);

            gamePlatforms.add(platform);
        }
        return gamePlatforms;
    }

    @Named("toPlatformDtosSet")
    public Set<PlatformDto> toPlatformDtosSet(Set<Platform> platforms) {
        return platformMapper.toPlatfromDtosSet(platforms);
    }

    private Platform getDefaultPlatform(String name) {
        if (compareApiName(possiblePlatformNames.get(Platform.GeneralName.PC), name)) {
            return defaultPlatforms.get(Platform.GeneralName.PC);
        } else if (compareApiName(possiblePlatformNames.get(
                Platform.GeneralName.PLAYSTATION), name
        )) {
            return defaultPlatforms.get(Platform.GeneralName.PLAYSTATION);
        } else if (compareApiName(possiblePlatformNames.get(Platform.GeneralName.XBOX), name)) {
            return defaultPlatforms.get(Platform.GeneralName.XBOX);
        } else if (compareApiName(possiblePlatformNames.get(
                Platform.GeneralName.NINTENDO_SWITCH), name
        )) {
            return defaultPlatforms.get(Platform.GeneralName.NINTENDO_SWITCH);
        } else if (compareApiName(possiblePlatformNames.get(Platform.GeneralName.MOBILE), name)) {
            return defaultPlatforms.get(Platform.GeneralName.MOBILE);
        } else if (compareApiName(possiblePlatformNames.get(Platform.GeneralName.MAC), name)) {
            return defaultPlatforms.get(Platform.GeneralName.MAC);
        } else if (compareApiName(possiblePlatformNames.get(Platform.GeneralName.LINUX), name)) {
            return defaultPlatforms.get(Platform.GeneralName.LINUX);
        } else if (compareApiName(possiblePlatformNames.get(Platform.GeneralName.SEGA), name)) {
            return defaultPlatforms.get(Platform.GeneralName.SEGA);
        } else if (compareApiName(possiblePlatformNames.get(Platform.GeneralName.ATARI), name)) {
            return defaultPlatforms.get(Platform.GeneralName.ATARI);
        } else if (compareApiName(possiblePlatformNames.get(
                Platform.GeneralName.CLASSIC_CONSOLE), name
        )) {
            return defaultPlatforms.get(Platform.GeneralName.CLASSIC_CONSOLE);
        } else {
            return defaultPlatforms.get(Platform.GeneralName.UNKNOWN);
        }
    }

    boolean compareApiName(String[] values, String name) {
        return Arrays.stream(values)
                .anyMatch(name::contains);
    }

    private Map<Platform.GeneralName, String[]> getPossibleNames() {
        Map<Platform.GeneralName, String[]> names = new HashMap<>();

        names.put(Platform.GeneralName.PC,
                new String[] {"pc", "windows", "steam", "gog"});
        names.put(Platform.GeneralName.PLAYSTATION,
                new String[] {"playstation", "ps", "psp"});
        names.put(Platform.GeneralName.XBOX,
                new String[] {"xbox"});
        names.put(Platform.GeneralName.NINTENDO_SWITCH,
                new String[] {"switch", "nintendo",
                        "wii", "gamecube", "game boy", "snes", "nes"});
        names.put(Platform.GeneralName.MOBILE,
                new String[] {"ios", "android", "mobile"});
        names.put(Platform.GeneralName.MAC,
                new String[] {"macos", "classic macintosh", "apple"});
        names.put(Platform.GeneralName.LINUX,
                new String[] {"linux"});
        names.put(Platform.GeneralName.SEGA,
                new String[] {"genesis", "sega"});
        names.put(Platform.GeneralName.ATARI,
                new String[] {"atari"});
        names.put(Platform.GeneralName.CLASSIC_CONSOLE,
                new String[] {"dreamcast", "3do", "jaguar", "game gear", "neo geo"});

        return names;
    }
}
