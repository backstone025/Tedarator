package com.backstone.tedarator.domain.parsing.service.modules.extractor.impl;

import com.backstone.tedarator.domain.parsing.service.modules.extractor.Extractor;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.ConstraintType;
import com.github.javaparser.ast.expr.*;

import java.math.BigDecimal;

public class MaxExtractor implements Extractor {
    @Override
    public boolean supports(AnnotationExpr annotation) {
        return "Max".equals(annotation.getNameAsString());
    }

    @Override
    public AnnotationSnapshot<?> extract(AnnotationExpr annotation) {
        BigDecimal maxValue = null;

        // 경우 1 : 단일 값 (예시 @Max(10))
        if (annotation instanceof SingleMemberAnnotationExpr singleAnn){
            Expression memberValue =  singleAnn.getMemberValue();
            maxValue = parseToBigDecimal(memberValue);
        }
        // 경우 2 : 명시된 값 (예시 @Max(value = 10))
        else if (annotation instanceof NormalAnnotationExpr normalAnn) {
            for (MemberValuePair pair : normalAnn.getPairs()) {
                if ("value".equals(pair.getNameAsString())) {
                    maxValue = parseToBigDecimal(pair.getValue());
                }
            }
        }

        return new AnnotationSnapshot<>(
                annotation.getNameAsString(),
                ConstraintType.MAX_VALUE,
                maxValue
        );
    }

    private BigDecimal parseToBigDecimal(Expression expr) {
        if (expr instanceof LiteralStringValueExpr literalExpr) {
            return new BigDecimal(literalExpr.getValue());
        }
        return null;
    }
}
