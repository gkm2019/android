package com.example.kkobugi.DTO;

public class NumberInfo {

    private int numberIdx;
    private String number;
    private String numberFax;
    private String departmentName;

    public int getNumberIdx() {
        return numberIdx;
    }

    public void setNumberIdx(int numberIdx) {
        this.numberIdx = numberIdx;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumberFax() {
        return numberFax;
    }

    public void setNumberFax(String numberFax) {
        this.numberFax = numberFax;
    }

    public String getDepartmentName() {return departmentName;}

    public void setDepartmentName(String departmentName){this.departmentName=departmentName;}
}
