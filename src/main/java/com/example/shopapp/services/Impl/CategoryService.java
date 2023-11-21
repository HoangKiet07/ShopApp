package com.example.shopapp.services.Impl;

import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.mappers.CategoryMapper;
import com.example.shopapp.models.Category;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Category createCategory(CategoryDTO categoryDto) {
        Category newCategory = CategoryMapper.instance.categoryDTOToCategory(categoryDto);
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Category nod found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long id, CategoryDTO categoryDto) {
        Category exitsCategory = getCategoryById(id);
        exitsCategory.setName(categoryDto.getName());
        categoryRepository.save(exitsCategory);
        return exitsCategory;
    }

    @Override
    public void deteleCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
