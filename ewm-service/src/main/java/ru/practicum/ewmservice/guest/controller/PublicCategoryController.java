package ru.practicum.ewmservice.guest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.guest.service.PublicCategoryService;
import ru.practicum.ewmservice.shared.dto.CategoryDto;
import ru.practicum.ewmservice.shared.util.Pagination;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/categories")
public class PublicCategoryController {

    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен список категорий");
        return publicCategoryService.getCategories(new Pagination(from, size, Sort.unsorted()));
    }

    @GetMapping("/{catId}")
    CategoryDto getCategory(@PathVariable Long catId) {
        log.info("Найдена категория с id {}", catId);
        return publicCategoryService.getCategoryById(catId);
    }

}
