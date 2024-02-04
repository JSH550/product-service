package hello.productservice.main.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment 자동생성
    @Column(name = "member_id")
    private Long memberId;

    @Column(unique = true)
    private String memberEmail;

    //이름
    private String memberName;

    private String memberPassword;

//    private String memberEmail;
//
//
//    private String memberNickName;

    public void saveMember(String memberEmail,String memberName,String memberPassword){
        this.memberEmail=memberEmail;
        this.memberName=memberName;
        this.memberPassword=memberPassword;
//        this.memberEmail=memberEmail;
//        this.memberNickName=memberNickName;

    }

//    public void saveMember(String memberName, String memberEmail,String memberPassword,String memberNickName){
//        this.memberName=memberName;
//        this.memberPassword=memberPassword;
//        this.memberEmail=memberEmail;
//        this.memberNickName=memberNickName;
//
//    }

}
