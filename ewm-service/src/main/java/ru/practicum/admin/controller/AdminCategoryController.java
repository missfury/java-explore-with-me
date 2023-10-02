package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.AdminCategoryService;
import ru.practicum.shared.dto.CategoryDto;
import ru.practicum.shared.dto.NewCategoryDto;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        CategoryDto category = adminCategoryService.addCategory(newCategoryDto);
        log.info("Создана категория {}", newCategoryDto);
        return category;
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = adminCategoryService.updateCategory(catId, categoryDto);
        log.info("Обновлена категория {} с id= {}", categoryDto, catId);
        return category;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable Long catId) {
        adminCategoryService.deleteCategory(catId);
        log.info("Удалена категория с id= {}", catId);
    }
}
