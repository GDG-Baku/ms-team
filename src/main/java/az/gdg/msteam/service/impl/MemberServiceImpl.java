package az.gdg.msteam.service.impl;

import az.gdg.msteam.client.AuthenticationClient;
import az.gdg.msteam.exception.MemberExistException;
import az.gdg.msteam.exception.MemberNotFoundException;
import az.gdg.msteam.exception.NoAccessException;
import az.gdg.msteam.mapper.MemberMapper;
import az.gdg.msteam.model.ResponseMessage;
import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.dto.MemberResponseDto;
import az.gdg.msteam.model.entity.MemberEntity;
import az.gdg.msteam.repository.MemberRepository;
import az.gdg.msteam.service.MemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final MemberRepository memberRepository;
    private final AuthenticationClient authenticationClient;

    public MemberServiceImpl(MemberRepository memberRepository, AuthenticationClient authenticationClient) {
        this.memberRepository = memberRepository;
        this.authenticationClient = authenticationClient;
    }

    @Override
    public List<MemberResponseDto> getAllMembers() {
        logger.info("ActionLog.getAllMembers.start");
        List<MemberDto> members = MemberMapper.INSTANCE.entityToDtoList(memberRepository.findAll());
        if (members.isEmpty()) {
            throw new MemberNotFoundException("No member is available");
        }
        logger.info("ActionLog.getAllMembers.success");
        return dtoToResponseDto(members);
    }

    @Override
    public String createMember(MemberDto memberDto) {
        logger.info("ActionLog.createMember.start with email {}", memberDto.getEmail());
        String message;
        String role = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (role.equals("ROLE_ADMIN")) {
            if (!findByEmail(memberDto.getEmail()).isPresent()) {
                memberRepository.save(MemberMapper.INSTANCE.dtoToEntity(memberDto));
                logger.info("ActionLog.createMember.success");
                message = "Member is created";
            } else {
                throw new MemberExistException("Member is exist with this email");
            }
        } else {
            throw new NoAccessException("You don't have access for this request");
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
        String role = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage response = new ResponseMessage();
        if (role.equals("ROLE_ADMIN")) {
            if (memberRepository.findById(id).isPresent()) {
                memberRepository.deleteById(id);
                logger.info("ActionLog.deleteMember.success with id {}", id);
                response.setMessage("Member deleted");
            } else {
                response.setMessage("Id is not available");
                throw new MemberNotFoundException("Member doesn't exist with id: " + id);
            }
        } else {
            throw new NoAccessException("You don't have access for this request");
        }
        logger.info("ActionLog.deleteMember.end with id {}", id);
        return response.getMessage();
    }

    @Override
    public String updateMember(Long id, MemberDto memberDto) {
        logger.info("ActionLog.updateMember.start with id {}", id);
        String message = "";
        String role = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (role.equals("ROLE_ADMIN")) {
            MemberEntity memberEntity = memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id: " + id));

            memberEntity.setFirstName(memberDto.getFirstName());
            memberEntity.setLastName(memberDto.getLastName());
            memberEntity.setEmail(memberDto.getEmail());
            memberEntity.setLinkedin(memberDto.getLinkedin());
            memberEntity.setGithub(memberDto.getGithub());
            memberEntity.setPosition(memberDto.getPosition());
            memberEntity.setPhoto(memberDto.getPhoto());

            memberRepository.save(memberEntity);
            logger.info("ActionLog.updateMember.success with id {}", id);
            message = "Member is updated";
        } else {
            throw new NoAccessException("You don't have access for this request");
        }
        logger.info("ActionLog.updateMember.end with id {}", id);
        return message;
    }

    @Override
    public MemberDto getMemberById(Long id) {
        logger.info("ActionLog.getMemberById.start with id {}", id);
        String role = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (role.equals("ROLE_ADMIN")) {
            return MemberMapper.INSTANCE.entityToDto(memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id: " + id)));
        } else {
            throw new NoAccessException("You don't have access for this request");
        }
    }

    public List<MemberResponseDto> dtoToResponseDto(List<MemberDto> dtoList) {
        List<MemberResponseDto> responseDtoList = new ArrayList<>();
        for (MemberDto memberDto : dtoList) {
            MemberResponseDto responseDto = new MemberResponseDto();

            responseDto.setFirstName(memberDto.getFirstName());
            responseDto.setLastName(memberDto.getLastName());
            responseDto.setEmail(memberDto.getEmail());
            responseDto.setGithub(memberDto.getGithub());
            responseDto.setLinkedin(memberDto.getLinkedin());
            responseDto.setPosition(memberDto.getPosition());
            responseDto.setPhoto(getPhotoByUrl(memberDto.getPhoto()));

            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    private byte[] getPhotoByUrl(String photoUrl) {
        byte[] b = null;
        return b;
    }
}
