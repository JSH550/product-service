package hello.productservice.main.data.entity;

import jakarta.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment 자동생성
    @Column(name = "member_id")
    private Long memberId;

}
