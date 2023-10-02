package ru.practicum.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shared.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}