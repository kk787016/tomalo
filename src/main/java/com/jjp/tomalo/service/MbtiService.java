package com.jjp.tomalo.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MbtiService {

    // 점수 정의 (파랑 > 초록 > 연두 > 노랑 > 빨강)
    private static final int B = 20; // Best (Blue - 천생연분)
    private static final int G = 16; // Good (Green - 아주 좋음)
    private static final int L = 12; // Lime (Light Green - 맞는 부분 있음)
    private static final int Y = 8;  // Yellow (최악은 아님)
    private static final int R = 4;  // Red (최악)

    // MBTI 순서 (차트의 가로/세로 순서와 동일)
    private static final String[] TYPES = {
            "INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP",
            "ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ"
    };

    private static final Map<String, Integer> TYPE_INDEX = new HashMap<>();

    // 16x16 점수 매트릭스 (이미지 차트 그대로 옮김)
    private static final int[][] SCORES = {
            //       INFP ENFP INFJ ENFJ INTJ ENTJ INTP ENTP ISFP ESFP ISTP ESTP ISFJ ESFJ ISTJ ESTJ
            /*INFP*/ {G,   G,   G,   B,   G,   B,   G,   G,   R,   R,   R,   R,   R,   R,   R,   R},
            /*ENFP*/ {G,   G,   B,   G,   B,   G,   G,   G,   R,   R,   R,   R,   R,   R,   R,   R},
            /*INFJ*/ {G,   B,   G,   G,   G,   G,   G,   B,   R,   R,   R,   R,   R,   R,   R,   R},
            /*ENFJ*/ {B,   G,   G,   G,   G,   G,   G,   G,   B,   R,   R,   R,   R,   R,   R,   R},
            /*INTJ*/ {G,   B,   G,   G,   G,   G,   G,   B,   L,   L,   L,   L,   Y,   Y,   Y,   Y},
            /*ENTJ*/ {B,   G,   G,   G,   G,   G,   B,   G,   L,   L,   L,   L,   L,   L,   L,   L},
            /*INTP*/ {G,   G,   G,   G,   G,   B,   G,   G,   L,   L,   L,   L,   Y,   Y,   Y,   B},
            /*ENTP*/ {G,   G,   B,   G,   B,   G,   G,   G,   L,   L,   L,   L,   Y,   Y,   Y,   Y},
            /*ISFP*/ {R,   R,   R,   B,   L,   L,   L,   L,   Y,   Y,   Y,   Y,   Y,   B,   Y,   B},
            /*ESFP*/ {R,   R,   R,   R,   L,   L,   L,   L,   Y,   Y,   Y,   Y,   B,   Y,   B,   Y},
            /*ISTP*/ {R,   R,   R,   R,   L,   L,   L,   L,   Y,   Y,   Y,   Y,   Y,   B,   Y,   B},
            /*ESTP*/ {R,   R,   R,   R,   L,   L,   L,   L,   Y,   Y,   Y,   Y,   B,   Y,   B,   Y},
            /*ISFJ*/ {R,   R,   R,   R,   Y,   L,   Y,   Y,   Y,   B,   Y,   B,   G,   G,   G,   G},
            /*ESFJ*/ {R,   R,   R,   R,   Y,   L,   Y,   Y,   B,   Y,   B,   Y,   G,   G,   G,   G},
            /*ISTJ*/ {R,   R,   R,   R,   Y,   L,   Y,   Y,   Y,   B,   Y,   B,   G,   G,   G,   G},
            /*ESTJ*/ {R,   R,   R,   R,   Y,   L,   B,   Y,   B,   Y,   B,   Y,   G,   G,   G,   G}
    };

    static {
        // 검색 속도를 위해 MBTI 문자열을 배열 인덱스로 매핑 (최초 1회 실행)
        for (int i = 0; i < TYPES.length; i++) {
            TYPE_INDEX.put(TYPES[i], i);
        }
    }

    // 외부에서 호출하는 메서드는 딱 이거 하나!
    public int getMbtiScore(String mbti1, String mbti2) {
        if (mbti1 == null || mbti2 == null) return 10; // 기본값

        Integer idx1 = TYPE_INDEX.get(mbti1.toUpperCase());
        Integer idx2 = TYPE_INDEX.get(mbti2.toUpperCase());

        if (idx1 == null || idx2 == null) return 10; // 잘못된 MBTI면 기본값

        return SCORES[idx1][idx2];
    }
}