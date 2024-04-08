package lgcns.shuttle.api.domain.sample.mybatis_sample.mapper;

import lgcns.shuttle.api.domain.sample.mybatis_sample.model.Test;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Mapper
public interface Test2Mapper {
    List<Test> selectLocHdr(@Param("locStatCd")String locStatCd);
}