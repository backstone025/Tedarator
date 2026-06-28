package com.backstone.tedarator.domain.parsing.service.engine;

import com.backstone.tedarator.domain.parsing.json.ConstraintsJson;
import com.backstone.tedarator.domain.parsing.service.modules.extractor.Extractor;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.ConstraintType;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ExtractConstraintsEngine {
    // 생성자 주입으로 제약 추출에 사용할 Extractor 모듈, constraints 불러오기
    private final List<Extractor> extractors;
    private final List<ConstraintType> constraints;

    /**
     * 필드로부터 어노테이션 스냅삽(제약 사항 정보) 수집하는 로직
     *
     * @param field     조사할 필드
     * @param isPrimary 해당 필드가 PK인지 여부
     * @return 해당 필드에서 조사한 어노테이션 스냅삽 리스트
     */
    public List<AnnotationSnapshot<?>> collectSnapshots(FieldDeclaration field, Boolean isPrimary) {
        List<AnnotationSnapshot<?>> snapshots = new ArrayList<>();
        // 필드에 선언된 어노테이션들 순회(N)
        for (AnnotationExpr annotation : field.getAnnotations()) {
            // 등록된 모든 Extractor 모듈들을 순회(M)
            for (Extractor extractor : extractors) {
                // 추출기가 해당 어노테이션 파싱할 수 있는가?
                if (extractor.supports(annotation) || extractor.supports(annotation, isPrimary)) {
                    // 스냅샵 추출 시도
                    AnnotationSnapshot<?> snapshot = extract(extractor, annotation, isPrimary);
                    // 최종 결과가 null아닐 경우
                    if (snapshot != null) {
                        snapshots.add(snapshot);
                    }
                }
            }
        }
        return snapshots;
    }

    private AnnotationSnapshot<?> extract(Extractor extractor, AnnotationExpr annotation, boolean isPrimary) {
        // PK 포함해 스냅샷 추출 시도
        AnnotationSnapshot<?> snapshotWithPrimary = extractor.extract(annotation, isPrimary);
        // 만일 PK포함한 추출 결과가 비어있을 경우 -> 기본 추출 시도
        if (snapshotWithPrimary == null) {
            return extractor.extract(annotation);
        }
        return snapshotWithPrimary;
    }

    /**
     * 어노테이션으로부터 추출한 제약 조건들을 정제하고 저장하기 좋은 형태로 변환하는 메소드
     *
     * @param snapshots 정제할 어노테이션 스냅샷들
     * @return 한 필드에 대한 최종 제약 조건들(JSON)
     */
    public ConstraintsJson FilteringSnapshots(List<AnnotationSnapshot<?>> snapshots) {
        // 존재하는 데이터만 ConstraintType별 동적으로 그룹핑
        Map<ConstraintType, List<AnnotationSnapshot<?>>> groupedMap = new HashMap<>();
        for (AnnotationSnapshot<?> snapshot : snapshots) {
            groupedMap.computeIfAbsent(snapshot.constraintType(), k -> new ArrayList<>()).add(snapshot);
        }

        // ConstraintJson에 넣을 임시 가변 맵
        Map<ConstraintType, Object> rawConstraints = new HashMap<>();

        // groupMap 순회
        for (Map.Entry<ConstraintType, List<AnnotationSnapshot<?>>> entry : groupedMap.entrySet()) {
            ConstraintType constraintType = entry.getKey();
            List<AnnotationSnapshot<?>> annotations = entry.getValue();
            Object decidedValue = null;

            // 1. 해당 제약에 여러 설정값이 충돌할 경우
            if (annotations.size() > 1) {
                decidedValue = castErrorSnapshot(annotations);
            }
            // 2. 충돌되지 않으며 설정값이 존재할 경우
            else {
                decidedValue = annotations.getFirst().extractedValue();
            }

            // 임시 가변 맵에 특정 제약에 대한 최종 설정값 저장
            rawConstraints.put(constraintType, decidedValue);
        }

        return new ConstraintsJson(rawConstraints);
    }

    // TODO: snapshot들 중 사용자에게 어떤 걸 쓸지 물어보고, 결정된 snapshot을 선택하도록 구현할 것
    private Object castErrorSnapshot(List<AnnotationSnapshot<?>> snapshots) {
        // 사용자에게 어떤 걸 쓸지 물어보고 결정된 snapshot 받아오기

        // 지금 당장은 맨 첫 번째로 결정
        return snapshots.getFirst().extractedValue();
    }
}