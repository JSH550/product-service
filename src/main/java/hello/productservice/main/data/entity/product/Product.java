package hello.productservice.main.data.entity.product;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    //컬렉션을 초기화 해주지 않으면 NullPointException발생
    private List<ProductFile> productFiles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductFile> productImages = new ArrayList<>();


    public void saveProduct(String productName, double productPrice, int productQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;

    }
}
