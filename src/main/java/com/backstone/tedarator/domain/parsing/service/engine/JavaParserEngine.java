package com.backstone.tedarator.domain.parsing.service.engine;

import com.backstone.tedarator.domain.parsing.json.BaseSchemaJson;
import com.backstone.tedarator.domain.parsing.json.EntityFieldJson;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JavaParserEngine {
    private final FieldJsonFactoryEngine fieldJsonFactoryEngine;

    public BaseSchemaJson parse(InputStream inputStream) {
        // 1. JavaParser를 사용해 소스코드를 AST(abstract syntex tree) 구조로 변환
        CompilationUnit cu = StaticJavaParser.parse(inputStream);

        // 2. 패키지 경로 추출 및 FQCN(Full Qualified Class Name)추출
        String packageName = cu.getPackageDeclaration()
                .map(NodeWithName::getNameAsString)
                .orElse("");

        // 3. 파일 내부의 클래스 선언부 가져오기
        ClassOrInterfaceDeclaration classDeclaration = cu.findFirst(ClassOrInterfaceDeclaration.class)
                .orElseThrow(() -> new RuntimeException("ClassOrInterfaceDeclaration not found"));  // 이 부분은 추후 예외처리

        // 4. 엔티티 이름 추출
        String entityName = packageName + "." + classDeclaration.getNameAsString();

        List<EntityFieldJson> entityFields = new ArrayList<>();

        // 5. entityFields 채우기
        classDeclaration.getFields().forEach(f -> entityFields.add(fieldJsonFactoryEngine.generateEntityFieldJson(f)));

        return new BaseSchemaJson(entityName, entityFields);
    }
}