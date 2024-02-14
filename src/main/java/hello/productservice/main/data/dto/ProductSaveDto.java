package hello.productservice.main.data.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
//저장용 DTO, productId 필수 해제
public class ProductSaveDto {

//    private Long productId;

    @NotBlank(message = "제조사를 입력해주세요.")
    private String productManufacturer;
    @NotBlank(message = "제품명을 입력해주세요.")
    private String productName;
    @Min(value = 0, message = "가격은 0보다 커야합니다.")
    @Max(value = 10000000, message = "가격은 10,000,000원 보다 작아야합니다.")
    private int productPrice;

//    @Min(value = 0, message = "수량은 0보다 커야합니다.")
//    @Max(value = 100000, message = "수량은 100000보다 작아야합니다.")
//    private int productQuantity;

    //    private List<String> productImagesPath;
    private List<String> productImagesName;
    //    private List<MultipartFile> productFiles;

}
