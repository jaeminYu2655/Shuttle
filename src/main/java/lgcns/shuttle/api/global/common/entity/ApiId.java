package lgcns.shuttle.api.global.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ApiId implements Serializable {
    @Column(name = "URL")
    private String url;

    @Column(name = "M_AUTH")
    private String M_AUTH;
}
