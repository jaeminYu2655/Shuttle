package lgcns.shuttle.api.domain.sample.mybatis_sample.controller;

import io.swagger.v3.oas.annotations.Operation;
import lgcns.shuttle.api.domain.sample.mybatis_sample.model.Test;
import lgcns.shuttle.api.domain.sample.mybatis_sample.service.Test2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/test2")
@RequiredArgsConstructor
//@Tag(name = "예제 API", description = "Swagger 테스트용 API")
public class Test2Controller {
    private final Test2Service test2ServiceImpl;

    @GetMapping(path="test2", produces = {"application/json"})
    @Operation(summary = "mapper/test2", description = "mapper/test2/test2 json을 출력합니다.", tags = {"View"})
    public ResponseEntity<List<Test>> test2Controller(@RequestParam Map<String, String> testMap) {
        return ResponseEntity.ok(test2ServiceImpl.testService());
    }
}
