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
public class ProductFileDto {
    private Long productFileId;
    private Product product;
    private String productFileName;
    private String productFilePath;
}
