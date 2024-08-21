package cn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private String type = "simple_message";
    private int status = 0;
}