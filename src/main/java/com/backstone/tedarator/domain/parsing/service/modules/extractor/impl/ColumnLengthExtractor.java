package com.backstone.tedarator.domain.parsing.service.modules.extractor.impl;

import com.backstone.tedarator.domain.parsing.service.modules.extractor.Extractor;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.ConstraintType;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;

public class ColumnLengthExtractor implements Extractor {
    @Override
    public boolean supports(AnnotationExpr annotation) {
        return "Column".equals(annotation.getNameAsString());
    }

    @Override
    public AnnotationSnapshot<?> extract(AnnotationExpr annotation) {
        // @Column의 기본 길이는 255
        Integer maxLength = 255;

        // 상황 1. @Column(length = ?)
        if(annotation instanceof NormalAnnotationExpr normalAnn){
            for(MemberValuePair pair : normalAnn.getPairs()){
                if(pair.getNameAsString().equals("length")){
                    maxLength = pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                }
            }
        }

        return new AnnotationSnapshot<>(
                annotation.getNameAsString(),
                ConstraintType.MAX_LENGTH,
                maxLength
        );
    }
}
