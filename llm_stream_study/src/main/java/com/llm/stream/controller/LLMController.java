package com.llm.stream.controller;

import com.llm.stream.model.ll.ChatCompletion;
import com.llm.stream.model.ll.ChatMsgReq;
import com.llm.stream.model.ll.Message;
import com.llm.stream.model.ll.OpenAIChatMessage;
import com.llm.stream.service.OpenAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/chat")
public class LLMController {

    private static final Logger logger = LoggerFactory.getLogger(LLMController.class);

    @Autowired
    private OpenAIService aiService;

    @Value("${openai.api.apiKey}")
    private String apiKey;

    @Value("${openai.api.endPoint}")
    private String endpoint;

    @Autowired
    private RestTemplate restTemplate;

//    public LLMController(OpenAIService aiService) {
//        this.aiService = aiService;
//    }

    /**
     * LLM模型的流式输出
     * @param chatMsgReq
     * @return
     */
    @PostMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLLMOutput1(@RequestBody ChatMsgReq chatMsgReq) { //, Authentication authentication
        logger.info("--------------------------");
        String message = chatMsgReq.getMessage();
        logger.info("进来啦，问题是: {}", message);
        // 可以在这里添加认证或权限检查逻辑
        // 例如：获取当前用户信息，根据需要进行权限验证等
        OpenAIChatMessage chatMessage = new OpenAIChatMessage();
        chatMessage.setModel("gpt-4");
        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message("user", message));
        chatMessage.setMessages(messageList);
        chatMessage.setStream(Boolean.FALSE);
        chatMessage.setTemperature(0.8);
        chatMessage.setFrequencyPenalty(0.8);
        chatMessage.setPresencePenalty(0.8);
        chatMessage.setMaxTokens(2048);

        logger.info("Request URL: {}", this.endpoint);
        logger.info("Authorization header: Bearer {}", this.apiKey);

        // 调用服务获取流式响应
        return aiService.chatBotStream(chatMessage);
    }


    /**
     * LLM模型的 非流式输出（一次性输出）
     * @param message
     * @return
     */
    @GetMapping(value = "/chatContent") // , produces = MediaType.TEXT_EVENT_STREAM_VALUE
    public String streamLLMOutput2(String message) { //@RequestBody OpenAIChatMessage chatMessage, , Authentication authentication
        System.out.println("进来啦，问题是："+ message);
        // 可以在这里添加认证或权限检查逻辑
        // 例如：获取当前用户信息，根据需要进行权限验证等

        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message("user", message));
        OpenAIChatMessage openAIChatMessage = new OpenAIChatMessage();
        openAIChatMessage.setModel("gpt-4");
        openAIChatMessage.setMessages(messageList);
        openAIChatMessage.setStream(Boolean.FALSE);
        openAIChatMessage.setTemperature(0.8);
        openAIChatMessage.setFrequencyPenalty(0.8);
        openAIChatMessage.setPresencePenalty(0.8);
        openAIChatMessage.setMaxTokens(2048);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", apiKey);

        HttpEntity<OpenAIChatMessage> entity = new HttpEntity<>(openAIChatMessage, headers);
        ResponseEntity<ChatCompletion> response_LLM = restTemplate.exchange(endpoint, HttpMethod.POST, entity, ChatCompletion.class);

        ChatCompletion body = response_LLM.getBody();
        String result = body.getChoices().get(0).getMessage().getContent();
        System.out.println(result);
        // 调用服务获取流式响应
//        return aiService.chatBotStream(chatMessage);
        return result;
    }
}
