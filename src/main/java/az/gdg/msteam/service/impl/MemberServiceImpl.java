package az.gdg.msteam.service.impl;

import az.gdg.msteam.exception.MemberAlreadyExistException;
import az.gdg.msteam.exception.MemberNotFoundException;
import az.gdg.msteam.mapper.MemberMapper;
import az.gdg.msteam.model.MemberResponse;
import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.MemberEntity;
import az.gdg.msteam.repository.MemberRepository;
import az.gdg.msteam.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<MemberDto> getAllMembers() {
        LOGGER.info("ActionLog.getAllMembers.start");
        List<MemberDto> members = MemberMapper.INSTANCE.entityToDtoList(memberRepository.findAll());
        if (members.isEmpty()) {
            throw new MemberNotFoundException("No member is available");
        }
        LOGGER.info("ActionLog.getAllMembers.success");
        return members;
    }

    @Override
    public String createMember(MemberDto memberDto) {
        LOGGER.info("ActionLog.createMember.start");
        String message;
        if (!findByEmail(memberDto.getEmail()).isPresent()) {
            memberRepository.save(MemberMapper.INSTANCE.dtoToEntity(memberDto));
            LOGGER.info("ActionLog.createMember.success");
            message = "Member is created";
        } else {
            throw new MemberAlreadyExistException("Member is exist with this email");
        }
        LOGGER.info("ActionLog.createMember.end");
        return message;
    }

    @Override
    public Optional<MemberEntity> findByEmail(String email) {
        LOGGER.info("ActionLog.findByEmail.start with email {}", email);
        return memberRepository.findByEmail(email);
    }

    @Override
    public String deleteMember(Integer id) {
        LOGGER.info("ActionLog.deleteMember.start with id {}", id);
        MemberResponse response = new MemberResponse();
        if (findById(id).isPresent()) {
            memberRepository.deleteById(id);
            LOGGER.info("ActionLog.deleteMember.success with id {}", id);
            response.setMessage("Member deleted");
        } else {
            response.setMessage("Id is not available");
            throw new MemberNotFoundException("Member doesn't exist with id: " + id);
        }
        LOGGER.info("ActionLog.deleteMember.end with id {}", id);
        return response.getMessage();
    }

    @Override
    public Optional<MemberEntity> findById(Integer id) {
        LOGGER.info("ActionLog.findById.start with id {}", id);
        return memberRepository.findById(id);
    }

    @Override
    public String updateMember(Integer id, MemberDto memberDto) {
        LOGGER.info("ActionLog.updateMember.start with id {}", id);
        MemberEntity memberEntity = findById(id).orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id: " + id));
        String message = "";

        memberEntity.setFirstName(memberDto.getFirstName());
        memberEntity.setLastName(memberDto.getLastName());
        memberEntity.setEmail(memberDto.getEmail());
        memberEntity.setLinkedin(memberDto.getLinkedin());
        memberEntity.setGithub(memberDto.getGithub());
        memberEntity.setPosition(memberDto.getPosition());
        memberEntity.setPhoto(memberDto.getPhoto());

        memberRepository.save(memberEntity);

        LOGGER.info("ActionLog.updateMember.success with id {}", id);
        message = "Member is updated";
        LOGGER.info("ActionLog.updateMember.end with id {}", id);

        return message;
    }

    @Override
    public MemberDto getMemberById(Integer id) {
        LOGGER.info("ActionLog.getMemberById.start with id {}", id);
        return MemberMapper.INSTANCE.entityToDto(findById(id).orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id: " + id)));
    }

   /* private String isMemberValid(MemberDto memberDto) {
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
    }*/
}
