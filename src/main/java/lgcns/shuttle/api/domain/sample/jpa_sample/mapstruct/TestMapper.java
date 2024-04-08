package lgcns.shuttle.api.domain.sample.jpa_sample.mapstruct;

import lgcns.shuttle.api.domain.sample.jpa_sample.dto.TestDto;
import lgcns.shuttle.api.domain.sample.jpa_sample.entity.TestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestDto testEntityToDto(TestEntity testEntity);
    TestEntity testDtoToEntity(TestDto testDto);
}
