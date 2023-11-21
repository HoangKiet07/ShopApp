package com.example.shopapp.services;

import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    Category updateCategory(long id, CategoryDTO category);

    void deteleCategory(long id);
}
