/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.bean;

//import com.visumbu.vb.model.TabWidget;
//import com.visumbu.vb.model.VbUser;

/**
 *
 * @author user
 */
public class ColumnDef {

    private Integer id;
    private String fieldName;
    private String fieldType;
    private String sortPriority;
    private String sortOrder;
    private String agregationFunction;
    private String displayName;
    private Integer groupPriority;
    private String displayFormat;
    private String dataFormat;
    private String functionName;
    private String expression;
    private String status;
    private String category;
//    private VbUser userId;
//    private TabWidget widgetId;

    public static class Aggregation {

        public static final String SUM = "sum";
        public static final String AVG = "avg";
        public static final String CTR = "ctr";   // clicks/impressions
        public static final String CPA = "cpa";   // cost/conversions
        public static final String CPC = "cpc";   // cost/clicks
        public static final String MAX = "max";
        public static final String MIN = "min";
        public static final String COUNT = "count";
        public static final String None = "";
        public static final String CPR = "cpr";  // cost/reacctions
        public static final String CPL = "cpl";  // cost/likes
        public static final String CPLC = "cplc"; // Cost/Link Click
        public static final String CPComment = "cpcomment"; // cost/comments
        public static final String CPostE = "cposte";  // cost/post engagements
        public static final String CPageE = "cpagee"; // cost/page engagements
        public static final String CPP = "cpp";  // cost/posts
    }

    public static class Format {

        public static final String CURRENCY = "$,.2f";
        public static final String PERCENTAGE = ".2%";
        public static final String DECIMAL1 = ",.1f";
        public static final String DECIMAL2 = ",.2f";
        public static final String INTEGER = ",.0f";
    }

    public ColumnDef(String fieldName, String fieldType, String displayName) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.displayName = displayName;
    }

    public ColumnDef(String fieldName, String fieldType, String displayName, Integer groupPriority) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.displayName = displayName;
        this.groupPriority = groupPriority;
    }

    public ColumnDef(String fieldName, String fieldType, String displayName, String agregationFunction, String displayFormat) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
        this.displayFormat = displayFormat;
    }

    public ColumnDef(Integer id, String fieldName, String fieldType, String displayName, String agregationFunction, String displayFormat, String status, String expression, String functionName) {
        this.id = id;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
        this.displayFormat = displayFormat;
        this.status = status;
        this.expression = expression;
        this.functionName = functionName;
    }

    public ColumnDef(String fieldName, String fieldType, String displayName, String agregationFunction) {
        this.fieldName = fieldName;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
    }

    public ColumnDef(Integer id, String fieldName, String fieldType, String sortPriority, String sortOrder, String agregationFunction, String displayName, Integer groupPriority, String displayFormat) {
        this.id = id;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.sortPriority = sortPriority;
        this.sortOrder = sortOrder;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
        this.groupPriority = groupPriority;
        this.displayFormat = displayFormat;
    }

    public ColumnDef() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(String sortPriority) {
        this.sortPriority = sortPriority;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getAgregationFunction() {
        return agregationFunction;
    }

    public void setAgregationFunction(String agregationFunction) {
        this.agregationFunction = agregationFunction;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getGroupPriority() {
        return groupPriority;
    }

    public void setGroupPriority(Integer groupPriority) {
        this.groupPriority = groupPriority;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

//    public VbUser getUserId() {
//        return userId;
//    }
//
//    public void setUserId(VbUser userId) {
//        this.userId = userId;
//    }
//
//    public TabWidget getWidgetId() {
//        return widgetId;
//    }
//
//    public void setWidgetId(TabWidget widgetId) {
//        this.widgetId = widgetId;
//    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

//    @Override
//    public String toString() {
//        return "ColumnDef{" + "id=" + id + ", fieldName=" + fieldName + ", fieldType=" + fieldType + ", sortPriority=" + sortPriority + ", sortOrder=" + sortOrder + ", agregationFunction=" + agregationFunction + ", displayName=" + displayName + ", groupPriority=" + groupPriority + ", displayFormat=" + displayFormat + ", dataFormat=" + dataFormat + ", functionName=" + functionName + ", expression=" + expression + ", status=" + status + ", userId=" + userId + ", widgetId=" + widgetId + '}';
//    }

    @Override
    public String toString() {
        return "ColumnDef{" + "id=" + id + ", fieldName=" + fieldName + ", fieldType=" + fieldType + ", sortPriority=" + sortPriority + ", sortOrder=" + sortOrder + ", agregationFunction=" + agregationFunction + ", displayName=" + displayName + ", groupPriority=" + groupPriority + ", displayFormat=" + displayFormat + ", dataFormat=" + dataFormat + ", functionName=" + functionName + ", expression=" + expression + ", status=" + status + ", category=" + category + '}';
    }
    
}
