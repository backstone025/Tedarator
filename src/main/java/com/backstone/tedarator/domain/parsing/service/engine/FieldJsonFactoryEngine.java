package com.backstone.tedarator.domain.parsing.service.engine;


import com.backstone.tedarator.domain.parsing.json.ConstraintsJson;
import com.backstone.tedarator.domain.parsing.json.EntityFieldJson;
import com.backstone.tedarator.domain.parsing.json.ReferenceTargetJson;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <H1>EntityFieldJson 팩토리 엔진</H1>
 * <H2>사용 전제</H2>
 * <p>- 한 줄에 무조건 하나의 필드만 선언한다.(int width, height -> 이렇게 작성 안됨)</p>
 */
@RequiredArgsConstructor
public class FieldJsonFactoryEngine {
    private final ExtractConstraintsEngine extractConstraintsEngine;

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
        // 모든 엔티티 메타 정보 추출 후 연관관계 분석하며 채워질 부분이댜.
        ReferenceTargetJson referenceTarget = null;

        // [제약 사항들 판별]
        // 필드에서 어노테이션 스냅샷들 수집
        List<AnnotationSnapshot<?>> snapshots = extractConstraintsEngine.collectSnapshots(field, isPrimary);

        // 수집한 수냅샷 정제처리하여 제약 사항 확립
        ConstraintsJson constraints = extractConstraintsEngine.FilteringSnapshots(snapshots);

        return new EntityFieldJson(name, type, isPrimary, isForeign, referenceTarget, constraints);
    }
}
