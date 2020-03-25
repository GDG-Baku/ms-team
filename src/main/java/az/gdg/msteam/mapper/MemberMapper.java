package az.gdg.msteam.mapper;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberEntity dtoToEntity(MemberDto memberDto);

    MemberDto entityToDto(MemberEntity memberEntity);

    List<MemberDto> entityToDtoList(List<MemberEntity> memberEntities);
}
