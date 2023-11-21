package com.example.shopapp.mappers;

import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper instance = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO categoryToCategoryDTO (Category category);

    Category categoryDTOToCategory(CategoryDTO categoryDTO);
}
