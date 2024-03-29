package ru.practicum.shared.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shared.dto.CompilationDto;
import ru.practicum.shared.dto.NewCompilationDto;
import ru.practicum.shared.model.Compilation;
import ru.practicum.shared.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream()
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Compilation toCompilation(NewCompilationDto newCompilationDTO, Set<Event> events) {
        return Compilation.builder()
                .title(newCompilationDTO.getTitle())
                .pinned(newCompilationDTO.getPinned())
                .events(events)
                .build();
    }

}
