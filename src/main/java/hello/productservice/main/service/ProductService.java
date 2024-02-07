package hello.productservice.main.service;

import hello.productservice.main.data.dto.ProductDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDto saveProduct(ProductDto productDto,
                           List<MultipartFile> imagesList) throws IOException;

    ProductDto findProductById(Long productId);

    Optional<ProductDto> findProductByName(String productName);


   List<ProductDto> getAllProducts();

   List<ProductDto> searchProducts(String searchKeyword);
    Resource getProductFileByFileName(String fileName)  throws MalformedURLException;
//    void deleteProductByName(String productName);

    void updateProductById(Long productId, ProductDto productDto);

}
