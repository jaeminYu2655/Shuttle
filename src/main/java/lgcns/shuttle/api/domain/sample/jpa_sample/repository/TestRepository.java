package lgcns.shuttle.api.domain.sample.jpa_sample.repository;

import lgcns.shuttle.api.domain.sample.jpa_sample.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestRepository extends JpaRepository<TestEntity, String> {
    List<TestEntity> findByLocStatCd(String locStatCd);

    @Query("Select this from TestEntity this where this.locNo=:locNo") // 쿼리문을 정의해서 쓸수도 있다.
    List<TestEntity> findByLocNo(@Param("locNo")String locNo);
}