package hello.productservice.main.service.impl;

import hello.productservice.main.data.dto.ProductDto;
import hello.productservice.main.data.entity.product.Product;
import hello.productservice.main.data.entity.product.ProductFile;
import hello.productservice.main.repository.ProductRepository;
import hello.productservice.main.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Value("${file.dir}")
    private String uploadDirectory;

    //TODO 이미지 업로드
    //TODO 제조사 정보 추가
    @Override
    @Transactional
    public ProductDto saveProduct(ProductDto productDto) throws IOException {

        //DB에서 클라이언트가 입력한 productName 있는지 조회
        boolean isProductExist = productRepository.existsByProductName(productDto.getProductName());

        log.info("ExistResult={}",isProductExist);
        if (isProductExist){
            throw new IllegalArgumentException("이미 등록된 상품명입니다. 다른 상품명을 입력해주세요");
        }

        Product product = new Product();
        product.saveProduct(productDto.getProductName()
                ,productDto.getProductPrice()
                ,productDto.getProductQuantity());
        Product savedProduct = productRepository.save(product);

        //파일저장기능

            List<MultipartFile> productFiles = productDto.getProductFiles();
        saveAttachedFiles(productFiles, savedProduct);

//        List<MultipartFile> productImages = productDto.getProductImages();
//        saveAttachedFiles(productImages, savedProduct);



//        //파일저장기능
//
//        List<MultipartFile> productFiles = productDto.getProductFiles();
//
//        for(MultipartFile file : productFiles){
//
//
//            //UUID + filename 으로 고유 filename 형성
//            String savedFileName = createFileName(file);
//            //지정된 directory save + 저장할 파일이름 + 확장자
//            String savedFilePath = uploadDirectory + File.separator + savedFileName;
//
//            //file.getBytes() -> MultipartFile 객체에서 바이트 배열로 데이터를 읽어와서 byte 형태로 저장
//            //byte[]
//            byte[] fileData = file.getBytes();
//            //String 형태의 path를 Java의 Path 형식으로 변환
//            Path filePath = Paths.get(savedFilePath);
//            //file 저장
//            Files.write(filePath, fileData);
//
//            //productFile entity 정보 저장
//            ProductFile productFile = new ProductFile();
//            productFile.saveProductFile(savedProduct,savedFileName,savedFilePath);
//            savedProduct.getProductFiles().add(productFile);
//        }



        ProductDto savedProductDto = convertToDto(product);
        return savedProductDto;
    }

    private void saveAttachedFiles(List<MultipartFile> multipartFileList, Product product) throws IOException {
        for(MultipartFile file : multipartFileList){

            //UUID + filename 으로 고유 filename 형성
            String savedFileName = createFileName(file);
            //지정된 directory save + 저장할 파일이름 + 확장자
            String savedFilePath = uploadDirectory + File.separator + savedFileName;

            //file.getBytes() -> MultipartFile 객체에서 바이트 배열로 데이터를 읽어와서 byte 형태로 저장
            //byte[]
            byte[] fileData = file.getBytes();
            //String 형태의 path를 Java의 Path 형식으로 변환
            Path filePath = Paths.get(savedFilePath);
            //file 저장
            Files.write(filePath, fileData);

            //productFile entity 정보 저장
            ProductFile productFile = new ProductFile();
            productFile.saveProductFile(product,savedFileName,savedFilePath);
            product.getProductFiles().add(productFile);
        }
    }

    //UUID + filename으로 고유 filename 형성 메서드
    private String createFileName(MultipartFile file) {
        String ext = extractExt(file);
        return UUID.randomUUID().toString() + "_" + file.getOriginalFilename()+ext;
    }

    //filename에서 확장자(ext)추출 메서드
    private String extractExt(MultipartFile file){
        //확장자는 .확장자 형태, 마지막으로 .문자 index를 변수로 추출
        int extStartPosition = file.getOriginalFilename().lastIndexOf(".");
        //확장자 추출하여 반환
        return file.getOriginalFilename().substring(extStartPosition+1);

    }

    @Override
    public ProductDto findProductById(Long productId) {
        Optional<Product> foundProduct = productRepository.findById(productId);
        if (foundProduct.isPresent()){
            return convertToDto(foundProduct.get());

        }else {
            throw new NoSuchElementException("Product not found with ID: " + productId);
        }
    }







    @Override
    public Optional<ProductDto> findProductByName(String productName) {

        Optional<Product> foundProductByName = productRepository.findByProductName(productName);
        return Optional.ofNullable(convertToDto(foundProductByName.get())) ;

//        if (foundProductByName.isPresent()){
//            return Optional.ofNullable(convertToDto(foundProductByName.get())) ;
//        }else {
//            throw new NoSuchElementException("Product not found with name: " + productName);
//        }
    }

    @Override
    public List<ProductDto> getAllProducts() {
        //DB에서 검색결과 List 저장
        List<Product> foundAllProducts = productRepository.findAll();
        //entity 값을 저장할 DTO List 객체 형성
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product foundProduct : foundAllProducts)  {
            ProductDto productDto = convertToDto(foundProduct);
            productDtos.add(productDto);
        }
        return productDtos;
    }

    /*
    검색기능
     */
    @Override
    public List<ProductDto> searchProducts(String searchKeyword) {
        //DB에서 검색결과 List 저장
        List<Product> searchedProducts = productRepository.findByProductNameContaining(searchKeyword);
        //entity 값을 저장할 DTO List 객체 형성
        if(searchedProducts.isEmpty()){
            throw new NoSuchElementException("검색 결과가 존재하지 않습니다. 검색어 :  " + searchKeyword);
        }
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product searchedProduct : searchedProducts ){
            ProductDto productDto= convertToDto(searchedProduct);
            productDtos.add(productDto);
        }
        return productDtos;
    }

    ;

//    @Override
//    public void deleteProductByName(String productName) {
//        Optional<Product> foundProduct = productRepository.findByProductName(productName);
//        if (foundProduct.isPresent()) {
//            productRepository.deleteByProductName(productName);
////            return "삭제완료";
//
//        } else {
//            throw new NoSuchElementException("Product not found with productName: " + productName);
//        }

//    }

    private ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setProductQuantity(product.getProductQuantity());
//        memberDto.setMemberEmail(member.getMemberEmail());
//        memberDto.setMemberNickName(member.getMemberNickName());
        return productDto;
    }




}
