package hello.productservice.main.validator;

public class ValidationUtils {

    public static int validatePageNumber(Integer pageNumber) {
        if (pageNumber == null || pageNumber == 0) {
            return 1; //페이지 번호가 null이거나 0일경우 기본값 1로 설정
        }
        return pageNumber;

    }

    public static int validateListSize(Integer listSize) {
        if (listSize == null || listSize == 0) {
            return 4;//리스트 크기가 null이거나 0일경우 기본값 4로 설정
        } else if (listSize > 100) {
            return 100;//리스트 크기가 100을 초과할경우 100으로 제한
        } else {
            return listSize;
        }
    }
}
