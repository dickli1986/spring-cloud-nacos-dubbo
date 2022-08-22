package com.dickli.mybatis.generator.plugins.service.serviceEnum;

public enum ServiceMethodEnum {
    SAVE("save"),
    MODIFY("modify"),
    REMOVE("removeById"),
    QUERY("queryById"),
    QUERY_LIST("queryList"),
    QUERY_PAGE_LIST("queryPageList")
    ;
    String methodName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    ServiceMethodEnum(String methodName) {
        this.methodName = methodName;
    }
}
