package com.dwliu.ssmintegration.dao.entity;

import java.io.Serializable;

public class Emp implements Serializable {
    private Integer empId;

    private String empName;

    private String dender;

    private String email;

    private Integer dId;

    private static final long serialVersionUID = 1L;

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    public String getDender() {
        return dender;
    }

    public void setDender(String dender) {
        this.dender = dender == null ? null : dender.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }
}