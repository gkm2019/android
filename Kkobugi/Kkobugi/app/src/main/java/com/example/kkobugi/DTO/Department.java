package com.example.kkobugi.DTO;

public class Department {
    private String departmentName;
    private String departmentContent;

    public String getDepartmentName(){
        return departmentName;
    }

    public void setDepartmentName(String departmentName){
        this.departmentName = departmentName;
    }

    public String getDepartmentContent(){
        return departmentContent;
    }

    public void setDepartmentContent(String departmentContent){
        this.departmentContent = departmentContent;
    }
}
