package com.tenco.blog.model.repository;

import com.tenco.blog.model.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardNativeRepository {

    //JPA 핵심 인터페이스
    // 데이터베이스와 모든 작업을 담당
    private EntityManager em;

    // 생성자를 확인해서 자동으로 EntityManager 객체를 주입 시킨다.
    // DI 처리
    public  BoardNativeRepository(EntityManager em){
        this.em = em;
    }

    public Board findById(Long id){
        // where 조건절을 활용해서 단건에 데이터를 조회
        String sqlStr = "select * from board_tb where id = ? ";
        Query query = em.createNativeQuery(sqlStr, Board.class);
        query.setParameter(1,id);
        // SQL Injection 방지 - 파라미터 바인딩
        // 직접 문자열을 연결하지 않고 ? 를 사용해서 안전하게 값 전달
        query.setParameter(1,id);

        //query.getSingleResult() -> 단일 결과만 반환하는 메서드
        // 주의: null 리턴 된다면 예외 발생 --> try -- catch 처리를 해야한다.
        // 주의: 혹시 결과가 2개 행의 리턴이 된다면 예외가 발생하게 된다.
       return (Board)query.getSingleResult();
    }

    // 게시글 목록 조회
    public List<Board> findAll(){
        // 쿼리 기술 --> 네이티브 쿼리
        Query query = em.createNativeQuery("select * from board_tb order by id desc",Board.class);

        // query.getResultList(): 여러 행의 결과를 List 객체로 반환
        // query.getSingleResult(); 단일 결과만 반환( 한 개의 row 데이만 있을 때)
        // List list = query.getResultList();
        return query.getResultList();
    }

    @Transactional
    public void save(String title, String content, String username){

        Query query = em.createNativeQuery("insert into board_tb(title, content, username, created_at)" +
                " values(?, ?, ?, now())");

        query.setParameter(1,title);
        query.setParameter(2,content);
        query.setParameter(3,username);

        query.executeUpdate();

    }


}
