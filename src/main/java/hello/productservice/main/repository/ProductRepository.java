package hello.productservice.main.repository;

import hello.productservice.main.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {
   Optional<Product> findByProductName(String productName);
   boolean existsByProductName(String productName);
//    void deleteByProductName(String productName);
   List<Product> findByProductNameContaining(String searchKeyword);

}
