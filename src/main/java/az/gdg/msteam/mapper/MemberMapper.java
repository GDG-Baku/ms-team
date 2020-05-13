package az.gdg.msteam.mapper;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.model.entity.MemberEntity;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberEntity dtoToEntity(MemberDto memberDto);

    @Mapping(target = "photo", ignore = true)
    MemberDto entityToDto(MemberEntity memberEntity);

    List<MemberDto> entityToDtoList(List<MemberEntity> memberEntities);
}
