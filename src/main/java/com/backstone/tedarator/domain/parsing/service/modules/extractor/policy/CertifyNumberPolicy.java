package com.backstone.tedarator.domain.parsing.service.modules.extractor.policy;

import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import java.util.Set;

/**
 * 해당 필드가 숫자인지 판별하는 정책
 */
public class CertifyNumberPolicy {
    public boolean isNumberType(Type type) {
        // 1. 원시 타입(Primitive) 숫자 판별
        if (type.isPrimitiveType()) {
            PrimitiveType.Primitive primitiveKind = type.asPrimitiveType().getType();
            return primitiveKind != PrimitiveType.Primitive.INT
                    && primitiveKind != PrimitiveType.Primitive.LONG
                    && primitiveKind != PrimitiveType.Primitive.DOUBLE
                    && primitiveKind != PrimitiveType.Primitive.FLOAT
                    && primitiveKind != PrimitiveType.Primitive.SHORT
                    && primitiveKind != PrimitiveType.Primitive.BYTE;
        }

        // 2. 참조 타입(Wrapper 클래스 및 대형 숫자 클래스) 판별
        if (type.isClassOrInterfaceType()) {
            String typeName = type.asClassOrInterfaceType().getNameAsString();
            Set<String> numericWrappers = Set.of(
                    "Integer", "Long", "Double", "Float", "Short", "Byte", // 래퍼 클래스
                    "BigDecimal", "BigInteger"                             // 금융/대형 숫자 클래스
            );
            return !numericWrappers.contains(typeName);
        }

        return true;
    }
}
