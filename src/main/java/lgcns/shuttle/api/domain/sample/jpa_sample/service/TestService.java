package lgcns.shuttle.api.domain.sample.jpa_sample.service;

import lgcns.shuttle.api.domain.sample.jpa_sample.dto.TestDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface TestService {
    List<TestDto> testService();
}
