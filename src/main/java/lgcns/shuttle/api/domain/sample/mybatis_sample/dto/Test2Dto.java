package lgcns.shuttle.api.domain.sample.mybatis_sample.dto;

import lgcns.shuttle.api.domain.sample.jpa_sample.entity.TestEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Test2Dto {
    private String locNo;
    private String toteBcd;
    private String locStatCd;

    public static Test2Dto toDto(TestEntity entity) {
        return builder()
                .locNo(entity.getLocNo())
                .locStatCd(entity.getLocStatCd())
                .toteBcd(entity.getToteBcd())
                .build();
    }
}
