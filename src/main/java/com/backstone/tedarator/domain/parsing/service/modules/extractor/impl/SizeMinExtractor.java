package com.backstone.tedarator.domain.parsing.service.modules.extractor.impl;

import com.backstone.tedarator.domain.parsing.service.modules.extractor.Extractor;
import com.backstone.tedarator.domain.parsing.service.modules.extractor.policy.CertifyNumberPolicy;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.ConstraintType;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.type.Type;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SizeMinExtractor implements Extractor {
    private final CertifyNumberPolicy certifyNumberPolicy;

    @Override
    public boolean supports(AnnotationExpr annotation, Type fieldType) {
        if (certifyNumberPolicy.isNumberType(fieldType)) {
            return false;
        }

        return "Size".equals(annotation.getNameAsString());
    }

    @Override
    public AnnotationSnapshot<?> extract(AnnotationExpr annotation) {
        // 상황1. @Size -> min = 0
        Integer minSize = 0;

        // 상황2. @Size(min = ?)
        if(annotation instanceof NormalAnnotationExpr normalAnn){
            for(MemberValuePair pair : normalAnn.getPairs()){
                if(pair.getNameAsString().equals("min")){
                    minSize = pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                }
            }
        }

        return new AnnotationSnapshot<>(
                annotation.getNameAsString(),
                ConstraintType.MIN_LENGTH,
                minSize
        );
    }
}
