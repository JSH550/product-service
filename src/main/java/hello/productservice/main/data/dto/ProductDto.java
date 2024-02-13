package hello.productservice.main.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "제조사를 입력해주세요.")
    private String productManufacturer;
    @NotBlank(message = "제품명을 입력해주세요.")
    private String productName;
    @Min(value = 0, message = "가격은 0보다 커야합니다.")
    private double productPrice;
    @Min(value = 0, message = "수량은 0보다 커야합니다.")
    private int productQuantity;

//    private List<String> productImagesPath;
    private List<String> productImagesName;
        //    private List<MultipartFile> productFiles;

}
