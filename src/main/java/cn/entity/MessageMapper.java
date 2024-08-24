package cn.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper extends JpaRepository<Message, Long> {
    // 自定义更新方法
    @Modifying
    @Query(value = "update messages set status = ?1 where message_id = ?2", nativeQuery = true)
    void updateStatusById(Integer status, Long id);

    @Query(value = "select * from messages where message_id = ?1", nativeQuery = true)
    Message getMsgById(long msgId);

    @Query(value = "select * from messages where receiver = ?1 and (type=2 or type= 3)", nativeQuery = true)
    List<Message> getAllNotReadNotice(String username);

    @Query(value = "select count(*) from messages where receiver = ?1 and status = 0 and (type=2 or type= 3)", nativeQuery = true)
    long countAllNotReadNotice(String username);
}