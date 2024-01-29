package hello.productservice.main.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;


}
