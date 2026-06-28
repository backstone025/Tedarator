package com.backstone.tedarator.domain.parsing.service.engine;

import com.backstone.tedarator.domain.parsing.json.BaseSchemaJson;
import com.backstone.tedarator.domain.parsing.service.modules.extractor.Extractor;
import com.backstone.tedarator.domain.parsing.service.modules.extractor.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JavaParserEngineTest {
    private JavaParserEngine javaParserEngine;

    @BeforeEach
    void setUp() {
        List<Extractor> extractors = createExtractors();
        ExtractConstraintsEngine extractConstraintsEngine = new ExtractConstraintsEngine(extractors);
        FieldJsonFactoryEngine fieldJsonFactoryEngine = new FieldJsonFactoryEngine(extractConstraintsEngine);
        javaParserEngine = new JavaParserEngine(fieldJsonFactoryEngine);
    }

    private List<Extractor> createExtractors() {
        return List.of(
                // nullable
                new ColumnNullableExtractor(),
                // unique
                new ColumnUniqueExtractor(),
                // 숫자 범위
                new LengthMaxExtractor(),
                new LengthMinExtractor(),
                new MaxExtractor(),
                new MinExtractor(),
                // 문자열 길이
                new ColumnLengthExtractor(),
                new SizeMaxExtractor(),
                new SizeMinExtractor()
        );
    }

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

    @Test
    @DisplayName("3차 : 제약 사항이 잘 추출되었는지 확인한다.")
    void parse_success_with_constrains_2() throws Exception {
        // 1. src/test/resources/dummy/Users.java 파일 지정
        File file = new File("src/test/resources/dummy/Users.java");
        try (InputStream inputStream = new FileInputStream(file)) {
            BaseSchemaJson result = javaParserEngine.parse(inputStream);

            assertThat(result.entityName()).isEqualTo("com.backstone.tedarator.test.dummy.Users");

            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            // 필요 시 JavaTimeModule 등록 (LocalDateTime 파싱용)
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            System.out.println("\n=================[ PARSING RESULT ]=================\n" + prettyJson);
        }
    }
}