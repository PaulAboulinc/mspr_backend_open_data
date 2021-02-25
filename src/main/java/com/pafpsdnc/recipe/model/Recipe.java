package com.pafpsdnc.recipe.model;

import javax.persistence.*;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(columnDefinition="DOUBLE PRECISION")
    protected long portions;

    @Column(length = 65535, columnDefinition="TEXT")
    protected String description;

    @Column(columnDefinition="TEXT")
    protected String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPortions() {
        return portions;
    }

    public void setPortions(long portions) {
        this.portions = portions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", portions=" + portions +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
