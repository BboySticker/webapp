package com.csye6225.webservice.RESTfulWebService.Entity.Recipe;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe")
public class Recipe {

    @OneToOne
    @Cascade(value = { org.hibernate.annotations.CascadeType.REMOVE, org.hibernate.annotations.CascadeType.DELETE})
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @Id
    @Column(name = "id")
    private String id;  // uuid

    @Column(name = "created_ts")
    private String createdTs;

    @Column(name = "updated_ts")
    private String updatedTs;

    @Column(name = "author_id")
    private String authorId;

    @Column(name = "cook_time_in_min")
    private int cookTimeInMin;

    @Column(name = "prep_time_in_min")
    private int prepTimeInMin;

    @Column(name = "total_time_in_min")
    private int totalTimeInMin;

    @Column(name = "title")
    private String title;

    @Column(name = "cuisine")
    private String cuisine;

    @Column(name = "servings")
    private int servings;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "ingredients")
    private List<String> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    @Cascade(value = { org.hibernate.annotations.CascadeType.REMOVE, org.hibernate.annotations.CascadeType.DELETE})
    @Column(name = "steps")
    private List<OrderedList> steps;

    @OneToOne
    @Cascade(value = { org.hibernate.annotations.CascadeType.REMOVE, org.hibernate.annotations.CascadeType.DELETE})
    @JoinColumn(name = "nutrition_information", referencedColumnName = "id")
    private NutritionInformation nutritionInformation;

    public Recipe() {
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(String createdTs) {
        this.createdTs = createdTs;
    }

    public String getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(String updatedTs) {
        this.updatedTs = updatedTs;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getCookTimeInMin() {
        return cookTimeInMin;
    }

    public void setCookTimeInMin(int cookTimeInMin) {
        this.cookTimeInMin = cookTimeInMin;
    }

    public int getPrepTimeInMin() {
        return prepTimeInMin;
    }

    public void setPrepTimeInMin(int prepTimeInMin) {
        this.prepTimeInMin = prepTimeInMin;
    }

    public int getTotalTimeInMin() {
        return totalTimeInMin;
    }

    public void setTotalTimeInMin(int totalTimeInMin) {
        this.totalTimeInMin = totalTimeInMin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<OrderedList> getSteps() {
        return steps;
    }

    public void setSteps(List<OrderedList> steps) {
        this.steps = steps;
    }

    public NutritionInformation getNutritionInformation() {
        return nutritionInformation;
    }

    public void setNutritionInformation(NutritionInformation nutritionInformation) {
        this.nutritionInformation = nutritionInformation;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "image=" + image +
                ", id='" + id + '\'' +
                ", created_ts='" + createdTs + '\'' +
                ", updatedTs='" + updatedTs + '\'' +
                ", authorId='" + authorId + '\'' +
                ", cookTimeInMin=" + cookTimeInMin +
                ", prepTimeInMin=" + prepTimeInMin +
                ", totalTimeInMin=" + totalTimeInMin +
                ", title='" + title + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", servings=" + servings +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", nutritionInformation=" + nutritionInformation +
                '}';
    }
}
