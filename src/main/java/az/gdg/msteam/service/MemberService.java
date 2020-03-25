package az.gdg.msteam.service;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.MemberEntity;

import java.util.List;

public interface MemberService {
    List<MemberDto> getAllMembers();

    String createMember(MemberDto memberDto);

    MemberEntity findByEmail(String email);

    String deleteMember(Integer id);

    MemberEntity findById(Integer id);

    String updateMember(Integer id, MemberDto memberDto);

    MemberDto getMemberById(Integer id);
}
