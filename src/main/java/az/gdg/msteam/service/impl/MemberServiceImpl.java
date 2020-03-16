package az.gdg.msteam.service.impl;

import az.gdg.msteam.mapper.MemberMapper;
import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.Member;
import az.gdg.msteam.repository.MemberRepository;
import az.gdg.msteam.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<MemberDto> getAllMembers() {
        List<Member> members = new ArrayList<>();

        for (Member member : memberRepository.findAll()) {
            members.add(member);
        }

        return MemberMapper.INSTANCE.entityToDtoList(members);
    }
}
