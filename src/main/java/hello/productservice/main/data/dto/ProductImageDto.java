package hello.productservice.main.data.dto;

import hello.productservice.main.data.entity.product.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProductImageDto {

    private Long productImageId;
//    private Product product;
    private String productImageName;
//    private String productImagePath;
}
