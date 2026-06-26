package com.backstone.tedarator.domain.parsing.json;

/**
 * 외래키일 경우 참조하는 부모 엔티티에 대한 정보를 담은 메타데이터
 * <br>{@link EntityFiledJson}에서 사용된다.
 *
 * @param entityName 풀 패키지 경로 포함한 부모 엔티티 이름
 * @param pkName     참조하는 부모 엔티티 PK 필드명
 */
public record ReferenceTargetJson(
        String entityName,
        String pkName
) {
}
