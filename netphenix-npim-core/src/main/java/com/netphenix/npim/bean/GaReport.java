/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.bean;

/**
 *
 * @author user
 */
public class GaReport {

    private String reportName;
    private String fields;
    private String defaultDimension;
    private String defaultFilter;

    public GaReport(String reportName, String fields, String defaultDimension, String defaultFilter) {
        this.reportName = reportName;
        this.fields = fields;
        this.defaultDimension = defaultDimension;
        this.defaultFilter = defaultFilter;
    }
    
    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getDefaultDimension() {
        return defaultDimension;
    }

    public void setDefaultDimension(String defaultDimension) {
        this.defaultDimension = defaultDimension;
    }

    public String getDefaultFilter() {
        return defaultFilter;
    }

    public void setDefaultFilter(String defaultFilter) {
        this.defaultFilter = defaultFilter;
    }

    @Override
    public String toString() {
        return "GaReport{" + "reportName=" + reportName + ", fields=" + fields + ", defaultDimension=" + defaultDimension + ", defaultFilter=" + defaultFilter + '}';
    }

}
