package com.example.animal_shelter;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class Adoption {
    private int adoptionId;
    private int admissionId;
    private String customerId;

    private Drawable admissionDog;
    private String dogName;
    private Date createDate;
    private String state;
    private String reason;

    public int getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(int adoptionId) {
        this.adoptionId = adoptionId;
    }

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

    public Drawable getAdmissionDog() {
        return admissionDog;
    }

    public void setAdmissionDog(Drawable admissionDog) {
        this.admissionDog = admissionDog;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
