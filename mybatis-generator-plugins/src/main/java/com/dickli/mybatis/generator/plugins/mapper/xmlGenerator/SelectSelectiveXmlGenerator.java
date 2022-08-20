package com.dickli.mybatis.generator.plugins.mapper.xmlGenerator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.sql.Types;

public class SelectSelectiveXmlGenerator extends AbstractXmlElementGenerator {
    public SelectSelectiveXmlGenerator() {
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "selectSelective"));
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", introspectedTable.getResultMapWithBLOBsId()));
        } else {
            answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
        }
        answer.addAttribute(new Attribute("parameterType", introspectedTable.getRules().calculateAllFieldsClass().getFullyQualifiedName()));
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select "); //$NON-NLS-1$
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement());

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        answer.addElement(buildWhere());

        parentElement.addElement(answer);
    }

    protected XmlElement buildWhere() {
        StringBuffer sb = new StringBuffer();
        XmlElement whereElement = new XmlElement("where");
        XmlElement ifElement = null;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            ifElement = new XmlElement("if");
            if (Types.VARCHAR == introspectedColumn.getJdbcType()) {
                ifElement.addAttribute(new Attribute("test", introspectedColumn.getJavaProperty() + " != null and "+introspectedColumn.getJavaProperty()+" !=''"));
            } else {
                ifElement.addAttribute(new Attribute("test", introspectedColumn.getJavaProperty() + " != null"));
            }

            sb.setLength(0);
            sb.append(" and " + MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn) + " = " + MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            ifElement.addElement(new TextElement(sb.toString()));
            whereElement.addElement(ifElement);
        }
        return whereElement;
    }
}
