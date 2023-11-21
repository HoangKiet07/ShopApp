package com.example.shopapp.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductListResponse {
    private List<ProductResponse> productResponses;
    private int totalPages;
}
