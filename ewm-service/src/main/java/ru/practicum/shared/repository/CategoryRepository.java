package ru.practicum.ewmservice.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.shared.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}