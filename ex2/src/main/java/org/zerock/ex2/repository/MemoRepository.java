package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    // * 쿼리 메서드(Query Methods)

    // 1) select 작업이라면 List 타입이나 배열 리턴 가능
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // 2) 파라미터에 Pageable 사용 시 Page<T> 타입 리턴
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // 3) deleteBy로 시작하는 삭제 처리
    void deleteMemoByMnoLessThan(Long num);


    // * @Query 어노테이션
    // - @Query의 value는 JPQL(Java Persistence Query Language)로, 객체지향 쿼리라고 불린다.
    // 필요한 데이터 선별 추출, Native SQL 사용, select를 제외한 DML 처리(@Modifying과 함께 사용)
    // 객체 지향 쿼리는 테이블 대신 엔티티 클래스 이용. 칼럼 대신 필드 이용

}
