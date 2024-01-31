package hello.productservice.main.service;

import hello.productservice.main.data.dto.ProductDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDto saveProduct(ProductDto productDto) throws IOException;

    ProductDto findProductById(Long productId);

    Optional<ProductDto> findProductByName(String productName);


   List<ProductDto> getAllProducts();

   List<ProductDto> searchProducts(String searchKeyword);

//    void deleteProductByName(String productName);

}
