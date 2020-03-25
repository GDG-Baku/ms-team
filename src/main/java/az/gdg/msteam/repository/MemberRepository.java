package az.gdg.msteam.repository;

import az.gdg.msteam.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    MemberEntity findByEmail(String email);
}
