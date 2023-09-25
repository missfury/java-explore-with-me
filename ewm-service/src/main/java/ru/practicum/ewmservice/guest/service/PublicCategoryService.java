package ru.practicum.ewmservice.guest.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmservice.shared.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {
    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(Long catId);
}
