package lgcns.shuttle.api.domain.sample.jpa_sample.controller;

import io.swagger.v3.oas.annotations.Operation;
import lgcns.shuttle.api.domain.sample.jpa_sample.dto.TestDto;
import lgcns.shuttle.api.domain.sample.jpa_sample.service.TestService;
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
@RequestMapping(value = "/test")
@RequiredArgsConstructor
//@Tag(name = "예제 API", description = "Swagger 테스트용 API")
public class TestController {
    private final TestService testServiceImpl;
    @GetMapping(path="test", produces = {"application/json"})
    @Operation(summary = "test 화면", description = "test 화면을 출력합니다.", tags = {"View"})
    public ResponseEntity<List<TestDto>> testController(@RequestParam Map<String, String> testMap) {
        List<TestDto> testDtoList = testServiceImpl.testService();
        return ResponseEntity.ok(testDtoList);
    }
}
