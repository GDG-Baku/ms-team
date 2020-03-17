package az.gdg.msteam.controller;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.service.impl.MemberServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@RestController
@RequestMapping("members")
public class MemberController {
    private MemberServiceImpl memberService;

    public MemberController(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "Getting all team members", response = MemberDto.class)
    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        return new ResponseEntity<>(memberService.getAllMembers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addMember(@RequestBody MemberDto memberDto) {
        return new ResponseEntity<>(memberService.createMember(memberDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMember(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(memberService.deleteMember(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateMember(@PathVariable Integer id, @RequestBody MemberDto memberDto) {
        return new ResponseEntity<>(memberService.updateMember(id, memberDto), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Integer id) {
        return new ResponseEntity<>(memberService.getMemberById(id), HttpStatus.OK);
    }
}