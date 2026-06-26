package com.backstone.tedarator.domain.parsing.service.engine;


import com.backstone.tedarator.domain.parsing.json.ConstraintsJson;
import com.backstone.tedarator.domain.parsing.json.EntityFieldJson;
import com.backstone.tedarator.domain.parsing.json.ReferenceTargetJson;
import com.backstone.tedarator.domain.parsing.service.policy.ConstraintsPolicy;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.RequiredArgsConstructor;

/**
 * <H1>EntityFieldJson 팩토리 엔진</H1>
 * <H2>사용 전제</H2>
 * <p>- 한 줄에 무조건 하나의 필드만 선언한다.(int width, height -> 이렇게 작성 안됨)</p>
 * <p>- min, max value는 <b>@Min</b>, <b>@Max</b>만 고려한다.</p>
 * <p>- min, max value는 정수형만 다루는걸 고려한다.</p>
 * <p>- min, max length는<b>@Size</b>만 고려한다. </p>
 */
@RequiredArgsConstructor
public class FieldJsonFactoryEngine {
    final ConstraintsPolicy constraintsPolicy;

    public EntityFieldJson generateEntityFieldJson(FieldDeclaration field) {
        VariableDeclarator variableDeclarator = field.getVariable(0);
        String name = variableDeclarator.getNameAsString();
        String type = variableDeclarator.getTypeAsString();

        // [주키(PK) 판별] : Id(주키), EmbeddedId(복합키)
        Boolean isPrimary = field.isAnnotationPresent("Id") || field.isAnnotationPresent("EmbeddedId");

        // [외래키(FK) 판별] : JPA 연관관계 어노테이션 존재하는지 판별
        Boolean isForeign = field.isAnnotationPresent("ManyToOne")
                || field.isAnnotationPresent("OneToOne")
                || field.isAnnotationPresent("JoinColumn");

        // 참조 세부 정보는 JavaParserEngine에서 처리한다.
        ReferenceTargetJson referenceTarget = null;

        // [제약 사항들 판별]
        // nullable
        Boolean nullable = constraintsPolicy.nullablePolicy(field, isPrimary);

        // unique
        Boolean isUnique = constraintsPolicy.uniquePolicy(field, isPrimary);

        // min, max value
        Integer minValue = constraintsPolicy.minValuePolicy(field);
        Integer maxValue = constraintsPolicy.maxValuePolicy(field);

        // min, max length
        Integer minLength = constraintsPolicy.minLengthPolicy(field);
        Integer maxLength = constraintsPolicy.maxLengthPolicy(field);

        // 제약 사항 묶기
        ConstraintsJson constraints = new ConstraintsJson(nullable, isUnique, minValue, maxValue, minLength, maxLength);

        return new EntityFieldJson(name, type, isPrimary, isForeign, referenceTarget, constraints);
    }
}
