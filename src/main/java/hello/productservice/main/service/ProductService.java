package hello.productservice.main.service;

import hello.productservice.main.data.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDto saveProduct(ProductDto productDto);

    ProductDto findProductById(Long productId);

    Optional<ProductDto> findProductByName(String productName);


   List<ProductDto> getAllProducts();

//    void deleteProductByName(String productName);

}
