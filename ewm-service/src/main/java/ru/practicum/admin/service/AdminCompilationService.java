package ru.practicum.admin.service;

import ru.practicum.shared.dto.CompilationDto;
import ru.practicum.shared.dto.NewCompilationDto;
import ru.practicum.shared.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);

    void deleteCompilation(Long compId);

}
