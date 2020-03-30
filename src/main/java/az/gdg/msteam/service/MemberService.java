package az.gdg.msteam.service;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<MemberDto> getAllMembers();

    String createMember(MemberDto memberDto);

    Optional<MemberEntity> findByEmail(String email);

    String deleteMember(Integer id);

    Optional<MemberEntity> findById(Integer id);

    String updateMember(Integer id, MemberDto memberDto);

    MemberDto getMemberById(Integer id);
}
