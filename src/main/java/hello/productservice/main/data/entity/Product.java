package hello.productservice.main.data.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment 자동생성
    @Column(name = "product_id")
    private Long productId;

    private String productName;
    private double productPrice;
    private int productQuantity;

    public void saveProduct(String productName, double productPrice, int productQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }
}
