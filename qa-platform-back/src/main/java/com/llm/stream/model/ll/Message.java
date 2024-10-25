package com.llm.stream.model.ll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    // 消息发送者的角色，可以是 "user", "assistant", "system"
    private String role;

    // 消息的内容，表示发送的文本内容
    private String content;
}
