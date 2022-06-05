package org.zerock.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity     // 엔티티 클래스
@Table(name="tbl_memo") // 어떠한 테이블로 생성할 것인지
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 자동 생성
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;
}
