package com.backstone.tedarator.domain.parsing.service.policy.impl;

import com.backstone.tedarator.domain.parsing.service.policy.ConstraintsPolicy;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;

import java.util.Optional;

public class ConstraintPolicyImpl implements ConstraintsPolicy {
    @Override
    public Boolean nullablePolicy(FieldDeclaration field, boolean isPrimary) {
        Boolean nullable = null;

        // PK일 경우, 무조건 not null이다.
        if(isPrimary) {
            nullable = false;
            return nullable;
        }

        Optional<AnnotationExpr> optional = field.getAnnotationByName("Column");
        if (optional.isPresent()) {
            AnnotationExpr columnAnn = optional.get();
            // @Column(nullable = false, unique = true) 분석
            if (columnAnn instanceof NormalAnnotationExpr normalAnn) {      // 타입 겸사 후 NormalAnnotationExpr로 변환
                for (MemberValuePair pair : normalAnn.getPairs()) {         // 각 속성별 돌아가면서 nullable, unique 인지 검사
                    if ("nullable".equals(pair.getNameAsString())) {
                        nullable = pair.getValue().asBooleanLiteralExpr().getValue();
                    }
                }
            }
        }

        return nullable;
    }

    @Override
    public Boolean uniquePolicy(FieldDeclaration field, boolean isPrimary) {
        Boolean isUnique = null;

        // PK일 경우, 무조건 unique하다.
        if(isPrimary) {
            isUnique = true;
            return isUnique;
        }

        Optional<AnnotationExpr> optional = field.getAnnotationByName("Column");
        if (optional.isPresent()) {
            AnnotationExpr columnAnn = optional.get();
            if (columnAnn instanceof NormalAnnotationExpr normalAnn) {
                for (MemberValuePair pair : normalAnn.getPairs()) {
                    if ("unique".equals(pair.getNameAsString())) {
                        isUnique = pair.getValue().asBooleanLiteralExpr().getValue();
                    }
                }
            }
        }

        return  isUnique;
    }

    @Override
    public Integer minValuePolicy(FieldDeclaration field) {
        Integer minValue = null;

        Optional<AnnotationExpr> minAnnOpt = field.getAnnotationByName("Min");
        if(minAnnOpt.isPresent()) {
            AnnotationExpr minAnn = minAnnOpt.get();
            // 경우 1 : 단일 값 (예시 @Min(10))
            if (minAnn instanceof SingleMemberAnnotationExpr singleAnn){
                minValue = singleAnn.getMemberValue().asIntegerLiteralExpr().asNumber().intValue();
            }
            // 경우 2 : 명시된 값 (예시 @Min(value = 10))
            else if (minAnn instanceof NormalAnnotationExpr normalAnn) {
                for (MemberValuePair pair : normalAnn.getPairs()) {
                    if ("value".equals(pair.getNameAsString())) {
                        minValue = pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                    }
                }
            }
        }
        return minValue;
    }

    @Override
    public Integer maxValuePolicy(FieldDeclaration field) {
        Integer maxValue = null;

        Optional<AnnotationExpr> maxAnnOpt = field.getAnnotationByName("Max");
        if(maxAnnOpt.isPresent()) {
            AnnotationExpr maxAnn = maxAnnOpt.get();
            // 경우 1 : 단일 값 (예시 @Max(10))
            if (maxAnn instanceof SingleMemberAnnotationExpr singleAnn){
                maxValue = singleAnn.getMemberValue().asIntegerLiteralExpr().asNumber().intValue();
            }
            // 경우 2 : 명시된 값 (예시 @Max(value = 10))
            else if (maxAnn instanceof NormalAnnotationExpr normalAnn) {
                for (MemberValuePair pair : normalAnn.getPairs()) {
                    if ("value".equals(pair.getNameAsString())) {
                        maxValue = pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                    }
                }
            }
        }

        return maxValue;
    }

    @Override
    public Integer minLengthPolicy(FieldDeclaration field) {
        Integer minSize = null;

        Optional<AnnotationExpr> minAnnOpt = field.getAnnotationByName("Size");
        if(minAnnOpt.isPresent()) {
            AnnotationExpr minAnn = minAnnOpt.get();
            if (minAnn instanceof NormalAnnotationExpr normalAnn) {
                for (MemberValuePair pair : normalAnn.getPairs()) {
                    if ("min".equals(pair.getNameAsString())) {
                        minSize = pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                    }
                }
            }
        }
        return minSize;
    }

    @Override
    public Integer maxLengthPolicy(FieldDeclaration field) {
        Integer maxSize = null;

        Optional<AnnotationExpr> maxAnnOpt = field.getAnnotationByName("Size");
        if(maxAnnOpt.isPresent()) {
            AnnotationExpr maxAnn = maxAnnOpt.get();
            if (maxAnn instanceof NormalAnnotationExpr normalAnn) {
                for (MemberValuePair pair : normalAnn.getPairs()) {
                    if ("max".equals(pair.getNameAsString())) {
                        maxSize = pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                    }
                }
            }
        }

        return maxSize;
    }
}
