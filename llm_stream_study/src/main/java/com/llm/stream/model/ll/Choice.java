package com.llm.stream.model.ll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Choice {
    private int index;
    private String finish_reason;
    private Message message;
    private String logprobs;
}
