package com.aicollab.backend.infrastructure.github.parser;

import java.util.ArrayList;
import java.util.List;

public class DiffParser {

    public static List<DiffChange> parse(String patch) {
        List<DiffChange> changes = new ArrayList<>();

        if (patch == null || patch.isBlank()) {
            return changes;
        }

        String[] lines = patch.split("\n");

        for (String line : lines) {
            if (line.startsWith("@@")) {
                // 헤더는 무시
                continue;
            }

            if (line.startsWith("+") && !line.startsWith("+++")) {
                changes.add(new DiffChange("added", line.substring(1)));
            } else if (line.startsWith("-") && !line.startsWith("---")) {
                changes.add(new DiffChange("removed", line.substring(1)));
            } else {
                // context
                changes.add(new DiffChange("context",line.startsWith(" ") ? line.substring(1) : line));
            }
        }

        return changes;
    }
}
