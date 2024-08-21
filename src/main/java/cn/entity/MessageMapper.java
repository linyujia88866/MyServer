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
    @Query("update Message me set me.status = ?1 where me.messageId = ?2")
    void updateStatusById(int status, Long id);

    @Query(value = "select * from messages where receiver = ?1 and status = 0", nativeQuery = true)
    List<Message> getAllNotRead(String username);

    @Query(value = "select * from messages where receiver = ?1 and status = 0 and type='system_notice'", nativeQuery = true)
    List<Message> getAllNotReadNotice(String username);

    @Query(value = "select count(*) from messages where receiver = ?1 and status = 0", nativeQuery = true)
    long countAllNotRead(String username);

    @Query(value = "select count(*) from messages where receiver = ?1 and status = 0 and type='system_notice'", nativeQuery = true)
    long countAllNotReadNotice(String username);
}