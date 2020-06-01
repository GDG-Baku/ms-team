package az.gdg.msteam.service.impl

import az.gdg.msteam.client.StorageClient
import az.gdg.msteam.exception.InvalidTokenException
import az.gdg.msteam.exception.MemberExistException
import az.gdg.msteam.exception.MemberNotFoundException
import az.gdg.msteam.exception.NoAccessException
import az.gdg.msteam.mapper.MemberMapper
import az.gdg.msteam.model.dto.MemberDto
import az.gdg.msteam.model.entity.MemberEntity
import az.gdg.msteam.repository.MemberRepository
import az.gdg.msteam.security.MemberAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class MemberServiceImplTest extends Specification {


    MemberServiceImpl memberService
    def memberRepository
    def storageClient


    void setup() {
        memberRepository = Mock(MemberRepository)
        storageClient = Mock(StorageClient)
        memberService = new MemberServiceImpl(memberRepository, storageClient)
    }

    /*def "should return all team members"() {
        given:
            def memberDto1 = new MemberDto()
            Map<String, String> photos = new HashMap<>()
            photos.put("asif", "asifImage")
            photos.put("asifHover", "asifHoverImage")
            List<String> memberPhotos = new ArrayList<>()
            def mockMembers = []
            mockMembers << MemberMapper.INSTANCE.dtoToEntity(memberDto1)
            memberRepository.findAll() >> mockMembers
            memberService.getMemberPhotos("asif", _ as Map<String, String>) >> memberPhotos
            memberDto1.setPhoto(memberPhotos)
        when:
            memberService.getAllMembers()
        then:
            memberRepository.findAll()
            notThrown(MemberNotFoundException)
    }*/

    def "should throw MemberNotFoundException if there is no any member"() {
        given:
            def mockMembers = []
            memberRepository.findAll() >> mockMembers
        when:
            def members = memberService.getAllMembers()
        then:
            members == null
            thrown(MemberNotFoundException)
    }

    def "should create new team member if email is unique"() {
        given:
            def memberDto = new MemberDto()
            memberDto.setFirstName("")
            MemberEntity memberEntity = MemberMapper.INSTANCE.dtoToEntity(memberDto)
            memberEntity.setPhoto("")
            def memberAuthentication = new MemberAuthentication("ROLE_ADMIN", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
            memberRepository.findByEmail(memberEntity.getEmail()) >> Optional.empty()
        when:
            memberService.createMember(memberDto)
        then:
            1 * memberRepository.save(memberEntity)
            notThrown(exception)
        where:
            exception << [NoAccessException, MemberExistException]
    }

    def "should throw NoAccessException when requesting except than ROLE_ADMIN"() {
        given:
            def memberDto = new MemberDto()
            def memberEntity = new MemberEntity()
            def memberAuthentication = new MemberAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
        when:
            memberService.createMember(memberDto)
        then:
            0 * memberRepository.save(memberEntity)
            thrown(NoAccessException)
    }

    def "should throw MemberExistException when trying to create new member with used email"() {
        given:
            def memberDto = new MemberDto()
            def memberEntity = new MemberEntity()

            def memberAuthentication = new MemberAuthentication("ROLE_ADMIN", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
            memberRepository.findByEmail(memberEntity.getEmail()) >> Optional.of(memberEntity)
        when:
            memberService.createMember(memberDto)
        then:
            0 * memberRepository.save(memberEntity)
            thrown(MemberExistException)
    }

    def "should call findByEmail on repository object"() {
        when:
            memberService.findByEmail("")
        then:
            1 * memberRepository.findByEmail("")
    }

    def "should delete member if exist"() {
        given:
            def memberEntity = new MemberEntity()
            memberEntity.setId(1)
            def memberAuthentication = new MemberAuthentication("ROLE_ADMIN", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
            memberRepository.findById(memberEntity.getId()) >> Optional.of(memberEntity)
        when:
            memberService.deleteMember(memberEntity.getId())
        then:
            1 * memberRepository.deleteById(memberEntity.getId())
            notThrown(exception)
        where:
            exception << [NoAccessException, MemberNotFoundException]
    }

    def "should throw MemberExistException when deleting member except than ROLE_ADMIN"() {
        given:
            def memberEntity = new MemberEntity()
            def memberAuthentication = new MemberAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
        when:
            memberService.deleteMember(memberEntity.getId())
        then:
            0 * memberRepository.deleteById(memberEntity.getId())
            thrown(NoAccessException)
    }

    def "should throw MemberNotFoundException when trying to delete member with non-existing id"() {
        given:
            def member = new MemberEntity()
            member.setId(1)
            def memberAuthentication = new MemberAuthentication("ROLE_ADMIN", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
            memberRepository.findById(member.getId()) >> Optional.empty()
        when:
            memberService.deleteMember(member.getId())
        then:
            thrown(MemberNotFoundException)
    }

    def "should update member when it is available"() {
        given:
            def memberDto = new MemberDto()
            memberDto.setFirstName("")
            def memberEntity = MemberMapper.INSTANCE.dtoToEntity(memberDto)
            memberEntity.setId(1)
            memberRepository.findById(memberEntity.getId()) >> Optional.of(memberEntity)
            def memberAuthentication = new MemberAuthentication("ROLE_ADMIN", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
        when:
            memberService.updateMember(memberEntity.getId(), memberDto)
        then:
            1 * memberRepository.save(memberEntity)
            notThrown(exception)
        where:
            exception << [NoAccessException, MemberNotFoundException]
    }

    def "should throw NoAccessException when updating member except than ROLE_ADMIN"() {
        given:
            def memberDto = new MemberDto()
            def memberEntity = new MemberEntity()
            memberEntity.setId(1)
            def memberAuthentication = new MemberAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
        when:
            memberService.updateMember(memberEntity.getId(), memberDto)
        then:
            0 * memberRepository.save(memberEntity)
            thrown(NoAccessException)
    }

    def "should throw MemberNotFoundException when member isn't available while updating"() {
        given:
            def memberDto = new MemberDto()
            def memberEntity = new MemberEntity()
            memberEntity.setId(1)
            def memberAuthentication = new MemberAuthentication("ROLE_ADMIN", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
            memberRepository.findById(memberEntity.getId()) >> Optional.empty()
        when:
            memberService.updateMember(memberEntity.getId(), memberDto)
        then:
            0 * memberRepository.save(memberEntity)
            thrown(MemberNotFoundException)
    }


    def "should get authenticated object"() {
        given:
            def memberAuthentication = new MemberAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
        when:
            memberService.getAuthenticatedObject()
        then:
            notThrown(InvalidTokenException)
    }

    def "should throw InvalidTokenException when calling getAuthenticatedObject method"() {
        given:
            def memberAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
        when:
            memberService.getAuthenticatedObject()
        then:
            0 * SecurityContextHolder.getContext().getAuthentication()
            thrown(InvalidTokenException)

    }

    def cleanup() {
    }
}
