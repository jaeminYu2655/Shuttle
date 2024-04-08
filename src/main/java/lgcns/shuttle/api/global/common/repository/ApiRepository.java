package lgcns.shuttle.api.global.common.repository;

import lgcns.shuttle.api.global.common.entity.Api;
import lgcns.shuttle.api.global.common.entity.ApiId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<Api, ApiId> {
}
