package com.dickli.mybatis.generator.plugins.service;

import com.dickli.mybatis.generator.plugins.service.serviceEnum.ServiceMethodEnum;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.ArrayList;
import java.util.List;

public class ServicePlugin extends PluginAdapter {
    // 项目目录，一般为 src/main/java
    private String targetProject;

    // service包名，如：com.thinkj2ee.cms.service.service
    private String servicePackage;

    // service实现类包名，如：com.thinkj2ee.cms.service.service.impl
    private String serviceImplPackage;

    // service接口名前缀
    private String servicePreffix;

    // service接口名后缀
    private String serviceSuffix;

    // service接口的父接口
    private String superServiceInterface;

    // service实现类的父类
    private String superServiceImpl;

    private String recordType;

    private String modelName;

    private FullyQualifiedJavaType model;

    private String serviceName;
    private String serviceImplName;

    @Override
    public boolean validate(List<String> warnings) {
        boolean valid = true;

        if (!StringUtility.stringHasValue(properties
                .getProperty("targetProject"))) { //$NON-NLS-1$
            warnings.add(Messages.getString("ValidationError.18", //$NON-NLS-1$
                    "MapperConfigPlugin", //$NON-NLS-1$
                    "targetProject")); //$NON-NLS-1$
            valid = false;
        }

        if (!StringUtility.stringHasValue(properties.getProperty("servicePackage"))) { //$NON-NLS-1$
            warnings.add(Messages.getString("ValidationError.18", //$NON-NLS-1$
                    "MapperConfigPlugin", //$NON-NLS-1$
                    "servicePackage")); //$NON-NLS-1$
            valid = false;
        }

        if (!StringUtility.stringHasValue(properties.getProperty("serviceImplPackage"))) { //$NON-NLS-1$
            warnings.add(Messages.getString("ValidationError.18", //$NON-NLS-1$
                    "MapperConfigPlugin", //$NON-NLS-1$
                    "serviceImplPackage")); //$NON-NLS-1$
            valid = false;
        }

        targetProject = properties.getProperty("targetProject");
        servicePackage = properties.getProperty("servicePackage");
        serviceImplPackage = properties.getProperty("serviceImplPackage");
        servicePreffix = properties.getProperty("servicePreffix");
        servicePreffix = StringUtility.stringHasValue(servicePreffix) ? servicePreffix : "";
        serviceSuffix = properties.getProperty("serviceSuffix");
        serviceSuffix = StringUtility.stringHasValue(serviceSuffix) ? serviceSuffix : "";
        superServiceInterface = properties.getProperty("superServiceInterface");
        superServiceImpl = properties.getProperty("superServiceImpl");

        return valid;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> answer = new ArrayList<>();
        String modelOnly = introspectedTable.getTableConfigurationProperty("modelOnly");
        if ("true".equals(modelOnly)) {
            return answer;
        }
        recordType = introspectedTable.getBaseRecordType();
        modelName = recordType.substring(recordType.lastIndexOf(".") + 1);
        model = new FullyQualifiedJavaType(recordType);
        // com.thinkj2ee.cms.service.IMemberService
        serviceName = servicePackage + "." + servicePreffix + modelName + serviceSuffix;
        // com.thinkj2ee.cms.service.impl.MemberService
        serviceImplName = serviceImplPackage + "." + modelName + serviceSuffix + "Impl";

        GeneratedJavaFile gjf = generateServiceInterface(introspectedTable);
        GeneratedJavaFile gjf2 = generateServiceImpl(introspectedTable);
        answer.add(gjf);
        answer.add(gjf2);
        return answer;
    }

    // 生成service接口
    private GeneratedJavaFile generateServiceInterface(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);
        Interface serviceInterface = new Interface(service);
        serviceInterface.setVisibility(JavaVisibility.PUBLIC);
        // 添加父接口
        if (StringUtility.stringHasValue(superServiceInterface)) {
            String superServiceInterfaceName = superServiceInterface.substring(superServiceInterface.lastIndexOf(".") + 1);
            serviceInterface.addImportedType(new FullyQualifiedJavaType(superServiceInterface));
            serviceInterface.addImportedType(new FullyQualifiedJavaType(recordType));
            serviceInterface.addSuperInterface(new FullyQualifiedJavaType(superServiceInterfaceName + "<" + modelName + ">"));
        }

        serviceInterface.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        serviceInterface.addImportedType(new FullyQualifiedJavaType("java.util.List"));

        for (ServiceMethodEnum serviceMethodEnum : ServiceMethodEnum.values()
        ) {
            addMethodToService(serviceInterface, introspectedTable, serviceMethodEnum);
        }

        GeneratedJavaFile gjf = new GeneratedJavaFile(serviceInterface, targetProject, context.getJavaFormatter());
        return gjf;
    }

    // 生成service实现类
    private GeneratedJavaFile generateServiceImpl(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);
        FullyQualifiedJavaType serviceImpl = new FullyQualifiedJavaType(serviceImplName);
        TopLevelClass clazz = new TopLevelClass(serviceImpl);
        clazz.setVisibility(JavaVisibility.PUBLIC);
        clazz.addImportedType(service);
        clazz.addSuperInterface(service);
        if (StringUtility.stringHasValue(superServiceImpl)) {
            String superServiceImplName = superServiceImpl.substring(superServiceImpl.lastIndexOf(".") + 1);
            clazz.addImportedType(superServiceImpl);
            clazz.addImportedType(recordType);
            clazz.setSuperClass(superServiceImplName + "<" + modelName + ">");
        }
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        clazz.addAnnotation("@Service");
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional"));
        clazz.addAnnotation("@Transactional");
        clazz.addImportedType(new FullyQualifiedJavaType("lombok.extern.slf4j.Slf4j"));
        clazz.addAnnotation("@Slf4j");

        String daoFieldType = introspectedTable.getMyBatis3JavaMapperType();
        String daoFieldName = firstCharToLowCase(daoFieldType.substring(daoFieldType.lastIndexOf(".") + 1));
        Field daoField = new Field(daoFieldName, new FullyQualifiedJavaType(daoFieldType));
        clazz.addImportedType(new FullyQualifiedJavaType(daoFieldType));
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        daoField.addAnnotation("@Autowired");
        daoField.setVisibility(JavaVisibility.PRIVATE);
        clazz.addField(daoField);


        clazz.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        clazz.addImportedType(new FullyQualifiedJavaType("java.util.List"));

        for (ServiceMethodEnum serviceMethodEnum : ServiceMethodEnum.values()
        ) {
            addMethodToServiceImpl(clazz, introspectedTable, serviceMethodEnum, daoFieldName);
        }

        GeneratedJavaFile gjf2 = new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
        return gjf2;
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

    private void addMethodToService(Interface serviceInterface, IntrospectedTable introspectedTable, ServiceMethodEnum methodEnum) {
        IntrospectedColumn primaryKey = introspectedTable.getPrimaryKeyColumns().stream().findFirst().get();
        Method method = new Method(methodEnum.getMethodName());
        method.setAbstract(Boolean.TRUE);
        switch (methodEnum) {
            case SAVE:
            case MODIFY:
                method.setReturnType(FullyQualifiedJavaType.getIntInstance());
                method.addParameter(new Parameter(model, firstCharToLowCase(modelName)));
                break;
            case REMOVE:
                method.setReturnType(FullyQualifiedJavaType.getIntInstance());
                method.addParameter(new Parameter(primaryKey.getFullyQualifiedJavaType(), primaryKey.getJavaProperty()));
                break;
            case QUERY:
                method.setReturnType(model);
                method.addParameter(new Parameter(primaryKey.getFullyQualifiedJavaType(), primaryKey.getJavaProperty()));
                break;
            case QUERY_LIST:
                method.setReturnType(new FullyQualifiedJavaType("List<" + model + ">"));
                method.addParameter(new Parameter(model, firstCharToLowCase(modelName)));
                break;
        }
        serviceInterface.addMethod(method);
    }

    private void addMethodToServiceImpl(TopLevelClass clazz, IntrospectedTable introspectedTable, ServiceMethodEnum methodEnum, String daoFieldName) {
        IntrospectedColumn primaryKey = introspectedTable.getPrimaryKeyColumns().stream().findFirst().get();
        Method method = new Method(methodEnum.getMethodName());
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        switch (methodEnum) {
            case SAVE:
                method.setReturnType(FullyQualifiedJavaType.getIntInstance());
                method.addParameter(new Parameter(model, firstCharToLowCase(modelName)));
                method.addBodyLine("return "+daoFieldName + ".insertSelective(" + firstCharToLowCase(modelName) + ");");
                break;
            case MODIFY:
                method.setReturnType(FullyQualifiedJavaType.getIntInstance());
                method.addParameter(new Parameter(model, firstCharToLowCase(modelName)));
                method.addBodyLine("return "+daoFieldName + ".updateByPrimaryKeySelective(" + firstCharToLowCase(modelName) + ");");
                break;
            case REMOVE:
                method.setReturnType(FullyQualifiedJavaType.getIntInstance());
                method.addParameter(new Parameter(primaryKey.getFullyQualifiedJavaType(), primaryKey.getJavaProperty()));
                method.addBodyLine("return "+daoFieldName + ".deleteByPrimaryKey(" + primaryKey.getJavaProperty() + ");");
                break;
            case QUERY:
                method.setReturnType(model);
                method.addParameter(new Parameter(primaryKey.getFullyQualifiedJavaType(), primaryKey.getJavaProperty()));
                method.addBodyLine("return "+daoFieldName + ".selectByPrimaryKey(" + primaryKey.getJavaProperty() + ");");
                break;
            case QUERY_LIST:
                method.setReturnType(new FullyQualifiedJavaType("List<" + model + ">"));
                method.addParameter(new Parameter(model, firstCharToLowCase(modelName)));
                method.addBodyLine("return "+daoFieldName + ".selectSelective(" + firstCharToLowCase(modelName) + ");");
                break;
        }
        clazz.addMethod(method);
    }
}
