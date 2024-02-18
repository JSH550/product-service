package hello.productservice.main.controller;

import hello.productservice.main.data.dto.product.ProductDto;
import hello.productservice.main.data.dto.product.ProductPageInfoDto;
import hello.productservice.main.data.dto.product.ProductSaveDto;
import hello.productservice.main.service.ProductService;
import hello.productservice.main.validator.ValidationUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/products")

public class ProductController {

    private final ProductService productService;
    //uploadDirectory, application.properties에서의 property값 가져옴
    @Value("${file.dir}")
    private String uploadDirectory;
//    private final ProductValidator productValidator;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
//        this.productValidator = productValidator;
    }

//    @GetMapping("")
//    public String showProductList(Model model){
//        List<ProductDto> productList = productService.getAllProducts();
//        model.addAttribute("productList",productList);
//        ProductDto productDto = productList.get(0);
//        log.info(productDto.getProductName());
////        return "/product/product-list";
//        return "/product/product-list-new";
//
//    };

    //클라이언트가 요청한 product 갯수, pageNumber로 produtList page 보여주는 기능
    //    @ResponseBody
    @GetMapping("")
    public String showProductListByPage(@RequestParam(required = false) Integer pageNumber,
                                        @RequestParam(required = false) Integer listSize,
                                        Model model) {
        // 클라이언트가 요청한 페이지 번호와 리스트 크기를 그대로 사용
        // 단, 리스트 크기는 최소 4 최대 100으로 제한
        pageNumber = ValidationUtils.validatePageNumber(pageNumber);
        listSize = ValidationUtils.validateListSize(listSize);

        List<ProductDto> productList = productService.findProducts(pageNumber, listSize);
        //DB에 저장된 product 전체 수/listSize, 소수점은 올림처리
        int maxPageSize = productService.calculateMaxPageSize(listSize);

        model.addAttribute("productList", productList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("listSize", listSize);
        model.addAttribute("maxPageSize", maxPageSize);

        ProductDto productDto = productList.get(0);
        log.info(productDto.getProductName());
        return "/product/product-list-new2";
    }

//    검색기능, 클라이언트가 검색어를 입력하면 query parameter로 받고 DB에서 Like로 조회

    //    @ResponseBody
    @GetMapping("/search")
    public String searchProduct(@RequestParam(required = false) Integer pageNumber,
                                @RequestParam(required = false) Integer listSize,
                                @RequestParam(required = false) String query,
                                Model model) {

        //query에 아무것도 담겨있지 않으면 예외로 던짐
        if (query.isEmpty()) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        // 클라이언트가 요청한 페이지 번호와 리스트 크기를 그대로 사용
        // 단, listSIze min 4 ~ max 100
        pageNumber = ValidationUtils.validatePageNumber(pageNumber);
        listSize = ValidationUtils.validateListSize(listSize);

        List<ProductDto> productList = productService.findProducts(pageNumber, listSize, query);

        int maxPageSize = productService.calculateMaxPageSize(listSize, query);

        ProductPageInfoDto productPageInfoDto = new ProductPageInfoDto();
        productPageInfoDto.saveProductListDto(pageNumber,maxPageSize,listSize);

        model.addAttribute("productPageInfoDto", productPageInfoDto);
        model.addAttribute("productList", productList);
//        model.addAttribute("pageNumber", pageNumber);
//        model.addAttribute("listSize", listSize);
//        model.addAttribute("maxPageSize", maxPageSize);

//        List<ProductDto> searchedProductList = productService.searchProducts(query);
//
//        model.addAttribute("productList",searchedProductList);

        return "/product/product-list-new2";
//        return "ok";
    }

    //새로운 prodcut 등록시, DB에 중복된 이름이 있으면 에러페이지로 이동


    @GetMapping("/new")
    public String showProductAddForm(Model model) {
//        model.addAttribute("productDto",new ProductDto())
        model.addAttribute("productSaveDto", new ProductSaveDto());
        return "/product/product-addform";
    }

    ;

    //    새로운 Product 추가 기능
//    @ResponseBody
    @PostMapping("/new")
    public String addNewProduct(@Validated @ModelAttribute("productSaveDto") ProductSaveDto productSaveDto,
                                BindingResult bindingResult, Model model,
                                @RequestParam("productImages") List<MultipartFile> productImages) throws IOException {

//        log.info("productName={}",productDto.getProductName());
//        log.info("productPrice={}",productDto.getProductPrice());
////        log.info("productQuantity={}",productDto.getProductQuantity());
//        log.info("업로드위치={}",uploadDirectory);

        //에러 발생시 addform으로 리다이렉트
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("errors", bindingResult);
            return "/product/product-addform";
        }

        //새로운 product 저장 성공시 해당 product 상세페이지로 이동
        try {
            ProductDto saveProduct = productService.saveProduct(productSaveDto, productImages);
            return "redirect:/products/" + saveProduct.getProductId();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("IOException occurred while save product", e);//에러 발생시 예외로 던짐

        }
    }

    /*
    DB에서 product Id로 상품 조회, 상세페이지 형성
     */
//    @ResponseBody
    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id
            , Model model) throws MalformedURLException {
        //db에서 id로 아이템 조회 DTO 객체로 저장
        ProductDto productById = productService.findProductById(id);
        model.addAttribute(productById);
        //파일 이름 확인
        log.info("첫번째 파일 경로 ={}", productById.getProductImagesName().get(0));

        //기능구현 1 - 첫번째 파일 이름 view에 전송
//        UrlResource urlResource = new UrlResource("file:"+
//                uploadDirectory + productById.getProductImagesPath().get(0));
//        HttpHeaders headers= new HttpHeaders();
//        if (productById.getProductImages().isEmpty()){
//            log.info("이미지없음;");
//        }

        model.addAttribute(productById);
        return "product/product-detail";
    }

    @GetMapping("/images/{imageName}")
    @ResponseBody
    public ResponseEntity<Resource> serveProductImage(@PathVariable String imageName) throws MalformedURLException {
        Resource productImage = productService.getProductFileByFileName(imageName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(productImage);

    }

    ;

    /*/
    상품 수정 기능
     */
    @GetMapping("/{id}/update")
    public String showProductUpdateForm(@PathVariable Long id,
                                        Model model) {

        //DB에서 Id로 상품 조회
        ProductDto foundProductById = productService.findProductById(id);
        //Model 정보 담아서 form에 전송
        model.addAttribute("productDto", foundProductById);
        model.addAttribute("productId", id);

        log.info("productManufacturer={}", foundProductById.getProductManufacturer());

        return "/product/product-updateform";

    }

    @PostMapping("/{id}/update")
    public String updateProduct(@ModelAttribute ProductDto productDto,
                                @PathVariable Long id,
                                Model model) {
        //수정기능
        productService.updateProductById(id, productDto);

        //수정된 상품 객체에 저장
        ProductDto productById = productService.findProductById(id);
        //해당 product 상세 페이지로 이동
        model.addAttribute(productById);
        return "redirect:/products/" + productById.getProductId();
    }


    @GetMapping("/urltest")
    @ResponseBody
    public ResponseEntity<Resource> img() throws MalformedURLException {
        UrlResource urlResource = new UrlResource("file:" +
                uploadDirectory + "79a23780-be69-46af-8634-a9dd58fba7e2_apple.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(urlResource, headers, HttpStatus.OK);

    }

    @GetMapping("/urltest2")
    public ResponseEntity<Resource> img2() throws MalformedURLException {
        String fileName = "79a23780-be69-46af-8634-a9dd58fba7e2_apple.jpg";
        UrlResource urlResource = new UrlResource("file:" +
                uploadDirectory + fileName);

        //한글 깨짐 방지 UTF_8로 인코딩
        String encoeded = UriUtils.encode(fileName, StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename=\"" +
                encoeded + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
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

        if (!file.isEmpty()) {
            String fullPath = uploadDirectory + file.getOriginalFilename();
            log.info("fullPath={}", fullPath);
            file.transferTo(new File(fullPath));
        }


        return "/product/product-upload-form";
    }


}
