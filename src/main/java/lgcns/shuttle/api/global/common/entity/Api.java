package lgcns.shuttle.api.global.common.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "TB_TEST_API_MST")
public class Api {
    @EmbeddedId
    private ApiId apiId;

    @Column(name = "YN")
    private String YN;
}


