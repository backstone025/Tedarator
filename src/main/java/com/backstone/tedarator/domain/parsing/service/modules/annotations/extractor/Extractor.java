package com.backstone.tedarator.domain.parsing.service.modules.annotations.extractor;

import com.backstone.tedarator.domain.parsing.service.modules.annotations.snapshot.AnnotationSnapshot;
import com.github.javaparser.ast.expr.AnnotationExpr;

/**
 * <H1>어노테이션 별, 제약 설정 값 추출기</H1>
 * {@Code supports} : 타겟 어노테이션이 해당 추출기가 사용되는지 여부 판별
 * {@Code extract} : 타겟 어노테이션으로부터 추출한 값을 반환
 */
public interface Extractor {
    default boolean supports(AnnotationExpr annotation) {
        return false;
    }
    default boolean supports(AnnotationExpr annotation, boolean isPrimary) {
        return false;
    }
    AnnotationSnapshot<?> extract(AnnotationExpr annotation);
}
