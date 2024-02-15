package hello.productservice.main.data.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
//저장용 DTO, productId 필수 해제
public class ProductSaveDto {

    @NotBlank(message = "제조사를 입력해주세요.")
    private String productManufacturer;
    @NotBlank(message = "제품명을 입력해주세요.")
    private String productName;
    @Range(min=0, max=100000,message = "가격은 1에서 100,000 사이여야 합니다.")
    private int productPrice;
    private List<String> productImagesName;
}
