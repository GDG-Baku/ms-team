package az.gdg.msteam.service;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.Member;

import java.util.List;

public interface MemberService {
    List<MemberDto> getAllMembers();

    String createMember(MemberDto memberDto);

    Member findByEmail(String email);

    String deleteMember(Integer id);

    Member findById(Integer id);
}
