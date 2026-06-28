package com.backstone.tedarator.domain.parsing.service.modules.extractor;

import com.backstone.tedarator.domain.parsing.service.modules.snapshot.AnnotationSnapshot;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.Type;

/**
 * <h1>어노테이션 기반 제약 조건 추출기 (Extractor)</h1>
 * <p>각 어노테이션(예: {@code @Column}, {@code @Min} 등)으로부터 실제 데이터베이스 제약 설정값을 파싱하여
 * {@link AnnotationSnapshot} 형태로 캡슐화하는 공통 인터페이스입니다.</p>
 * <h2>@todo 구현해야 할 모듈 목록</h2>
 * <h3>숫자 범위</h3>
 * <ul>
 * <li>{@code @Positive}, {@code @PositiveOrZero}</li>
 * <li>{@code @Negative}, {@code @NegativeOrZero}</li>
 * <li>{@code @DecimalMin}, {@code @DecimalMax}</li>
 * <li>{@code @Digits}</li>
 * </ul>
 * <h3>문자 형식</h3>
 * <ul>
 * <li>{@code @Email}</li>
 * <li>{@code @Pattern}</li>
 * <li>{@code @UUID}</li>
 * </ul>
 * <h2>! 사용 전 주의 사항 !</h2>
 * <ul>
 * <li>{@code isPrimary} 파라미터는 <strong>반드시 실제 {@code @Id} 어노테이션의 존재 여부</strong>로만 판단해 넘겨야 합니다.</li>
 * <li>필드 이름이 단순히 "id"인 경우는 주키(PK) 조건 충족으로 인정하지 않으므로 주의하십시오.</li>
 * <li>{@code supports()}를 오버라이딩 할 경우, <strong>반드시 하나만 골라</strong> 사용하십시오.(어느 하나라도 true 반환하면 참으로 동작합니다.)</li>
 * <li>{@code extract()}를 오버라이딩 할 경우 <strong>반드시 하나만 골라</strong> 사용하십시오.(여러 개 구현해도 하나만 동작됩니다.)</li>
 * </ul>
 * <h2>메서드 상세 설명</h2>
 * <ul>
 * <li>{@code supports} : 현재 타겟 어노테이션 혹은 주키 조건이 해당 추출기에서 처리 가능한 대상인지 판별합니다.</li>
 * <li>{@code extract} : 타겟 어노테이션의 속성을 분석하여 규격화된 제약 설정값 스냅샷을 반환합니다.</li>
 * </ul>
 * <h2>모듈 클래스 명칭 규칙</h2>
 * <h3>[어노테이션]+[속성]+"Extractor"</h3>
 * <p>ex) {@code @Column(length = 10)}</p>
 * <p>-> {@code ColumnLengthExtractor}</p>
 */
public interface Extractor {
    default boolean supports(AnnotationExpr annotation, Type fieldType) {
        return false;
    }

    default boolean supports(AnnotationExpr annotation, boolean isPrimary, Type fieldType) {
        return false;
    }

    default AnnotationSnapshot<?> extract(AnnotationExpr annotation) {
        return null;
    }

    default AnnotationSnapshot<?> extract(AnnotationExpr annotation, boolean isPrimary, Type fieldType) {
        return null;
    }
}
