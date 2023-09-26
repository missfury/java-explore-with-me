package ru.practicum.admin.service;

import ru.practicum.shared.dto.CategoryDto;
import ru.practicum.shared.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);
}
