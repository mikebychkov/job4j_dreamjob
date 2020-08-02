package com.dream.model;

public interface Model {

    Model of(int id, String name);
    int getId();
    void setId(int id);
    String getName();
    void setName(String name);
    String getTableName();
}
