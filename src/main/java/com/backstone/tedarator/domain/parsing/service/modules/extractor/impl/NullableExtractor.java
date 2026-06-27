package com.backstone.tedarator.domain.parsing.service.modules.extractor.impl;

import com.backstone.tedarator.domain.parsing.service.modules.extractor.Extractor;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.ConstraintType;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;

public class NullableExtractor implements Extractor {
    @Override
    public boolean supports(AnnotationExpr annotation, boolean isPrimary) {
        if(isPrimary){
            return true;
        }

        return "Column".equals(annotation.getNameAsString());
    }

    @Override
    public AnnotationSnapshot<?> extract(AnnotationExpr annotation, boolean isPrimary) {
        // JPA에서 기본적으로 true로 설정하기 때문이다.
        // primitive 타입의 경우, null 지정할 수 없기에 초기값을 지정해야 한다.
        boolean nullable = true;

        // 상황 1. isPrimary = true
        // @Id로 지정되었으나, @Column 지정되어있지 않을 수 있기에 바로 반환하도록 구현했다.
        if(isPrimary) {
            return new AnnotationSnapshot<>(
                    annotation.getNameAsString(),
                    ConstraintType.NULLABLE,
                    false
            );
        }

        // 상황 2. @Column(nullable = false)
        if(annotation instanceof NormalAnnotationExpr normalAnn){
            for(MemberValuePair pair : normalAnn.getPairs()){
                if(pair.getNameAsString().equals("nullable")){
                    nullable = pair.getValue().asBooleanLiteralExpr().getValue();
                }
            }
        }

        return new AnnotationSnapshot<>(
                annotation.getNameAsString(),
                ConstraintType.NULLABLE,
                nullable
        );
    }
}
