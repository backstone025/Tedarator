package com.backstone.tedarator.domain.parsing.service.modules.snapshot;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * <H1>제약 설정 타입</H1>
 * <H2>사용 시 참고 사항</H2>
 * <p>- 제약과 설정 타입 간 매핑을 통해(<b>type -> getType()</b>) 안정적인 형변환을 기대할 수 있다.</p>
 */
// TODO: 문자 형식과 관련한 제약 타입 추가할 것(이메일 타입, 문자 패턴, UUID 등)
@Getter
public enum ConstraintType {
    NULLABLE(Boolean.class),   // null 허용 여부
    UNIQUE(Boolean.class),     // unique 여부
    MIN_VALUE(BigDecimal.class),  // 최소 숫자 범위
    MAX_VALUE(BigDecimal.class),  // 최대 숫자 범위
    MIN_LENGTH(Integer.class), // 최소 문자열 길이
    MAX_LENGTH(Integer.class);  // 최대 문자열 길이

    private final Class<?> type;

    ConstraintType(Class<?> type) {
        this.type = type;
    }
}
