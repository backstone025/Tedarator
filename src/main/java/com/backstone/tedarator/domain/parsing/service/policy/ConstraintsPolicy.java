package com.backstone.tedarator.domain.parsing.service.policy;

import com.github.javaparser.ast.body.FieldDeclaration;

public interface ConstraintsPolicy {
    /**
     * <H1>nullable 관련 정책</H1>
     * <H2>사용 전 참고 사항</H2>
     * <p>- null로 반환 될 경우, 시스템의 기본 설정 값을 따르도록 한다.(선언 X시 처리하는 방침)</p>
     *
     * @param field     조사할 field
     * @param isPrimary 해당 field가 PK인지 여부
     * @return          nullable 여부
     */
    Boolean nullablePolicy(FieldDeclaration field, boolean isPrimary);

    /**
     * <H1>unique 관련 정책</H1>
     * <H2>사용 전 참고 사항</H2>
     * <p>- null로 반환 될 경우, 시스템의 기본 설정 값을 따르도록 한다.(선언 X시 처리하는 방침)</p>
     *
     * @param field     조사할 field
     * @param isPrimary 해당 field가 PK인지 여부
     * @return          unique한지 여부
     */
    Boolean uniquePolicy(FieldDeclaration field, boolean isPrimary);

    /**
     * <H1>minValue 관련 정책</H1>
     * <H2>사용 전 참고 사항</H2>
     * <p>- 현재 정수형만 고료하고 있기에, 추후 BigDecimal사용 고려할 것</p>
     * @param field 조사할 field
     * @return      최소 값
     */
    Integer minValuePolicy(FieldDeclaration field);

    /**
     * <H1>maxValue 관련 정책</H1>
     * <H2>사용 전 참고 사항</H2>
     * <p>- 현재 정수형만 고료하고 있기에, 추후 BigDecimal사용 고려할 것</p>
     * @param field 조사할 field
     * @return      최대 값
     */
    Integer maxValuePolicy(FieldDeclaration field);

    /**
     * <H1>minLength 관련 정책</H1>
     * @param field 조사할 field
     * @return      최소 문자열 길이
     */
    Integer minLengthPolicy(FieldDeclaration field);

    /**
     * <H1>maxLength 관련 정책</H1>
     * @param field 조사할 field
     * @return      최대 문자열 길이
     */
    Integer maxLengthPolicy(FieldDeclaration field);
}
