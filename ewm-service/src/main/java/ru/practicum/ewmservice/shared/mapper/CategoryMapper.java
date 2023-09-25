package ru.practicum.ewmservice.shared.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.shared.dto.CategoryDto;
import ru.practicum.ewmservice.shared.dto.NewCategoryDto;
import ru.practicum.ewmservice.shared.model.Category;

@Component
public class CategoryMapper {
    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toCategory(NewCategoryDto newCategoryDTO) {
        return Category.builder()
                .name(newCategoryDTO.getName())
                .build();
    }
}
