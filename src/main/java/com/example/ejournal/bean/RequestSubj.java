package com.example.ejournal.bean;

import java.util.Map;

public class RequestSubj {
    private String classs;
    private Map<Long, Long> subjects; //key=id of subject, value=term

    public String getClasss() {
        return classs;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }

    public Map<Long, Long> getSubjects() {
        return subjects;
    }

    public void setSubjects(Map<Long, Long> subjects) {
        this.subjects = subjects;
    }
}
