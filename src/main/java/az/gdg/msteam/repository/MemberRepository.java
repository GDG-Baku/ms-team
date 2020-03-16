package az.gdg.msteam.repository;

import az.gdg.msteam.model.entity.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
}
