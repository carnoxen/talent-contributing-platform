package com.bitor.tft.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    public Optional<Category> findByName(String name);
}
