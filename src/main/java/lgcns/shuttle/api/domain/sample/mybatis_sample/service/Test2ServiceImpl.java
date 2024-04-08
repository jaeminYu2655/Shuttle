package lgcns.shuttle.api.domain.sample.mybatis_sample.service;

import lgcns.shuttle.api.domain.sample.mybatis_sample.model.Test;
import lgcns.shuttle.api.domain.sample.mybatis_sample.mapper.Test2Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class Test2ServiceImpl implements Test2Service {
    private final Test2Mapper test2Mapper;
    @Override
    public List<Test> testService() {
        return test2Mapper.selectLocHdr("10");
    }
}
