package com.backstone.tedarator.domain.parsing.service.modules.snapshot;

/**
 * <H1>어노테이션 스냅샷</H1>
 *
 * @param annotationName 어노테이션 이름
 * @param constraintType 어노테이션이 관여하는 제약 타입
 * @param extractedValue 해당 어노테이션에서 추출된 설정값
 * @param <T>            추출된 설정값의 타입
 */
public record AnnotationSnapshot<T>(
        String annotationName,
        ConstraintType constraintType,
        T extractedValue
) {
}
