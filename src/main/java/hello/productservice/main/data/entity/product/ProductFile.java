package hello.productservice.main.data.entity.product;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ProductFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment 자동생성
//    @Column(name = "product_id")
    private Long productFileId;
    @ManyToOne
    @JoinColumn(name ="product_id")
    private Product product;
    private String productFileName;
    private String productFilePath;


    public void saveProductFile(Product product,  String productFileName, String productFilePath) {
        this.product = product;
        this.productFileName = productFileName;
        this.productFilePath = productFilePath;
    }
}
