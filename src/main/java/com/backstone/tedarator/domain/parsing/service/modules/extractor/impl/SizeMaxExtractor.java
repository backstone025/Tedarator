package com.backstone.tedarator.domain.parsing.service.modules.extractor.impl;

import com.backstone.tedarator.domain.parsing.service.modules.extractor.Extractor;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.ConstraintType;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;

public class SizeMaxExtractor implements Extractor {
    @Override
    public boolean supports(AnnotationExpr annotation) {
        return "Size".equals(annotation.getNameAsString());
    }

    @Override
    public AnnotationSnapshot<?> extract(AnnotationExpr annotation) {
        // 상황1. @Size -> 기본 Integer.MAX_VALUE 값
        // @Size의 최대 문자열 길이는 Integer.MAX_VALUE
        Integer maxSize = Integer.MAX_VALUE;

        // 상황2. @Size(max = ?)
        if(annotation instanceof NormalAnnotationExpr normalAnn){
            for(MemberValuePair pair : normalAnn.getPairs()){
                if(pair.getNameAsString().equals("max")){
                    maxSize = pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                }
            }
        }

        return new AnnotationSnapshot<>(
                annotation.getNameAsString(),
                ConstraintType.MAX_LENGTH,
                maxSize
        );
    }
}
