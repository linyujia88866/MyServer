package cn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Message {

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
}