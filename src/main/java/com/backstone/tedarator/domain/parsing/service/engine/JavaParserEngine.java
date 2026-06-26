package com.backstone.tedarator.domain.parsing.service.engine;

import com.backstone.tedarator.domain.parsing.json.BaseSchemaJson;
import com.backstone.tedarator.domain.parsing.json.EntityFiledJson;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JavaParserEngine {

    public BaseSchemaJson parse(InputStream inputStream) {
        // 1. JavaParser를 사용해 소스코드를 AST(abstract syntex tree) 구조로 변환
        CompilationUnit cu = StaticJavaParser.parse(inputStream);

        // 2. 파일 내부의 클래스 선언부 가져오기
        ClassOrInterfaceDeclaration classDeclaration = cu.findFirst(ClassOrInterfaceDeclaration.class)
                .orElseThrow(() -> new RuntimeException("ClassOrInterfaceDeclaration not found"));  // 이 부분은 추후 예외처리

        // 3. 엔티티 이름 추출
        String entityName = classDeclaration.getNameAsString();

        List<EntityFiledJson> entityFields = new ArrayList<>();

        // 4. entityFields 채우기
        classDeclaration.getFields().forEach(f -> entityFields.add(EntityFiledJson.of(f)));
        return new BaseSchemaJson(entityName, entityFields);
    }
}
