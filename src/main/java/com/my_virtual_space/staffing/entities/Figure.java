package com.my_virtual_space.staffing.entities;

import com.my_virtual_space.staffing.entities.auditor.Auditable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "figures")
public class Figure extends Auditable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "hourly_cost")
    private BigDecimal hourlyCost;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "figure_person", joinColumns = @JoinColumn(name = "figure"), inverseJoinColumns = @JoinColumn(name = "person"))
    private List<Person> people = new ArrayList<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "figure_project", joinColumns = @JoinColumn(name = "figure"), inverseJoinColumns = @JoinColumn(name = "project"))
    private List<Project> projects = new ArrayList<>();

    public Figure() {
    }

    public Figure(String name, BigDecimal hourlyCost) {
        this.name = name;
        this.hourlyCost = hourlyCost;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getHourlyCost() {
        return hourlyCost;
    }

    public void setHourlyCost(BigDecimal hourlyCost) {
        this.hourlyCost = hourlyCost;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Figure{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hourlyCost=" + hourlyCost +
                ", people=" + people +
                ", projects=" + projects +
                '}';
    }
}
