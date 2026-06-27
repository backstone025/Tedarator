package com.backstone.tedarator.domain.parsing.service.modules.extractor.impl;

import com.backstone.tedarator.domain.parsing.service.modules.extractor.Extractor;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.ConstraintType;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;

public class ColumnUniqueExtractor implements Extractor {
    @Override
    public boolean supports(AnnotationExpr annotation, boolean isPrimary) {
        // 상황 1. 해당 필드가 PK일 경우 무조건 true 반환
        if(isPrimary){
            return true;
        }

        // 상황 2. @Column 지정된 필드일 경우 true 반환
        return "Column".equals(annotation.getNameAsString());
    }

    @Override
    public AnnotationSnapshot<?> extract(AnnotationExpr annotation, boolean isPrimary) {
        // JPA에서 기본적으로 false로 설정하기 때문이다.
        // primitive 타입의 경우, null 지정할 수 없기에 초기값을 지정해야 한다.
        boolean isUnique = false;

        // 상황 1. isPrimary = true
        // @Id로 지정되었으나, @Column 지정되어있지 않을 수 있기에 바로 반환하도록 구현했다.
        if(isPrimary) {
            return new AnnotationSnapshot<>(
                    annotation != null ? annotation.getNameAsString() : "PrimaryKey",
                    ConstraintType.UNIQUE,
                    true
            );
        }

        // 상황 2. @Column(unique = true)
        if(annotation instanceof NormalAnnotationExpr normalAnn){
            for(MemberValuePair pair : normalAnn.getPairs()){
                if(pair.getNameAsString().equals("unique")){
                    isUnique = pair.getValue().asBooleanLiteralExpr().getValue();
                }
            }
        }

        return new AnnotationSnapshot<>(
                annotation.getNameAsString(),
                ConstraintType.UNIQUE,
                isUnique
        );
    }
}
