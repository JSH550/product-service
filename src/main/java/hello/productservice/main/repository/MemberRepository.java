package hello.productservice.main.repository;

import hello.productservice.main.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>  {
    Optional<Member> findByMemberName(String memberName);
    Optional<Member> findByMemberEmail(String memberEmail);


}
