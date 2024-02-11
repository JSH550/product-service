package hello.productservice.main.service.impl;

import hello.productservice.main.data.dto.ProductDto;
import hello.productservice.main.data.entity.product.Product;
import hello.productservice.main.data.entity.product.ProductImage;
import hello.productservice.main.repository.ProductImageRepository;
import hello.productservice.main.repository.ProductRepository;
import hello.productservice.main.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Value("${file.dir}")
    private String uploadDirectory;
    //TODO 제조사 정보 추가
    @Override
    @Transactional
    public ProductDto saveProduct(ProductDto productDto,
                                  List<MultipartFile> images) throws IOException {

        //DB에서 클라이언트가 입력한 productName 있는지 조회
//        boolean isProductExist = productRepository.existsByProductName(productDto.getProductName());
//
//        log.info("ExistResult={}",isProductExist);
//
//        if (isProductExist){
//            throw new IllegalArgumentException("이미 등록된 상품명입니다. 다른 상품명을 입력해주세요");
//        }

        Product product = new Product();
        product.saveProduct(productDto.getProductName()
                ,productDto.getProductManufacturer()
                ,productDto.getProductPrice()
                ,productDto.getProductQuantity());
        Product savedProduct = productRepository.save(product);

        //이미지 저장
        if (images !=null && !images.isEmpty()){
            saveAttachedFiles(images,savedProduct);
        }

        ProductDto savedProductDto = convertToDto(product);
        return savedProductDto;
    }
    private void saveAttachedFiles(List<MultipartFile> images, Product product) throws IOException {
        for(MultipartFile file : images){

            //UUID + filename 으로 고유 filename 형성
            String savedFileName = createFileName(file);
            //지정된 directory save + 저장할 파일이름 + 확장자
            String savedFilePath = uploadDirectory + File.separator + savedFileName;
//            //file.getBytes() -> MultipartFile 객체에서 바이트 배열로 데이터를 읽어와서 byte 형태로 저장
//            //byte[]
//            byte[] fileData = file.getBytes();
//            //String 형태의 path를 Java의 Path 형식으로 변환
//            Path filePath = Paths.get(savedFilePath);
//            //file 저장
//            Files.write(filePath, fileData);
            productImageRepository.saveImage(file.getBytes(),savedFilePath);

            log.info("saveFileName={}",savedFileName);
            //productFile entity 정보 저장
            ProductImage productImage = new ProductImage();
            productImage.saveProductImage(product,savedFileName);
            product.getProductImages().add(productImage);
        }

    }
    /*
      UUID + filename으로 고유 filename 형성 메서드
     */
    private String createFileName(MultipartFile file) {
        String ext = extractExt(file);
        String originalFilename = file.getOriginalFilename();
        return UUID.randomUUID().toString() +
                "_" + originalFilename.substring(0, originalFilename.lastIndexOf(".")) +
                "."+ ext;
    }
    /*/
    filename에서 확장자(ext)추출 메서드
     */
    private String extractExt(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        //확장자는 .확장자 형태, 마지막으로 .문자 index를 변수로 추출
        int extStartPosition = originalFilename.lastIndexOf(".");
        //확장자 추출하여 반환
        if (extStartPosition >=0 ){
            return file.getOriginalFilename().substring(extStartPosition+1);
        }else{
            throw new IllegalArgumentException("파일에 확장자가 없습니다.");
        }

    }

    @Override
    public ProductDto findProductById(Long productId) {

        Optional<Product> foundProduct = productRepository.findById(productId);

        if (foundProduct.isPresent()){

            log.info("productManufacturer={}",foundProduct.get().getProductManufacturer());

            //productId에 관계된 이미지들 이름 전부 저장
            //productDto에는 List로 image 이름만 저장하자(id는 필요없음..)
            //경로는 저장하지 말자, 변경될 가능성 높음....

            //이름을 어떻게보낼건지?!
            //구현1번 imagename만저장, 컨트롤러에서 URL 만들기 복잡함..서비스계층에서 URL 만들어야함
//      ==============     메서드로 추출함========================
//            List<String> productImageNames = new ArrayList<>();
//            List<ProductImage> productImages = foundProduct.get().getProductImages();
//            for (ProductImage productImage : productImages) {
//                //저장된 image들 이름 반복 돌면서 List에 저장
//                productImageNames.add(productImage.getProductImageName());
//                log.info("imageName={}",productImage.getProductImageName());
//            }

            List<String> productImageNames = saveProductImagesToList(foundProduct);

//            //구현2번 서비스계층에서 URL 만들어서 컨트롤러로 전달
//
//            List<String> foundProductImagesPaths = new ArrayList<>();
//            List<ProductImage> productImages = foundProduct.get().getProductImages();
//
//            for (ProductImage productImage : productImages) {
//                String ImagePath= uploadDirectory + productImage.getProductImageName();
//                //저장된 image들 URL 형성하면서  List에 반복 저장
//                foundProductImagesPaths.add(ImagePath);
//                log.info("imagePath={}",ImagePath);
//            }

            //productImage 유무 확인용 OK!!!!!!!!!
//            log.info("productIamge={}",foundProduct.get().getProductImages().get(0).getProductImageName());
//            return convertToDtoWithFiles(foundProduct.get(),foundProductImagesPaths);

            return convertToDtoWithFiles(foundProduct.get(),productImageNames);


        }else {
            throw new NoSuchElementException("Product not found with ID: " + productId);
        }


        //이미지가져오기

    }

    /*/
       product에 mappin된 image들을 list에 저장
     */
    private static List<String> saveProductImagesToList(Optional<Product> foundProduct) {
        List<String> productImageNames = new ArrayList<>();
        List<ProductImage> productImages = foundProduct.get().getProductImages();
        for (ProductImage productImage : productImages) {
            //저장된 image들 이름 반복 돌면서 List에 저장
            productImageNames.add(productImage.getProductImageName());
            log.info("imageName={}",productImage.getProductImageName());
        }
        return productImageNames;
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
    //filename으로 스토리지에서 file 가져와서 return하는 기능
    public Resource getProductFileByFileName(String fileName) throws MalformedURLException {
        UrlResource resource = new UrlResource("file:"+ uploadDirectory+fileName);
        return resource;
    }

    /*/
    상품 수정기능
     */

    @Transactional
    public void updateProductById(Long productId, ProductDto productDto){
        //productDto 수정할거 넘어오면
        //DB에서 해당 제품이 실제로 있는지 조회하고
        //있으면 수정해줘
        Optional<Product> foundProduct = productRepository.findById(productId);
        boolean isProductExist = productRepository.existsByProductName(productDto.getProductName());

        if (!foundProduct.isPresent()){
            throw new IllegalArgumentException("상품이 존재하지 않습니다 다시 확인해주세요");
        }
        if (isProductExist){
            throw new IllegalArgumentException("이미 등록된 상품명입니다. 다른 상품명을 입력해주세요");
        }else {
            foundProduct.get().saveProduct(productDto.getProductName(),
                    productDto.getProductManufacturer(),
                    productDto.getProductPrice(),
                    productDto.getProductQuantity());
        }

    }


    @Override
    public List<ProductDto> getAllProducts() {
        //DB에서 검색결과 List 저장
        List<Product> foundAllProducts = productRepository.findAll();
        //entity 값을 저장할 DTO List 객체 형성
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product foundProduct : foundAllProducts)  {
//            product에 연관된 image들 이름 List에 저장
            List<String> productImages = saveProductImagesToList(Optional.of(foundProduct));
            ProductDto productDto = convertToDtoWithFiles(foundProduct,productImages);
            productDtos.add(productDto);
            log.info("productImage{}=",productImages.get(0));
        }
        return productDtos;
    }


    /*/
    클라이언트가 요청한 갯수만큼의 아이템정보 전송
     */

    public List<ProductDto> findProducts(Integer pageNumber,Integer listSize){

//        listSize가 0이거나 null 이면 4저장(default)
        if(listSize==null||listSize==0){
            listSize=4;
        }

//        몇page에 몇개의 품목을 보여줄지 객체에 저장
        PageRequest pageRequest = PageRequest.of(pageNumber, listSize);

        //DB에서 결과 조회, List에 저장
        List<Product> foundProducts = productRepository.findAll(pageRequest).getContent();

//        List에 저장
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product foundProduct : foundProducts) {
            //            product에 연관된 image들 이름 List에 저장
            List<String> productImages = saveProductImagesToList(Optional.ofNullable(foundProduct));
            ProductDto productDto = convertToDtoWithFiles(foundProduct, productImages);
            productDtos.add(productDto);
            log.info("productImage{}=",productImages.get(0));
        }
        return productDtos;
//        resturn해서 정보전송

    }


    public int calculateMaxPageSize(Integer listSize){

//        product table 의 total data 수 조회 객체에 저장
        long countTotalData =  productRepository.count();
        int maxPageSize = (int) Math.ceil(countTotalData/listSize);//나눈값을 올림처리하여 정수로 만듬

        return maxPageSize;


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

    //TODO productList 보여줄때 이미지파일1장이랑 제품이름 보여주기 기능



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
        productDto.setProductManufacturer(product.getProductManufacturer());
        productDto.setProductName(product.getProductName());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setProductQuantity(product.getProductQuantity());
//        memberDto.setMemberEmail(member.getMemberEmail());
//        memberDto.setMemberNickName(member.getMemberNickName());
        return productDto;
    }

    /*/
    첨부 file이 있는 product에서 사용, file들 이름 받아와서 Dto에 저장
     */
    private ProductDto convertToDtoWithFiles(Product product, List<String> fileNames) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setProductManufacturer(product.getProductManufacturer());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setProductQuantity(product.getProductQuantity());
        productDto.setProductImagesName(fileNames);
//        memberDto.setMemberEmail(member.getMemberEmail());
//        memberDto.setMemberNickName(member.getMemberNickName());
        return productDto;
    }




}
