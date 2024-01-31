package hello.productservice.main.data.entity.product;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ProductImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment 자동생성
//    @Column(name = "product_id")
    private Long productImageId;

    @ManyToOne
    @JoinColumn(name ="product_id")
    private Product product;
    private String productImageName;
    private String productImagePath;


    public void saveProductImage(Product product,  String productImageName, String productImagePath) {
        this.product = product;
        this.productImageName = productImageName;
        this.productImagePath = productImagePath;
    }

}
