package com.aicollab.backend.infrastructure.github.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiffChange {
    private String type;    // added / removed / context
    private String content; // 변경된 코드 라인
}
