package ru.practicum.guest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.guest.service.PublicCompilationService;
import ru.practicum.shared.dto.CompilationDto;
import ru.practicum.shared.util.Pagination;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final PublicCompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<CompilationDto> compilations = compilationService.getCompilations(pinned, new Pagination(from,
                size, Sort.unsorted()));
        log.info("Получены подборки событий");
        return compilations;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        CompilationDto compilation = compilationService.getCompilationById(compId);
        log.info("Получена подборка событий с id: {}", compId);
        return compilation;
    }
}
