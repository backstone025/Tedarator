package com.backstone.tedarator.domain.parsing.service.engine;

import com.backstone.tedarator.domain.parsing.service.modules.extractor.Extractor;
import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExtractConstraintsEngine {
    // 생성자 주입으로 제약 추출에 사용할 Extractor 모듈 불러오기
    private final List<Extractor> extractors;

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
}