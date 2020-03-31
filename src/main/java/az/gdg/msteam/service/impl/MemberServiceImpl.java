package az.gdg.msteam.service.impl;

import az.gdg.msteam.exception.MemberExistException;
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
    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<MemberDto> getAllMembers() {
        logger.info("ActionLog.getAllMembers.start");
        List<MemberDto> members = MemberMapper.INSTANCE.entityToDtoList(memberRepository.findAll());
        if (members.isEmpty()) {
            throw new MemberNotFoundException("No member is available");
        }
        logger.info("ActionLog.getAllMembers.success");
        return members;
    }

    @Override
    public String createMember(MemberDto memberDto) {
        logger.info("ActionLog.createMember.start with email {}", memberDto.getEmail());
        String message;
        if (!findByEmail(memberDto.getEmail()).isPresent()) {
            memberRepository.save(MemberMapper.INSTANCE.dtoToEntity(memberDto));
            logger.info("ActionLog.createMember.success");
            message = "Member is created";
        } else {
            throw new MemberExistException("Member is exist with this email");
        }
        logger.info("ActionLog.createMember.end");
        return message;
    }

    @Override
    public Optional<MemberEntity> findByEmail(String email) {
        logger.info("ActionLog.findByEmail.start with email {}", email);
        return memberRepository.findByEmail(email);
    }

    @Override
    public String deleteMember(Long id) {
        logger.info("ActionLog.deleteMember.start with id {}", id);
        MemberResponse response = new MemberResponse();
        if (memberRepository.findById(id).isPresent()) {
            memberRepository.deleteById(id);
            logger.info("ActionLog.deleteMember.success with id {}", id);
            response.setMessage("Member deleted");
        } else {
            response.setMessage("Id is not available");
            throw new MemberNotFoundException("Member doesn't exist with id: " + id);
        }
        logger.info("ActionLog.deleteMember.end with id {}", id);
        return response.getMessage();
    }

    @Override
    public String updateMember(Long id, MemberDto memberDto) {
        logger.info("ActionLog.updateMember.start with id {}", id);
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id: " + id));

        memberEntity.setFirstName(memberDto.getFirstName());
        memberEntity.setLastName(memberDto.getLastName());
        memberEntity.setEmail(memberDto.getEmail());
        memberEntity.setLinkedin(memberDto.getLinkedin());
        memberEntity.setGithub(memberDto.getGithub());
        memberEntity.setPosition(memberDto.getPosition());
        memberEntity.setPhoto(memberDto.getPhoto());

        String message = "";
        memberRepository.save(memberEntity);

        message = "Member is updated";
        logger.info("ActionLog.updateMember.success with id {}", id);

        return message;
    }

    @Override
    public MemberDto getMemberById(Long id) {
        logger.info("ActionLog.getMemberById.start with id {}", id);
        return MemberMapper.INSTANCE.entityToDto(memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id: " + id)));
    }
}
