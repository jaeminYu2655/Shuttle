package lgcns.shuttle.api.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@Table(name = "TB_TEST_MEMBER_MST")
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "M_ID", unique = true)
    private Long id;

    @Setter
    @Column(nullable = false, name = "M_NICKNAME")
    private String nickname;

    @Setter
    @Column(nullable = true, name = "M_EMAIL")
    private String email;

    @Setter
    @Column(nullable = false, name = "M_PASSWORD")
    private String password;

    @Column(name = "M_AUTH")
    @Enumerated(EnumType.STRING)
    private Authority authority;
}
