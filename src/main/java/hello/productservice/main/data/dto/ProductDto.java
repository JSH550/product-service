package hello.productservice.main.data.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class ProductDto {

    private Long productId;
    private String productManufacturer;
    private String productName;
    private double productPrice;
    private int productQuantity;

//    private List<String> productImagesPath;
    private List<String> productImagesName;
        //    private List<MultipartFile> productFiles;

}
