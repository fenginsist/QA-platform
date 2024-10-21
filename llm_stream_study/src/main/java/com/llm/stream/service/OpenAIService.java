package com.llm.stream.service;

import com.llm.stream.model.ll.OpenAIChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class OpenAIService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);


    private final WebClient webClient;

    private String apiKey;

    private String endpoint;

    // 构造函数注入 WebClient.Builder 和配置值
    public OpenAIService(WebClient.Builder webClientBuilder, @Value("${openai.api.key}") String apiKey, @Value("${openai.api.endpoint}") String endpoint) {
        this.apiKey = apiKey;
        this.endpoint = endpoint;
        this.webClient = webClientBuilder
                .baseUrl(this.endpoint)
                .defaultHeader("Authorization", this.apiKey)
                .build();
    }

    /**
     * 调用 OpenAI API 获取流式响应
     *
     * @param chatMessage 聊天消息对象
     * @return Flux<String> 流式响应
     */
    public Flux<String> chatBotStream(OpenAIChatMessage chatMessage) {
        // 确保启用流式输出
        chatMessage.setStream(true);

        return webClient.post()
                //.uri(this.endpoint)     // 这里不用加了，在 构造函数中已经添加了  .baseUrl(this.endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(chatMessage)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                // 下面是新增的
                .map(response -> {
                    // 解析 OpenAI 返回的 SSE 格式数据
                    // 注意：OpenAI 的 SSE 响应通常以 data: ... 开头，最后以 data: [DONE] 表示流结束。
                    if (response.startsWith("data: ")) {
                        return response.substring(6).trim();
                    }
                    return response;
                })
                .filter(data -> !"[DONE]".equals(data))  // 过滤掉结束标志
                .map(this::parseOpenAIResponse);
    }

    /**
     * 解析 OpenAI 的响应数据
     *
     * @param data OpenAI 返回的字符串
     * @return String 解析后的内容
     */
    private String parseOpenAIResponse(String data) {
        // 根据 OpenAI 的响应格式进行解析
        // 这里只是简单返回数据，实际项目中可以解析 JSON 并提取所需信息
        logger.info("last return data: " + data);
        return data;
    }
}
