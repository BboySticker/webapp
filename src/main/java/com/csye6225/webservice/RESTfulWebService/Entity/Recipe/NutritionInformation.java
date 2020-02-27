package com.csye6225.webservice.RESTfulWebService.Entity.Recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "nutrition_information")
public class NutritionInformation {

    @Id
    @JsonIgnore
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();  // uuid

    @Column(name = "calories")
    private int calories;

    @Column(name = "cholesterol_in_mg")
    private float cholesterolInMg;

    @Column(name = "sodium_in_mg")
    private int sodiumInMg;

    @Column(name = "carbohydrates_in_grams")
    private float carbohydratesInGrams;

    @Column(name = "protein_in_grams")
    private float proteinInGrams;

    @OneToOne(mappedBy = "nutritionInformation")
    private Recipe recipe;

    public NutritionInformation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getCholesterolInMg() {
        return cholesterolInMg;
    }

    public void setCholesterolInMg(float cholesterolInMg) {
        this.cholesterolInMg = cholesterolInMg;
    }

    public int getSodiumInMg() {
        return sodiumInMg;
    }

    public void setSodiumInMg(int sodiumInMg) {
        this.sodiumInMg = sodiumInMg;
    }

    public float getCarbohydratesInGrams() {
        return carbohydratesInGrams;
    }

    public void setCarbohydratesInGrams(float carbohydratesInGrams) {
        this.carbohydratesInGrams = carbohydratesInGrams;
    }

    public float getProteinInGrams() {
        return proteinInGrams;
    }

    public void setProteinInGrams(float proteinInGrams) {
        this.proteinInGrams = proteinInGrams;
    }

    @Override
    public String toString() {
        return "NutritionInformation{" +
                "id='" + id + '\'' +
                ", calories=" + calories +
                ", cholesterolInMg=" + cholesterolInMg +
                ", sodiumInMg=" + sodiumInMg +
                ", carbohydratesInGrams=" + carbohydratesInGrams +
                ", proteinInGrams=" + proteinInGrams +
                '}';
    }
}
