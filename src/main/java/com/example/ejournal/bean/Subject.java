package com.example.ejournal.bean;

import java.math.BigDecimal;

public class Subject {
    private String name;
    private BigDecimal grade;
    private Long term;
    private Long classs;
    private Long myClass;
    private Long gradeId;
    private BigDecimal finalGrade;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Long getClasss() {
        return classs;
    }

    public void setClasss(Long classs) {
        this.classs = classs;
    }

    public Long getMyClass() {
        return myClass;
    }

    public void setMyClass(Long myClass) {
        this.myClass = myClass;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public BigDecimal getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(BigDecimal finalGrade) {
        this.finalGrade = finalGrade;
    }
}
