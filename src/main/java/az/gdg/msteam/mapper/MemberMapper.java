package az.gdg.msteam.mapper;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    Member dtoToEntity(MemberDto memberDto);

    MemberDto entityToDto(Member member);

    List<MemberDto> entityToDtoList(List<Member> members);
}
