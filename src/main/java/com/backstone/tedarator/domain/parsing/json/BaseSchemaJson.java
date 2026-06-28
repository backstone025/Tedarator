package com.backstone.tedarator.domain.parsing.json;

import java.util.List;

/**
 * {@link com.backstone.tedarator.domain.parsing.entity.TargetEntity TargetEntity}에 사용되는 최종 엔티티의 메타데이터
 *
 * @param entityName   메타데이터의 주체가 되는 엔티티
 * @param entityFields 해당 엔티티의 필드 정보들
 */
public record BaseSchemaJson(
        String entityName,
        List<EntityFieldJson> entityFields
) {
}
