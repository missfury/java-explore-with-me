package ru.practicum.ewmservice.admin.service;

import ru.practicum.ewmservice.shared.dto.CategoryDto;
import ru.practicum.ewmservice.shared.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);
}
