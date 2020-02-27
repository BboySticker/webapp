package com.csye6225.webservice.RESTfulWebService.Entity.Recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.UUID;

@Entity
@Table(name = "ordered_list")
public class OrderedList {

    @Id
    @Column(name = "id")
    @JsonIgnore
    private String id = UUID.randomUUID().toString();

    @Min(1)
    @Column(name = "position")
    private int position;

    @Column(name = "items")
    private String items;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    @JsonIgnore
    private Recipe recipe;

    public OrderedList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "OrderedList{" +
                "id='" + id + '\'' +
                ", position=" + position +
                ", items='" + items + '\'' +
                '}';
    }
}
