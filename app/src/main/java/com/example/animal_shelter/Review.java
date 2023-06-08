package com.example.animal_shelter;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class Review {
    private int reviewId;
    private int adoptionId;
    private String customerId;
    private Date createDate;
    private String title;
    private String content;
    private Drawable dogImage1;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(int admissionId) {
        this.adoptionId = admissionId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Drawable getDogImage1() {
        return dogImage1;
    }

    public void setDogImage1(Drawable dogImage1) {
        this.dogImage1 = dogImage1;
    }
}
