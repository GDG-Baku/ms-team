package az.gdg.msteam.controller;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.service.impl.MemberServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("members")
public class MemberController {
    private MemberServiceImpl memberService;

    public MemberController(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "Getting all team members", response = MemberDto.class)
    @GetMapping
    public ResponseEntity getAllMembers() {
        return new ResponseEntity(memberService.getAllMembers(), HttpStatus.OK);
    }
}
