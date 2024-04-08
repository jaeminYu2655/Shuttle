package lgcns.shuttle.api.domain.sample.jpa_sample.service;

import lgcns.shuttle.api.domain.sample.jpa_sample.repository.TestRepository;
import lgcns.shuttle.api.domain.sample.jpa_sample.dto.TestDto;
import lgcns.shuttle.api.domain.sample.jpa_sample.mapstruct.TestMapper;
import lgcns.shuttle.api.domain.sample.jpa_sample.entity.TestEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final TestMapper testMapper;

    public List<TestDto> testService() {
        List<TestEntity> testEntityList = testRepository.findByLocStatCd("10");

        return testEntityList.stream()
                .map(testMapper::testEntityToDto)
                .collect(Collectors.toList());
    }
}