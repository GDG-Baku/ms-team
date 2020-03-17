package az.gdg.msteam.service.impl;

import az.gdg.msteam.exception.MemberNotFoundException;
import az.gdg.msteam.mapper.MemberMapper;
import az.gdg.msteam.model.MemberResponse;
import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.Member;
import az.gdg.msteam.repository.MemberRepository;
import az.gdg.msteam.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;
    private boolean isValid;

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

    @Override
    public String createMember(MemberDto memberDto) {
        isValid = true;
        String message = isMemberValid(memberDto);
        if (isValid) {
            memberRepository.save(MemberMapper.INSTANCE.dtoToEntity(memberDto));
            message = "Member is created";
        }
        return message;
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public String deleteMember(Integer id) {
        MemberResponse response = new MemberResponse();
        if (findById(id) != null) {
            memberRepository.deleteById(id);
            response.setMessage("Member deleted");
        } else {
            response.setMessage("Id is not available");
        }
        return response.getMessage();
    }

    @Override
    public Member findById(Integer id) {
        Member member = null;
        try {
            member = memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id"));
        } catch (MemberNotFoundException e) {
            e.printStackTrace();
        }
        return member;
    }


    private String isMemberValid(MemberDto memberDto) {
        MemberResponse response = new MemberResponse();
        if (memberDto.getFirstName() == null || memberDto.getFirstName().length() == 0) {
            response.setMessage("First name cannot be empty!");
            isValid = false;
            return response.getMessage();
        }
        if (memberDto.getLastName() == null || memberDto.getLastName().length() == 0) {
            response.setMessage("Last name cannot be empty!");
            isValid = false;
            return response.getMessage();
        }
        if (memberDto.getEmail() == null || memberDto.getEmail().length() == 0) {
            response.setMessage("Email cannot be empty!");
            isValid = false;
            return response.getMessage();
        }
        if (findByEmail(memberDto.getEmail()) != null) {
            response.setMessage("Email already exist");
            isValid = false;
            return response.getMessage();
        }
        return response.getMessage();
    }
}
