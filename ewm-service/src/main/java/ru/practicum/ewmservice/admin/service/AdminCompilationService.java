package ru.practicum.ewmservice.admin.service;

import ru.practicum.ewmservice.shared.dto.CompilationDto;
import ru.practicum.ewmservice.shared.dto.NewCompilationDto;
import ru.practicum.ewmservice.shared.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);
    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);
    void deleteCompilation(Long compId);

}
