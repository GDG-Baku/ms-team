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

    private def memberService
    private def memberRepository
    private def storageClient

    void setup() {
        memberRepository = Mock(MemberRepository)
        storageClient = Mock(StorageClient)
        memberService = new MemberServiceImpl(memberRepository, storageClient)
    }

    def "should return all team members"() {
        given:
            def memberDto = new MemberDto()
            memberDto.setFirstName("")
            def mockMembers = []
            mockMembers << MemberMapper.INSTANCE.dtoToEntity(memberDto)
            memberRepository.findAll() >> mockMembers
            storageClient.getImages() >> new HashMap<String, String>()
        when:
            def members = memberService.getAllMembers()
        then:
            members.size() != 0
            notThrown(MemberNotFoundException)
    }

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

    def "should throw NoAccessException when creating member except than ROLE_ADMIN"() {
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

    def "should throw NoAccessException when deleting member except than ROLE_ADMIN"() {
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

    def "should return authenticated object"() {
        given:
            def memberAuthentication = new MemberAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
        when:
            memberService.getAuthenticatedObject()
        then:
            notThrown(InvalidTokenException)
    }

    def "should throw InvalidTokenException when non-authenticated user requests"() {
        given:
            def memberAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
        when:
            memberService.getAuthenticatedObject()
        then:
            0 * SecurityContextHolder.getContext().getAuthentication()
            thrown(InvalidTokenException)
    }

    def "should return member with given id"() {
        given:
            def memberEntity = new MemberEntity()
            def id = 1
            memberEntity.setId(id)
            def memberAuthentication = new MemberAuthentication("ROLE_ADMIN", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
            memberRepository.findById(id) >> Optional.of(memberEntity)
            storageClient.getImages() >> new HashMap<String, String>()
        when:
            memberService.getMemberById(id)
        then:
            notThrown(exception)
        where:
            exception << [NoAccessException, MemberNotFoundException]
    }

    def "should throw NoAccessException when getting member except than ROLE_ADMIN"() {
        given:
            def memberAuthentication = new MemberAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
        when:
            memberService.getMemberById(1)
        then:
            thrown(NoAccessException)
    }

    def "should throw MemberNotFoundException when no member exist with given id"() {
        given:
            def id = 1
            def memberAuthentication = new MemberAuthentication("ROLE_ADMIN", true)
            SecurityContextHolder.getContext().setAuthentication(memberAuthentication)
            memberRepository.findById(id) >> Optional.empty()
        when:
            memberService.getMemberById(id)
        then:
            thrown(MemberNotFoundException)
    }

    def "should get first photo of given member"() {
        given:
            def memberName = "asif"
            def photos = new HashMap<String, String>()
            photos.put("asif", "asifPhotoUrl")
            storageClient.getImages() >> photos
        when:
            def memberPhotos = memberService.getMemberPhotos(memberName)
        then:
            !memberPhotos.get(0).isEmpty()
            memberPhotos.get(1).isEmpty()
    }

    def "should get hovered photo of given member"() {
        given:
            def memberName = "asif"
            def photos = new HashMap<String, String>()
            photos.put("asifHover", "asifPhotoUrl")
            storageClient.getImages() >> photos
        when:
            def memberPhotos = memberService.getMemberPhotos(memberName)
        then:
            memberPhotos.get(0).isEmpty()
            !memberPhotos.get(1).isEmpty()
    }

    def cleanup() {
    }
}
