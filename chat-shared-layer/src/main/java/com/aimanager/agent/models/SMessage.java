package com.aimanager.agent.models;

import com.aimanager.agent.dto.VisitorResponseDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SMessage extends Message{
    private VisitorResponseDto content;
}
