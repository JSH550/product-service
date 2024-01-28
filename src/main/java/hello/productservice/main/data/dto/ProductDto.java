package hello.productservice.main.data.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ProductDto {

    private Long productId;

    private String productName;
    private double productPrice;
    private int productQuantity;
}
