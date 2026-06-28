package com.backstone.tedarator.domain.parsing.service.modules.extractor.policy;

import com.github.javaparser.ast.type.Type;
import java.util.Set;

public class CertifyStringOrEnumPolicy {

    // 자바의 대표적인 표준 참조 타입들 (문자열 길이 제약과 무관한 녀석들)
    private static final Set<String> EXCLUDED_STANDARD_TYPES = Set.of(
            "Integer", "Long", "Double", "Float", "Short", "Byte",
            "Boolean", "BigDecimal", "BigInteger",
            "LocalDateTime", "LocalDate", "LocalTime", "ZonedDateTime", "Date"
    );

    /**
     * 오직 Type 객체만 사용하여 문자열 또는 이넘 타입 여부를 추론합니다.
     */
    public boolean isStringOrEnumType(Type fieldType) {
        // 1. 자바 원시 타입(primitive)은 무조건 탈락
        if (fieldType.isPrimitiveType()) {
            return false;
        }

        // 2. 일반 클래스/인터페이스/Enum 타입인 경우
        if (fieldType.isClassOrInterfaceType()) {
            String typeName = fieldType.asClassOrInterfaceType().getNameAsString();

            // 상황 1. 순수 String
            if ("String".equals(typeName)) {
                return true;
            }

            // 상황 2. 자바 표준 타입(Long, LocalDateTime 등)에 포함되지 않을 경우
            // 일반 클래스로 포착되나, 자바 표준 타입이 아니므로 사용자 정의 Enum으로 판별
            if (!EXCLUDED_STANDARD_TYPES.contains(typeName)) {
                return true;
            }
        }

        return false;
    }
}