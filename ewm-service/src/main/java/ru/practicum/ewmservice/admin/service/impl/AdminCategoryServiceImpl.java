package ru.practicum.ewmservice.admin.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.ewmservice.admin.service.AdminCategoryService;
import ru.practicum.ewmservice.shared.dto.CategoryDto;
import ru.practicum.ewmservice.shared.dto.NewCategoryDto;
import ru.practicum.ewmservice.shared.exceptions.NotAvailableException;
import ru.practicum.ewmservice.shared.exceptions.NotFoundException;
import ru.practicum.ewmservice.shared.mapper.CategoryMapper;
import ru.practicum.ewmservice.shared.model.Category;
import ru.practicum.ewmservice.shared.repository.CategoryRepository;

@Service
@Transactional
@AllArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(categoryMapper.toCategory(newCategoryDto));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + id + " не найдена"));
        category.setName(categoryDto.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Категория с id=" + id + " не найдена");
        } else {
            try {
                categoryRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                throw new NotAvailableException("Категория не пустая");
            }
        }
    }

}
