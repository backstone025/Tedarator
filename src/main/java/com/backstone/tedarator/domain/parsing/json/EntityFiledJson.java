package com.backstone.tedarator.domain.parsing.json;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.Builder;

/**
 * 특정 엔티티에 대한 한 필드 세부 정보를 담은 메타데이터
 * <br> {@link ReferenceTargetJson}에서 사용된다.
 *
 * @param name            필드명
 * @param type            필드 타입
 * @param isPrimary       주키 여부
 * @param isForeign       외래키 여부
 * @param referenceTarget (왜래키일 경우) 참조 정보
 * @param constraints     제약 사항들
 */
public record EntityFiledJson(
        String name,
        String type,
        Boolean isPrimary,
        Boolean isForeign,
        ReferenceTargetJson referenceTarget,
        ConstraintsJson constraints
) {
    public static EntityFiledJson of(
            FieldDeclaration field
    ){
        VariableDeclarator variableDeclarator = field.getVariable(0);
        String name = variableDeclarator.getNameAsString();
        String type = variableDeclarator.getTypeAsString();
        Boolean isPrimary = variableDeclarator.getInitializer().isPresent();
        Boolean isForeign = false;
        ReferenceTargetJson referenceTarget = null;
        ConstraintsJson constraints = new ConstraintsJson(true, false, null, null, null, null);
        return new EntityFiledJson(name, type, isPrimary, isForeign, referenceTarget, constraints);
    }
}
