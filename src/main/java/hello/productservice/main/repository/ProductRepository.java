package hello.productservice.main.repository;

import hello.productservice.main.data.entity.product.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {
   Optional<Product> findByProductName(String productName);
   boolean existsByProductName(String productName);
//    void deleteByProductName(String productName);

//   searchKeyword를 포함하는 요소 검색
   List<Product> findByProductNameContaining(String searchKeyword, PageRequest pageRequest);
   List<Product> findByProductNameContaining(String searchKeyword);
   Long countByProductNameContaining(String searchKeyword);

}
