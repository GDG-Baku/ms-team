package az.gdg.msteam.service;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<MemberDto> getAllMembers();

    String createMember(MemberDto memberDto);

    Optional<MemberEntity> findByEmail(String email);

    String deleteMember(Long id);

    String updateMember(Long id, MemberDto memberDto);

    MemberDto getMemberById(Long id);
}
