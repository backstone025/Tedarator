package com.backstone.tedarator.domain.parsing.entity;

import com.backstone.tedarator.domain.parsing.json.BaseSchemaJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * 특정 프로젝트에서 뽑아낸 한 엔티티에 대한 메타데이터
 */
@Entity
@Getter
@NoArgsConstructor
public class TargetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long targetId;

    @JoinColumn
    private Long projectId;

    private String entityName;

    @JdbcTypeCode(SqlTypes.JSON)
    private BaseSchemaJson baseSchemaJson;
}
