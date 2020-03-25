package az.gdg.msteam.service.impl;

import az.gdg.msteam.exception.MemberNotFoundException;
import az.gdg.msteam.mapper.MemberMapper;
import az.gdg.msteam.model.MemberResponse;
import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.MemberEntity;
import az.gdg.msteam.repository.MemberRepository;
import az.gdg.msteam.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private boolean isValid;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<MemberDto> getAllMembers() {
        List<MemberDto> members = MemberMapper.INSTANCE.entityToDtoList(memberRepository.findAll());
        if (members.size() == 0) {
            throw new MemberNotFoundException("Members couldn't be fetched");
        }
        return members;
    }

    @Override
    public String createMember(MemberDto memberDto) {
        isValid = true;
        String message = isMemberValid(memberDto);
        if (isValid && findByEmail(memberDto.getEmail()) == null) {
            memberRepository.save(MemberMapper.INSTANCE.dtoToEntity(memberDto));
            message = "Member is created";
        } else {
            message = "Email already exist";
        }
        return message;
    }

    @Override
    public MemberEntity findByEmail(String email) {
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
    public MemberEntity findById(Integer id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id: " + id));
    }

    @Override
    public String updateMember(Integer id, MemberDto memberDto) {
        MemberEntity memberEntity = findById(id);
        String message = "";
        if (memberEntity != null) {
            isValid = true;
            message = isMemberValid(memberDto);
            if (isValid) {
                memberEntity.setFirstName(memberDto.getFirstName());
                memberEntity.setLastName(memberDto.getLastName());
                memberEntity.setEmail(memberDto.getEmail());
                memberEntity.setLinkedin(memberDto.getLinkedin());
                memberEntity.setGithub(memberDto.getGithub());
                memberEntity.setPosition(memberDto.getPosition());
                memberEntity.setPhoto(memberDto.getPhoto());
                memberRepository.save(memberEntity);
                message = "Member is updated";
            }
        } else {
            message = "Member doesn't exist";
        }
        return message;
    }

    @Override
    public MemberDto getMemberById(Integer id) {
        return MemberMapper.INSTANCE.entityToDto(findById(id));
    }

    private String isMemberValid(MemberDto memberDto) {
        MemberResponse response = new MemberResponse();
        if (memberDto == null) {
            response.setMessage("Member is null");
            isValid = false;
            return response.getMessage();
        }
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
        return response.getMessage();
    }
}
