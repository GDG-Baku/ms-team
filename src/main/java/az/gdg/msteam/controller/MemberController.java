package az.gdg.msteam.controller;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.service.MemberService;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "Getting all team members", response = MemberDto.class)
    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        logger.debug("Get all team members start");
        return new ResponseEntity<>(memberService.getAllMembers(), HttpStatus.OK);
    }

    @ApiOperation(value = "Adding new team member")
    @PostMapping("internal")
    public ResponseEntity<String> addMember(@RequestHeader("X-Auth-Token") String token,
                                            @RequestBody @Valid MemberDto memberDto) {
        logger.debug("Add new team member start");
        return new ResponseEntity<>(memberService.createMember(memberDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Deleting team member")
    @DeleteMapping("internal/{id}")
    public ResponseEntity<String> deleteMember(@RequestHeader("X-Auth-Token") String token,
                                               @PathVariable("id") Long id) {
        logger.debug("Delete team member by id {} start", id);
        return new ResponseEntity<>(memberService.deleteMember(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Updating team member")
    @PutMapping("internal/{id}")
    public ResponseEntity<String> updateMember(@RequestHeader("X-Auth-Token") String token,
                                               @PathVariable Long id,
                                               @RequestBody @Valid MemberDto memberDto) {
        logger.debug("Update team member by id {} start", id);
        return new ResponseEntity<>(memberService.updateMember(id, memberDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Getting team member by id")
    @GetMapping("internal/{id}")
    public ResponseEntity<MemberDto> getMemberById(@RequestHeader("X-Auth-Token") String token,
                                                   @PathVariable Long id) {
        logger.debug("Get team member by id {} start", id);
        return new ResponseEntity<>(memberService.getMemberById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Getting all team member's emails", response = String.class)
    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        logger.debug("Get all team member's emails start");
        return new ResponseEntity<>(memberService.getAllEmails(), HttpStatus.OK);
    }
}