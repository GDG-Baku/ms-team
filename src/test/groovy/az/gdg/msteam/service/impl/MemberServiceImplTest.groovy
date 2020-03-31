package az.gdg.msteam.service.impl


import az.gdg.msteam.mapper.MemberMapper
import az.gdg.msteam.model.dto.MemberDto
import az.gdg.msteam.repository.MemberRepository
import spock.lang.Specification

class MemberServiceImplTest extends Specification {


    MemberServiceImpl memberService
    def memberRepository


    void setup() {
        memberRepository = Mock(MemberRepository)
        memberService = new MemberServiceImpl(memberRepository)
    }


    def "should return all team members"() {
        given:
            def memberDto1 = new MemberDto()
            memberDto1.setFirstName("Asif")
            memberDto1.setLastName("Hajiyev")
            memberDto1.setEmail("asifemail@gmail.com")
            memberDto1.setGithub("githubAccount")
            memberDto1.setLinkedin("linkedinAccount")
            memberDto1.setPosition("back-end")
            memberDto1.setPhoto("photoUrl")

            def mockMembers = []
            mockMembers << MemberMapper.INSTANCE.dtoToEntity(memberDto1)
            memberRepository.findAll() >> mockMembers
        when:
            def members = memberService.getAllMembers() // exception thrown here
        then:
            1 * memberRepository.findAll()
            members.size() == 1
    }


    /*   def "should return all team members"() {
           given:
               def memberDto1 = new MemberDto()
               memberDto1.setFirstName("Asif")
               memberDto1.setLastName("Hajiyev")
               memberDto1.setEmail("asif.hajiyev@outlook.com")
               memberDto1.setGithub("githubAccountAsif")
               memberDto1.setLinkedin("linkedinAccountAsif")
               memberDto1.setPosition("back-end")
               memberDto1.setPhoto("photoUrlAsif")

               def mockMembers = []
               mockMembers << MemberMapper.INSTANCE.dtoToEntity(memberDto1)
               memberRepository.findAll() >> mockMembers
           when:
               def members = memberService.getAllMembers()
           then:
               memberRepository.findAll()
               members.size() == 1
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
               memberDto.setFirstName("Asif")
               memberDto.setLastName("Hajiyev")
               memberDto.setEmail("asif.hajiyev@outlook.com")
               memberDto.setGithub("githubAccountAsif")
               memberDto.setLinkedin("linkedinAccountAsif")
               memberDto.setPosition("back-end")
               memberDto.setPhoto("photoUrlAsif")

               def empty = Optional.empty();
               memberRepository.findByEmail(memberDto.getEmail()) >> empty
           when:
               memberService.createMember(memberDto)
           then:
               1 * memberRepository.save(MemberMapper.INSTANCE.dtoToEntity(memberDto))
               notThrown(MemberAlreadyExistException)
       }

       def "should throw MemberExistException when trying to create new member with used email"() {
           given:
               def memberDto = new MemberDto()
               memberDto.setEmail("asif.hajiyev@outlook.com")

               def entity = Optional.of(memberDto)
               1 * memberRepository.findByEmail(memberDto.getEmail()) >> entity
           when:
               memberService.createMember(memberDto)
           then:
               thrown(MemberAlreadyExistException)
       }

       def "should call findByEmail on repository object"() {
           when:
               memberService.findByEmail("")
           then:
               1 * memberRepository.findByEmail("")
       }

       def "should delete member if exist"() {
           given:
               def member = new MemberEntity()
               member.setId(1)
               def entity = Optional.of(member)
               memberRepository.findById(member.getId()) >> entity
           when:
               memberService.deleteMember(member.getId())
           then:
               1 * memberRepository.deleteById(member.getId())
       }

       def "should throw exception when trying to delete member with non-existing id"() {
           given:
               def member = new MemberEntity()
               member.setId(1)
               def empty = Optional.empty()
               memberRepository.findById(member.getId()) >> empty
           when:
               memberService.deleteMember(member.getId())
           then:
               thrown(MemberNotFoundException)
       }

       def "should update member when it is available"() {
           given:
               def memberDto = new MemberDto()
               memberDto.setFirstName("Asif")
               memberDto.setLastName("Hajiyev")
               memberDto.setEmail("asif.hajiyev@outlook.com")
               memberDto.setGithub("githubAccountAsif")
               memberDto.setLinkedin("linkedinAccountAsif")
               memberDto.setPosition("back-end")
               memberDto.setPhoto("photoUrlAsif")
               def memberEntity = MemberMapper.INSTANCE.dtoToEntity(memberDto)
               memberEntity.setId(1)
               def entity = Optional.of(memberEntity)
               memberRepository.findById(memberEntity.getId()) >> entity
           when:
               memberService.updateMember(memberEntity.getId(), memberDto)
           then:
               1 * memberRepository.save(memberEntity)
               notThrown(MemberNotFoundException)
       }

       def "should throw MemberNotFoundException when member isn't available while updating"() {
           given:
               def memberDto = new MemberDto()
               memberDto.setFirstName("Asif")
               memberDto.setLastName("Hajiyev")
               memberDto.setEmail("asif.hajiyev@outlook.com")
               memberDto.setGithub("githubAccountAsif")
               memberDto.setLinkedin("linkedinAccountAsif")
               memberDto.setPosition("back-end")
               memberDto.setPhoto("photoUrlAsif")
               def memberEntity = MemberMapper.INSTANCE.dtoToEntity(memberDto)
               memberEntity.setId(1)
               def empty = Optional.empty()
               memberRepository.findById(memberEntity.getId()) >> empty
           when:
               memberService.updateMember(memberEntity.getId(), memberDto)
           then:
               thrown(MemberNotFoundException)
       }

       def "should return member with given id"() {
           given:
               def member = new MemberEntity()
               memberRepository.findById(member.getId()) >> Optional.of(member)
           when:
               memberService.findById(member.getId())
           then:
               1 * memberRepository.findById(member.getId())
               notThrown(MemberNotFoundException)
       }
   */

    def cleanup() {
    }
}
