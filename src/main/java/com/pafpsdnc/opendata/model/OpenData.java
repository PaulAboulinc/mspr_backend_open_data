package com.pafpsdnc.opendata.model;

import javax.persistence.*;

@Entity
public class OpenData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(columnDefinition="INTEGER")
    protected long uniqId;

    @Column(columnDefinition="TEXT")
    protected String value;

    @Column(columnDefinition="TEXT")
    protected String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUniqId() {
        return uniqId;
    }

    public void setUniqId(long uniqId) {
        this.uniqId = uniqId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OpenData{" +
                "id=" + id +
                ", uniqId=" + uniqId +
                ", value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
