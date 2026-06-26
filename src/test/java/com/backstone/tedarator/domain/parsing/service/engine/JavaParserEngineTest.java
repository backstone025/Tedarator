package com.backstone.tedarator.domain.parsing.service.engine;

import com.backstone.tedarator.domain.parsing.json.BaseSchemaJson;
import com.backstone.tedarator.domain.parsing.service.policy.ConstraintsPolicy;
import com.backstone.tedarator.domain.parsing.service.policy.impl.ConstraintPolicyImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class JavaParserEngineTest {
    private final ConstraintsPolicy constraintsPolicy = new ConstraintPolicyImpl();
    private final FieldJsonFactoryEngine fieldJsonFactoryEngine = new FieldJsonFactoryEngine(constraintsPolicy);
    private final JavaParserEngine javaParserEngine = new JavaParserEngine(fieldJsonFactoryEngine);

    @Test
    @DisplayName("1차 : dummy 폴더의 Users.java 파일을 읽어와 엔티티명과 필드를 파싱한다.")
    void parse_success_with_dummy_file() throws Exception {
        // 1. src/test/resources/dummy/Users.java 파일 지정
        File file = new File("src/test/resources/dummy/Users.java");
        try (InputStream inputStream = new FileInputStream(file)) {
            BaseSchemaJson result = javaParserEngine.parse(inputStream);

            assertThat(result.entityName()).isEqualTo("Users");

            assertThat(result.entityFields()).hasSize(7);

            System.out.println("최종 파싱 결과 객체 = " + result);
        }
    }

    @Test
    @DisplayName("2차 : 제약 사항이 잘 추출되었는지 확인한다.")
    void parse_success_with_constrains() throws Exception {
        // 1. src/test/resources/dummy/Users.java 파일 지정
        File file = new File("src/test/resources/dummy/Users.java");
        try (InputStream inputStream = new FileInputStream(file)) {
            BaseSchemaJson result = javaParserEngine.parse(inputStream);

            assertThat(result.entityName()).isEqualTo("com.backstone.tedarator.test.dummy.Users");

            System.out.println("최종 파싱 결과 객체 = " + result);
        }
    }
}