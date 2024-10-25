package com.llm.stream.model.ll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAIChatMessage {
    // 模型名称，比如 "gpt-3.5-turbo" 或 "gpt-4"
    private String model;

    // 消息列表，包含用户和助手之间的对话历史
    private List<Message> messages;

    // 是否启用流式输出，true 表示流式传输逐步返回
    private boolean stream;

    // 控制生成内容的随机性。0.0 表示确定性，1.0 表示完全随机
    private Double temperature;

    // 控制生成单词的频率，值越高越倾向于避免重复
    private Double frequencyPenalty;

    // 控制生成内容的多样性，值越高越倾向于使用新词
    private Double presencePenalty;

    // 最大生成的token数，用来控制生成内容的长度
    private Integer maxTokens;
}
