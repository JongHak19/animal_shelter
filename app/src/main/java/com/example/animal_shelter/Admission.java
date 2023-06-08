package com.example.animal_shelter;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;

public class Admission {
    private int admissionId;
    private String customerId;
    private Date createDate;
    private String name;
    private  String breed;
    private String gender;
    private String RescueArea;
    private int Age;
    private int Weight;
    private String Health;
    private String Personality;
    private String Habits;
    private String Fur;
    private String Caution;
    private Drawable Image;
    private String State;

    public int getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(int admissionId) {
        this.admissionId = admissionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRescueArea() {
        return RescueArea;
    }

    public void setRescueArea(String rescueArea) {
        RescueArea = rescueArea;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public String getHealth() {
        return Health;
    }

    public void setHealth(String health) {
        Health = health;
    }

    public String getPersonality() {
        return Personality;
    }

    public void setPersonality(String personality) {
        Personality = personality;
    }

    public String getHabits() {
        return Habits;
    }

    public void setHabits(String habits) {
        Habits = habits;
    }

    public String getFur() {
        return Fur;
    }

    public void setFur(String fur) {
        Fur = fur;
    }

    public String getCaution() {
        return Caution;
    }

    public void setCaution(String caution) {
        Caution = caution;
    }

    public Drawable getImage() {
        return Image;
    }

    public void setImage(Drawable image) {
        this.Image = image;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
