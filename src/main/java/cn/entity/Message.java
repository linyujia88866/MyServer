package cn.entity;

import cn.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    /**
     * 消息的发送者
     */
    private String sender;
    /**
     * 消息的接收者
     */
    private String receiver;
    /**
     * 消息的内容
     */
    private String content;

    private Integer type = MessageType.simple_message.getCode();
    private int status = 0;


    private Date createdAt;


    private Date updatedAt;
}