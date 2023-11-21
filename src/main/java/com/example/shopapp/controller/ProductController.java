package com.example.shopapp.controller;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ProductListResponse;
import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.services.IProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        // tao page
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.findAll(pageRequest);
        // lay tong so trang
        int totalPage = productPage.getTotalPages();

        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(
                ProductListResponse.builder()
                        .productResponses(products)
                        .totalPages(totalPage)
                        .build());
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable("id") Long productId) throws Exception{
        Product product = productService.getProductById(productId);
        return ProductResponse.fromProduct(product);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
            ){
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError -> FieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product product = productService.createProduct(productDTO);
            return ResponseEntity.ok(product);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> uploadImages(
        @ModelAttribute("files") List<MultipartFile> files,
        @PathVariable Long id
) throws Exception {
        try{
            Product product = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            for (MultipartFile file : files) {
                if (file.getSize() >0 ){
                    if (file != null) {
                        // kiem tra kich thuoc file va dinh dang
                        if (file.getSize() > 10 * 1024 * 1024) {
                            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                                    .body("this file di too larget! Maximum size id 10 MB");
                        }
                        //lay dinh dang file
                        String contentType = file.getContentType();
                        if (contentType == null || !contentType.startsWith("image/")) {
                            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                    .body("this file must be an image");
                        }
                        String filename = storeFile(file);
                        ProductImage productImage = productService.createProductImage(id,
                                ProductImageDTO.builder()
                                        .imageUrl(filename)
                                        .build());
                    }
                }
            }
            return ResponseEntity.ok("Created Product + image");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

}

private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
}
private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
            //them UUID trc ten de filename la duy nhat
        String uniqueFilename = UUID.randomUUID().toString() + "_"+filename;
            //duong dan den thu muc muon luu file
        Path uploadDir = Paths.get("uploads");
            //kiem tra va tao thu muc luu tru
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
            //duong dan day du den file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
            // sao chep file vao thu muc dich

        return uniqueFilename;
}

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Delete product with id :" + id);
    }

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();
        for (int i =0; i<=200; i++){
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(1, 1000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(2,5))
                    .build();
            try{
                productService.createProduct(productDTO);
            }catch (Exception e){
                return ResponseEntity.badRequest().body("Fake Products falied");
            }
        }
        return ResponseEntity.ok("Fake Products");
    }
}


























