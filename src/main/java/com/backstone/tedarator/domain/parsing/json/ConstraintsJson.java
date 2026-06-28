package com.backstone.tedarator.domain.parsing.json;

import com.backstone.tedarator.domain.parsing.service.modules.snapshot.ConstraintType;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h1>특정 필드에 대한 제약사항을 담은 메타데이터</h1>
 * <ul>
 * <li>{@link EntityFieldJson}에서 사용된다.</li>
 * <li>{@link ConstraintType}에서 선언된 제약 조건 타입을 유동적으로 JSON으로 변환해준다.</li>
 * </ul>
 */
public record ConstraintsJson(
        Map<ConstraintType, Object> constraints
) {
    public ConstraintsJson {
        constraints = Collections.unmodifiableMap(constraints);
    }

    /**
     * 내부 제약 조건 맵을 Jackson 라이브러리 사용해 평탄화된 Map 구조로 변환시킨다.
     *
     * @return JSON (key-value) 제약 조건 명세 맵
     */
    @JsonValue  // 객체 전체가 아닌 해당 메소드가 반환하는 Map 객체 자체를 최종 JSON으로 반환
    public Map<String, Object> toSerializableMap() {
        return constraints.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue
                ));
    }

    /**
     * 타입 안정성을 보장하며 내부 constraints 맵에서 특정 제약 조건의 값을 가져오도록 한다.
     *
     * @param type 꺼내고자 하는 제약 조건 타입
     * @param <T>  ConstraintType 내부 매핑 클래스에 의해 동적으로 결정되는 반환 타입
     * @return 안전하게 현변환된 제약 조건 값(제약 조건 없을 경우: null)
     */
    @SuppressWarnings("unchecked")  // 검증되지 않는 형변환에 대한 컴파일러 경고 무시
    public <T> T get(ConstraintType type) {  // 제네릭 메소드 (<T> : 메소드 안에 사용할 가상 타입, T : 반환 타입)
        Object value = constraints.get(type);
        // 제약 조건의 지정된 Class 정보 없을 경우 null 반환
        if (value == null) {
            return null;
        }
        // ConstraintType 지정된 Class 정보 사용해 안전하게 가상 타입 T로 형변환
        return (T) type.getType().cast(value);
    }
}
