package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        System.out.println(memoRepository.getClass().getName());
    }

    // 더미데이터 생성 .save()
    @Test
    public void testInsertDummies(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample... " + i).build();
            memoRepository.save(memo);
        });
    }

    // 조회 작업 1) findById()
    @Test
    public void testSelect(){

        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        // Optional 객체 반환

        System.out.println("===============================");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    // 조회 작업 2) getOne()
    @Transactional  // @Transactional 어노테이션 필요
    @Test
    public void testSelect2(){

        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno); // 객체 그대로 반환

        System.out.println("=====================");

        System.out.println(memo);
    }

    // 수정 작업 save()
    @Test
    public void testUpdate(){

        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        // 내부적으로 해당 엔티티의 @Id값이 일치하는지 확인해서 insert 혹은 update 작업 처리
        System.out.println(memoRepository.save(memo));
    }

    // 삭제 작업 deleteById()
    @Test
    public void testDelete(){

        Long mno = 100L;

        memoRepository.deleteById(mno);
        // 결과는 select 이후에 delete 구문이 실행되는 방식으로 동작
    }

    /* 페이징/정렬 처리하기 */
    /*
        Pageable pageable = PageRequest.of(page, size, Sort);

        Pageable => 인터페이스
        PageRequest => 생성자가 protected인 클래스, static 메소드인 of로 생성

     */

    // 정렬 조건 없이 페이징
    @Test
    public void testPageDefault(){

        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);
        // findAll() 메소드의 리턴타입은 Page.
        // 단순히 해당 목록만을 가져오는 것이 아닌 실제 페이지 처리에 필요한 전체 데이터의 개수를 가져오는 쿼리와 함께 수행
        System.out.println(result);

        System.out.println("----------------------------------------");
        // Page가 가진 메소드들
        System.out.println("Total Pages : " + result.getTotalPages());      // 총 몇 페이지
        System.out.println("Total Count : " + result.getTotalElements());   // 전체 페이지
        System.out.println("Page Number : " + result.getNumber());          // 현재 페이지 번호 0부터 시작
        System.out.println("PAge Size : " + result.getSize());              // 페이지당 데이터 개수
        System.out.println("has next page? : " + result.hasNext());         // 다음 페이지 존재 여부
        System.out.println("first page? : " + result.isFirst());            // 시작 페이지(0) 여부

        System.out.println("------------------------------------------");

        for(Memo memo : result.getContent()) {  // List<T> : getContent()
            System.out.println(memo);           // Stream<T> : get()
        }
    }

    // 정렬 조건 추가하기
    @Test
    public void testSort() {
        // 역순 정렬 : Sort.by(필드).descending()
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);    // and를 이용한 연결

//        Pageable pageable = PageRequest.of(0, 10, sort1);
        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    // 쿼리메서드 사용
    @Test
    public void testQueryMethods(){

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for(Memo memo : list) {
            System.out.println(memo);
        }
    }
    // Pageable 사용한 쿼리메서드
    @Test
    public void testQueryMethodsWithPageable(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }

    // 쿼리메서드로 삭제 처리
    @Commit     // => @Commit, @Transactional 사용
    @Transactional // => deleteBy... 인 경우 select문으로 해당 엔티티 객체들을 가져오는 작업과 각 엔티티를 삭제하는 작업이 같이 이루어짐
    @Test
    public void testDeleteQueryMethods(){

        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}
