package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.AdminCompilationService;
import ru.practicum.shared.dto.CompilationDto;
import ru.practicum.shared.dto.NewCompilationDto;
import ru.practicum.shared.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final AdminCompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDTO) {
        log.info("Создана подборка событий: {}", newCompilationDTO);
        return compilationService.addCompilation(newCompilationDTO);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Обновлена подборка {} с id = {}", updateCompilationRequest, compId);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCompilation(@PathVariable Long compId) {
        log.info("Удалена подборка с id = {}", compId);
        compilationService.deleteCompilation(compId);
    }
}
