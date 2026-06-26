package com.backstone.tedarator.domain.parsing.json;

/**
 * 특정 필드에 대한 제약사항을 담은 메타데이터
 * <br>{@link EntityFiledJson}에서 사용된다.
 *
 * @param nullable
 * @param isUnique
 * @param minValue
 * @param maxValue
 * @param minLength
 * @param maxLength
 */
public record ConstraintsJson(
        Boolean nullable,
        Boolean isUnique,
        Integer minValue,
        Integer maxValue,
        Integer minLength,
        Integer maxLength
) {
}
