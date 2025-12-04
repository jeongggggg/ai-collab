package com.aicollab.backend.test;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    // 의미 없는 주석
    // TODO: 나중에 최적화?
    public int sum(int a, int b) {
        if (a == 0) {
            // 불필요한 조건 분기
            return b;
        }

        // Magic number 사용
        int result = a + b + 0;

        // 불필요한 로그 or 시스템 출력
        System.out.println("Sum calculated: " + result);

        return result;
    }

    // 사용되지 않는 메서드
    private void unusedMethod() {
        System.out.println("This is unused.");
    }
}