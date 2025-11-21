package com.aicollab.backend.infrastructure.github.parser;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiffParser {

    public List<DiffChange> parse(String patch) {
        List<DiffChange> changes = new ArrayList<>();

        if (patch == null || patch.isBlank()) {
            return changes;
        }

        String[] lines = patch.split("\n");

        for (String line : lines) {
            if (line.startsWith("@@")) continue; // 헤더는 무시

            if (line.startsWith("+") && !line.startsWith("+++")) {
                changes.add(new DiffChange("added", line.substring(1)));
            } else if (line.startsWith("-") && !line.startsWith("---")) {
                changes.add(new DiffChange("removed", line.substring(1)));
            } else {
                changes.add(new DiffChange("context",line.startsWith(" ") ? line.substring(1) : line));
            }
        }

        return changes;
    }

    // LLM 입력용 버전
    public List<String> parseToLines(String patch) {
        List<String> result = new ArrayList<>();

        if (patch == null || patch.isBlank()) {
            return result;
        }

        String[] lines = patch.split("\n");

        for (String line : lines) {
            if (line.startsWith("@@")) continue;

            if (line.startsWith("+") && !line.startsWith("+++")) {
                result.add(line.substring(1));
            } else if (line.startsWith("-") && !line.startsWith("---")) {
                result.add(line.substring(1));
            } else {
                result.add(line.startsWith(" ") ? line.substring(1) : line);
            }
        }

        return result;
    }
}
