package lgcns.shuttle.api.domain.sample.jpa_sample.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TB_SHT_LOC_HDR")
@Data
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LOC_NO", nullable = false)
    private String locNo;

    @Column(name = "TOTE_BCD")
    private String toteBcd;

    @Column(name = "LOC_STAT_CD")
    private String locStatCd;
}
