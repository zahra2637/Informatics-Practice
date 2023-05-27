package com.sample.app.service;

import com.sample.app.entity.Person;

import java.util.List;

public class PersonSearchResult {
    List<Person> objects;
    int from;
    int size;

    public PersonSearchResult(List<Person> objects, int i, int size) {
        this.objects = objects;
    }

    public List<Person> getObjects() {
        return objects;
    }

    public void setObjects(List<Person> objects) {
        this.objects = objects;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
