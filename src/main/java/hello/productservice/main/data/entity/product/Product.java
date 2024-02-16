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
    private String productManufacturer;
    private int productPrice;
//    private int productQuantity;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
//    //컬렉션을 초기화 해주지 않으면 NullPointException발생
//    private List<ProductFile> productFiles = new ArrayList<>();

    //mappedby 는 연관관계에서 mapping되었다는 의미(읽기전용)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product",  fetch = FetchType.LAZY)
    private List<ProductImage> productImages = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductCategory> productCategories = new ArrayList<>();



//    public void saveProduct(String productName,String productManufacturer, double productPrice, int productQuantity) {
//        this.productName = productName;
//        this.productManufacturer = productManufacturer;
//        this.productPrice = productPrice;
//        this.productQuantity = productQuantity;
//
//    }




    public void saveProduct(String productName,String productManufacturer, int productPrice) {
        this.productName = productName;
        this.productManufacturer = productManufacturer;
        this.productPrice = productPrice;

    }
}
