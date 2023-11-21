package com.example.shopapp.services.Impl;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.ProductImageRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()->new DataNotFoundException("Cannot find category with id:" + productDTO.getCategoryId()));
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .category(category)
                .description(productDTO.getDescription())
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot fine product with id: "+ id));
    }

    @Override
    public Page<ProductResponse> findAll(PageRequest page) {
        return productRepository.findAll(page).map(product -> ProductResponse.fromProduct(product));
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
        Product product = getProductById(id);
        if (product != null){
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(()->new DataNotFoundException("Cannot find category with id:" + productDTO.getCategoryId()));
            product.setName(productDTO.getName());
            product.setCategory(category);
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException("Cannot find product with id:" + productImageDTO.getProductId()));
        ProductImage productImage = ProductImage.builder()
                .imageUrl(productImageDTO.getImageUrl())
                .product(product)
                .build();

        int size = productImageRepository.findByProduct(product).size();
        if (size>=5){
            throw new InvalidParamException("Number of images must be <5");
        }
        return productImageRepository.save(productImage);
    }
}
