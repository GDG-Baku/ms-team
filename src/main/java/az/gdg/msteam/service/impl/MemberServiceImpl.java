package az.gdg.msteam.service.impl;

import az.gdg.msteam.client.StorageClient;
import az.gdg.msteam.exception.InvalidTokenException;
import az.gdg.msteam.exception.MemberExistException;
import az.gdg.msteam.exception.MemberNotFoundException;
import az.gdg.msteam.exception.NoAccessException;
import az.gdg.msteam.mapper.MemberMapper;
import az.gdg.msteam.model.ResponseMessage;
import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.MemberEntity;
import az.gdg.msteam.repository.MemberRepository;
import az.gdg.msteam.service.MemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    private static final String NO_ACCESS_TO_REQUEST = "You don't have access for this request";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private final MemberRepository memberRepository;
    private final StorageClient storageClient;

    public MemberServiceImpl(MemberRepository memberRepository, StorageClient storageClient) {
        this.memberRepository = memberRepository;
        this.storageClient = storageClient;
    }

    @Override
    public List<MemberDto> getAllMembers() {
        logger.info("ActionLog.getAllMembers.start");
        List<MemberDto> members = MemberMapper.INSTANCE.entityToDtoList(memberRepository.findAll());
        if (members.isEmpty()) {
            throw new MemberNotFoundException("No member is available");
        }
        Map<String, String> photos = storageClient.getImages();
        for (MemberDto memberDto : members) {
            memberDto.setPhoto(getMemberPhotos(memberDto.getFirstName(), photos));
        }
        logger.info("ActionLog.getAllMembers.success");
        return members;
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new InvalidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String createMember(MemberDto memberDto) {
        logger.info("ActionLog.createMember.start with email {}", memberDto.getEmail());
        String role = (String) getAuthenticatedObject().getPrincipal();
        String message;
        if (role.equals(ROLE_ADMIN)) {
            if (!findByEmail(memberDto.getEmail()).isPresent()) {
                MemberEntity memberEntity = MemberMapper.INSTANCE.dtoToEntity(memberDto);
                memberEntity.setPhoto(memberDto.getFirstName().toLowerCase());
                memberRepository.save(memberEntity);
                logger.info("ActionLog.createMember.success");
                message = "Member is created";
            } else {
                throw new MemberExistException("Member is exist with this email");
            }
        } else {
            throw new NoAccessException(NO_ACCESS_TO_REQUEST);
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
        ResponseMessage response = new ResponseMessage();
        String role = (String) getAuthenticatedObject().getPrincipal();
        if (role.equals(ROLE_ADMIN)) {
            if (memberRepository.findById(id).isPresent()) {
                memberRepository.deleteById(id);
                logger.info("ActionLog.deleteMember.success with id {}", id);
                response.setMessage("Member deleted");
            } else {
                response.setMessage("Id is not available");
                throw new MemberNotFoundException("Member doesn't exist with id: " + id);
            }
        } else {
            throw new NoAccessException(NO_ACCESS_TO_REQUEST);
        }
        logger.info("ActionLog.deleteMember.end with id {}", id);
        return response.getMessage();
    }

    @Override
    public String updateMember(Long id, MemberDto memberDto) {
        logger.info("ActionLog.updateMember.start with id {}", id);
        String message = "";
        String role = (String) getAuthenticatedObject().getPrincipal();
        if (role.equals(ROLE_ADMIN)) {
            MemberEntity memberEntity = memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id: " + id));
            memberMapperToUpdate(memberEntity, memberDto);
            memberRepository.save(memberEntity);
            logger.info("ActionLog.updateMember.success with id {}", id);
            message = "Member is updated";
        } else {
            throw new NoAccessException(NO_ACCESS_TO_REQUEST);
        }
        logger.info("ActionLog.updateMember.end with id {}", id);
        return message;
    }

    @Override
    public MemberDto getMemberById(Long id) {
        logger.info("ActionLog.getMemberById.start with id {}", id);
        String role = (String) getAuthenticatedObject().getPrincipal();
        if (role.equals(ROLE_ADMIN)) {
            return MemberMapper.INSTANCE.entityToDto(memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException("Member doesn't exist with this id: " + id)));
        } else {
            throw new NoAccessException(NO_ACCESS_TO_REQUEST);
        }
    }

    @Override
    public List<String> getAllEmails() {
        List<String> emails = new ArrayList<>();
        List<MemberEntity> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new MemberNotFoundException("No member is available");
        } else {
            for (MemberEntity memberEntity : members) {
                emails.add(memberEntity.getEmail());
            }
        }
        return emails;
    }

    private List<String> getMemberPhotos(String name, Map<String, String> photos) {
        List<String> memberPhotos = new ArrayList<>();
        memberPhotos.add(0, "");
        memberPhotos.add(1, "");
        for (Map.Entry<String, String> entry : photos.entrySet()) {
            if (entry.getKey().toLowerCase().startsWith(name.toLowerCase())) {
                if (!entry.getKey().toLowerCase().contains("hover")) {
                    memberPhotos.set(0, entry.getValue());
                } else {
                    memberPhotos.set(1, entry.getValue());
                }
            }
        }
        return memberPhotos;
    }

    private void memberMapperToUpdate(MemberEntity memberEntity, MemberDto memberDto) {
        memberEntity.setFirstName(memberDto.getFirstName());
        memberEntity.setLastName(memberDto.getLastName());
        memberEntity.setEmail(memberDto.getEmail());
        memberEntity.setLinkedin(memberDto.getLinkedin());
        memberEntity.setGithub(memberDto.getGithub());
        memberEntity.setPosition(memberDto.getPosition());
        memberEntity.setPhoto(memberDto.getFirstName().toLowerCase());
    }
}
