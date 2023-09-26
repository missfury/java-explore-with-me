package ru.practicum.guest.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shared.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(Long compId);
}
