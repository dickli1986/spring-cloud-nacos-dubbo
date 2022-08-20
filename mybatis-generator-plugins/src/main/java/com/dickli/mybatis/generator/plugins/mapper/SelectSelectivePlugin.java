package com.dickli.mybatis.generator.plugins.mapper;

import com.dickli.mybatis.generator.plugins.mapper.xmlGenerator.SelectSelectiveXmlGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;

public class SelectSelectivePlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return Boolean.TRUE;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        String recordType = introspectedTable.getBaseRecordType();
        String modelName = recordType.substring(recordType.lastIndexOf(".") + 1);
        FullyQualifiedJavaType model = new FullyQualifiedJavaType(recordType);
        Method method = new Method("selectSelective");
        method.setAbstract(Boolean.TRUE);
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        method.setReturnType(new FullyQualifiedJavaType("List<"+modelName+">"));
        method.addParameter(new Parameter(model,firstCharToLowCase(modelName)));
        interfaze.addMethod(method);
        return super.clientGenerated(interfaze, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        AbstractXmlElementGenerator elementGenerator = new SelectSelectiveXmlGenerator();
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.addElements(document.getRootElement());
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private String firstCharToLowCase(String str) {
        char[] chars = new char[1];
        //String str="ABCDE1234";
        chars[0] = str.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            return str.replaceFirst(temp, temp.toLowerCase());
        }
        return str;
    }
}
