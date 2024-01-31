package hello.productservice.main.controller;

import hello.productservice.main.data.dto.ProductDto;
import hello.productservice.main.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {

    //uploadDirectory, application.properties에서의 property값 가져옴
    @Value("${file.dir}")
    private String uploadDirectory;

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(Model model, IllegalArgumentException ex) {
        model.addAttribute("customErrorMessage", "Bad Request: " + ex.getMessage());
        return "customError";
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNoSuchElementException(Model model, NoSuchElementException ex) {
        model.addAttribute("customErrorMessage", "Bad Request: " + ex.getMessage());
        return "customError";
    }

    @GetMapping("")
    public String showProductList(Model model){
        List<ProductDto> productList = productService.getAllProducts();
        model.addAttribute("productList",productList);
        ProductDto productDto = productList.get(0);
        log.info(productDto.getProductName());
        return "/product/product-list";

    };
    /*/
    검색기능, 클라이언트가 검색어를 입력하면 query parameter로 받고 DB에서 Like로 조회
     */

    @GetMapping("/search")
    public String searchProduct(@RequestParam String query,
                                Model model){
        List<ProductDto> searchedProductList = productService.searchProducts(query);
        model.addAttribute("productList",searchedProductList);
        log.info(query);
        return "/product/product-list";

    }

    //새로운 prodcut 등록시, DB에 중복된 이름이 있으면 에러페이지로 이동


    @GetMapping("/new")
    public String showProductAddForm(Model model){

        model.addAttribute("productDto",new ProductDto());
        return "/product/product-addform";

    };
    @ResponseBody
    @PostMapping("")
    public String addNewProduct(@ModelAttribute("productDto") ProductDto productDto) throws IOException {
        log.info("productName={}",productDto.getProductName());
        log.info("productPrice={}",productDto.getProductPrice());
        log.info("productQuantity={}",productDto.getProductQuantity());
        log.info("productFiles={}",productDto.getProductFiles().isEmpty());
        log.info("업로드위치={}",uploadDirectory);

        //TODO db에 파일명 저장하기

            ProductDto productDto1 = productService.saveProduct(productDto);
            return"ok";

        //DB에서 상품명으로 조회, true false 반환
        //true면 에러 반환 flase면 save 진행
//        ProductDto productDto1 = productService.saveProduct(productDto);
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

    /*/
    업로드기능 테스트
     */

    //application.properties 의 file.dir properties 값을 가져옴



    @GetMapping("/upload")
    public String newFile() {
        return "/product/product-upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file,
                           HttpServletRequest request) throws IOException {
        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);

        if(!file.isEmpty()){
            String fullPath = uploadDirectory + file.getOriginalFilename();
            log.info("fullPath={}", fullPath);
            file.transferTo(new File(fullPath));
        }


        return "/product/product-upload-form";
    }








}
