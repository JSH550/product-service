package hello.productservice.main.service.impl;

import hello.productservice.main.data.dto.ProductDto;
import hello.productservice.main.data.entity.Product;
import hello.productservice.main.repository.ProductRepository;
import hello.productservice.main.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    //TODO 이미지 업로드
    //TODO 제조사 정보 추가
    @Override
    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {
        boolean isProductExist = productRepository.existsByProductName(productDto.getProductName());
        log.info("ExistResult={}",isProductExist);
        if (isProductExist){
            throw new IllegalArgumentException("이미 등록된 상품명입니다. 다른 상품명을 입력해주세요");
        }

        Product product = new Product();
        product.saveProduct(productDto.getProductName(), productDto.getProductPrice(),productDto.getProductQuantity());
        productRepository.save(product);

        ProductDto savedProduct = convertToDto(product);
        return savedProduct;
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
