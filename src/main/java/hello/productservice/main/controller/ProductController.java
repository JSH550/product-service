package hello.productservice.main.controller;

import hello.productservice.main.data.dto.MemberDto;
import hello.productservice.main.data.dto.ProductDto;
import hello.productservice.main.service.ProductService;
import hello.productservice.main.test.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/list")
    public String showProductList(Model model){
        List<ProductDto> productList = productService.getAllProducts();
        model.addAttribute("productList",productList);
        ProductDto productDto = productList.get(0);
        log.info(productDto.getProductName());
        return "/product/product-list";

    };

    //새로운 prodcut 등록시, DB에 중복된 이름이 있으면 에러페이지로 이동
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(Model model, IllegalArgumentException ex) {
        model.addAttribute("customErrorMessage", "Bad Request: " + ex.getMessage());
        return "customError";
    }



    @GetMapping("/new")
    public String showProductAddForm(Model model){

        model.addAttribute("productDto",new ProductDto());
        return "/product/productAddForm";

    };
    @ResponseBody
    @PostMapping("")
    public String addNewProduct(@ModelAttribute("productDto") ProductDto productDto){
        log.info("productName={}",productDto.getProductName());
        log.info("productPrice={}",productDto.getProductPrice());
        log.info("productQuantity={}",productDto.getProductQuantity());


        //DB에서 상품명으로 조회, true false 반환
        //true면 에러 반환 flase면 save 진행
//      Optional<ProductDto> productByName =productService.findProductByName(productDto.getProductName());
        ProductDto productDto1 = productService.saveProduct(productDto);

//        if (productByName.isPresent()){
//            return "중복된 상품명입니다.";
//        }else {
//            ProductDto savedProduct = productService.saveProduct(productDto);
//            return savedProduct.getProductName();
//        }


return"ok";

    }
    /*
    DB에서 product Id로 상품 조회, 상세페이지 형성
     */
//    @ResponseBody
    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id
                                    ,Model model){
        //db에서 id로 아이템 조회
        //상세내용 불러와주셈
        ProductDto productById = productService.findProductById(id);
        model.addAttribute(productById);
        return "product/product-detail";

    }


}
