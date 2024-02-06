package hello.productservice.main.controller;

import hello.productservice.main.data.dto.ProductDto;
import hello.productservice.main.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.UriUtil;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
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
    public String addNewProduct(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("productImages") List<MultipartFile> productImages) {
        log.info("productName={}",productDto.getProductName());
        log.info("productPrice={}",productDto.getProductPrice());
        log.info("productQuantity={}",productDto.getProductQuantity());
        log.info("업로드위치={}",uploadDirectory);


        try {
            ProductDto productDto1 = productService.saveProduct(productDto,productImages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                                    ,Model model) throws MalformedURLException {
        //db에서 id로 아이템 조회 DTO 객체로 저장
        ProductDto productById = productService.findProductById(id);
        model.addAttribute(productById);
        //파일 이름 확인
        //구현1 이미지 경로자체를 productDTo에 저장 - 수정하기 힘들다, 이미지를 받아오는 서비스계층에 넘기자
//        log.info("첫번째 파일 경로={}",productById.getProductImagesPath().get(0));
//        log.info("첫번째 파일 경로={}",productById.getProductImagesPath().get(1));
        log.info("첫번째 파일 경로 ={}",productById.getProductImagesName().get(0));
//        log.info("첫번째 파일 경로 ={}",productById.getProductImagesName().get(1));


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
                .header(HttpHeaders.CONTENT_TYPE,MediaType.IMAGE_JPEG_VALUE)
                .body(productImage);

    };

    /*/
    상품 수정 기능
     */
    @GetMapping("/{id}/update")
    public String showProductUpdateForm(@PathVariable Long id,
                                            Model model){

        //DB에서 Id로 상품 조회
        ProductDto foundProductById = productService.findProductById(id);
        //Model 정보 담아서 form에 전송, 수정form과 동일함
        model.addAttribute("productDto",foundProductById);
        model.addAttribute("productId",id);

        log.info("productManufacturer={}",foundProductById.getProductManufacturer());

        return "/product/product-updateform";

    }

    @PostMapping("/{id}/update")
    public String updateProduct(@ModelAttribute ProductDto productDto,
                                @PathVariable Long id,
                                Model model){
        //수정기능
        productService.updateProductById(id,productDto);

        //수정된 상품 객체에 저장
        ProductDto productById = productService.findProductById(id);
        //해당 product 상세 페이지로 이동
        model.addAttribute(productById);
        return "product/product-detail";
    }




    @GetMapping("/urltest")
    @ResponseBody
    public ResponseEntity<Resource> img() throws MalformedURLException {
        UrlResource urlResource = new UrlResource("file:" +
                uploadDirectory + "79a23780-be69-46af-8634-a9dd58fba7e2_apple.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(urlResource,headers,HttpStatus.OK);

    }

    @GetMapping("/urltest2")
    public ResponseEntity<Resource> img2() throws MalformedURLException {
        String fileName = "79a23780-be69-46af-8634-a9dd58fba7e2_apple.jpg";
        UrlResource urlResource = new UrlResource("file:" +
                uploadDirectory + fileName);

        //한글 깨짐 방지 UTF_8로 인코딩
        String encoeded = UriUtils.encode(fileName, StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename=\""+
                encoeded +"\"";

        return  ResponseEntity.ok()
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

        if(!file.isEmpty()){
            String fullPath = uploadDirectory + file.getOriginalFilename();
            log.info("fullPath={}", fullPath);
            file.transferTo(new File(fullPath));
        }


        return "/product/product-upload-form";
    }








}
