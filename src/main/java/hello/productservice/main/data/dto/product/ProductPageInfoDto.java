package hello.productservice.main.data.dto.product;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ProductPageInfoDto {
    @NotBlank(message = "페이지 번호가 없습니다.")
    int pageNumber;
    @NotBlank(message = "페이지 끝 번호가 없습니다.")
    int maxPageSize;
    @NotBlank(message = "페이지에 표시할 제품 수가 정의되지 않았습니다..")
    int listSize;


    public  void saveProductListDto(int pageNumber, int maxPageSize, int listSize) {
        this.pageNumber = pageNumber;
        this.maxPageSize = maxPageSize;
        this.listSize = listSize;
    }
}
