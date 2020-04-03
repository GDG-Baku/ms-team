package az.gdg.msteam.controller;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.dto.MemberResponseDto;
import az.gdg.msteam.service.MemberService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "Getting all team members", response = MemberDto.class)
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        logger.debug("Get all team members start");
        return new ResponseEntity<>(memberService.getAllMembers(), HttpStatus.OK);
    }

    @ApiOperation(value = "Adding new team member")
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @PostMapping("internal")
    public ResponseEntity<String> addMember(@RequestBody @Valid MemberDto memberDto) {
        logger.debug("Add new team member start");
        return new ResponseEntity<>(memberService.createMember(memberDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Deleting team member")
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @DeleteMapping("internal/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable("id") Long id) {
        logger.debug("Get team member by id {} start", id);
        return new ResponseEntity<>(memberService.deleteMember(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Updating team member")
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @PutMapping("internal/{id}")
    public ResponseEntity<String> updateMember(@PathVariable Long id, @RequestBody @Valid MemberDto memberDto) {
        logger.debug("Update team member by id {} start", id);
        return new ResponseEntity<>(memberService.updateMember(id, memberDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Getting team member by id")
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @GetMapping("internal/{id}")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Long id) {
        logger.debug("Update team member by id {} start", id);
        return new ResponseEntity<>(memberService.getMemberById(id), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}