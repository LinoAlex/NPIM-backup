/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.bean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sabari
 */
public class LinkedInPostType {

    public static Map<String, Object> getPostType(String segment) {
        if (segment.equalsIgnoreCase("seniorities")) {
            return getSeniority();
        } else if (segment.equalsIgnoreCase("countries")) {
            return getCountry();
        } else if (segment.equalsIgnoreCase("functions")) {
            return getJobFunctions();
        } else if (segment.equalsIgnoreCase("industries")) {
            return getIndustries();
        } else if (segment.equalsIgnoreCase("companySizes")) {
            return getCompanySize();
        }
        return null;
    }

    public static Map<String, Object> getCountry() {
        Map<String, Object> country = new HashMap<>();
        country.put("in", "India");
        country.put("us", "America");
        country.put("pk", "Pakistan");
        country.put("bd", "Bangladesh");
        country.put("ch", "China");
        country.put("jp", "Japan");
        return country;

    }

    public static Map<String, Object> getSeniority() {
        Map<String, Object> seniority = new HashMap<>();
        seniority.put("1", "Unpaid");
        seniority.put("2", "Training");
        seniority.put("3", "Entry-level");
        seniority.put("4", "Senior");
        seniority.put("5", "Manager");
        seniority.put("6", "Director");
        seniority.put("7", "Vice President (VP)");
        seniority.put("8", "Chief X Officer (CxO)");
        seniority.put("9", "Partner");
        seniority.put("10", "Owner");

        return seniority;
    }

    private static Map<String, Object> getJobFunctions() {
        Map<String, Object> jobFunctions = new HashMap<>();
        jobFunctions.put("1", "Accounting");
        jobFunctions.put("2", "Administrative");
        jobFunctions.put("3", "Arts and Design");
        jobFunctions.put("4", "Business Development");
        jobFunctions.put("5", "Community & Social Services");
        jobFunctions.put("6", "Consulting");
        jobFunctions.put("7", "Education");
        jobFunctions.put("8", "Engineering");
        jobFunctions.put("9", "Entrepreneurship");
        jobFunctions.put("10", "Finance");
        jobFunctions.put("11", "Healthcare Services");
        jobFunctions.put("12", "Human Resources");
        jobFunctions.put("13", "Information Technology");
        jobFunctions.put("14", "Legal");
        jobFunctions.put("15", "Marketing");
        jobFunctions.put("16", "Media & Communications");
        jobFunctions.put("17", "Military & Protective Services");
        jobFunctions.put("18", "Operations");
        jobFunctions.put("19", "Product Management");
        jobFunctions.put("20", "Program & Product Management");
        jobFunctions.put("21", "Purchasing");
        jobFunctions.put("22", "Quality Assurance");
        jobFunctions.put("23", "Real Estate");
        jobFunctions.put("24", "Rersearch");
        jobFunctions.put("25", "Sales");
        jobFunctions.put("26", "Support");
        return jobFunctions;

    }

    private static Map<String, Object> getIndustries() {
        Map<String, Object> industries = new HashMap<>();
        industries.put("11", "Management Consulting");
        industries.put("99", "Design");
        industries.put("4", "Computer Software");
        industries.put("80", "Marketing and Advertising");
        industries.put("19", "Apparel & Fashion");
        industries.put("70", "Research");
        industries.put("96", "Information Technology and Services");
        industries.put("75", "Government Administration");
        
        return industries;
    }

    private static Map<String, Object> getCompanySize() {
        Map<String, Object> companySize = new HashMap<>();
        companySize.put("A", "Self-employed");
        companySize.put("B", "1-10 employees");
        companySize.put("C", "11-50 employees");
        companySize.put("D", "51-200 employees");
        companySize.put("E", "201-500 employees");
        companySize.put("F", "501-1000 employees");
        companySize.put("G", "1001-5000 employees");
        companySize.put("H", "5001-10,000 employees");
        companySize.put("I", "10,001+ employees");

        return companySize;
    }

}
