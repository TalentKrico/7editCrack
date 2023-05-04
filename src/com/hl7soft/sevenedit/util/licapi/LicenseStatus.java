package com.hl7soft.sevenedit.util.licapi;

import java.util.Calendar;

public class LicenseStatus {
    int daysEvaluated;

    int evaluationDaysLeft;

    Calendar evalStart;

    Calendar expirationDate;

    String licenseKey;

    int registeredProductId;

    int registeredSerialNumber;

    boolean cheatDetected;

    boolean evaluationExpired;

    boolean evaluationInProgress;

    boolean registered;

    public int getDaysEvaluated() {
        return this.daysEvaluated;
    }

    public void setDaysEvaluated(int daysEvaluated) {
        this.daysEvaluated = daysEvaluated;
    }

    public int getEvaluationDaysLeft() {
        return this.evaluationDaysLeft;
    }

    public void setEvaluationDaysLeft(int evaluationDaysLeft) {
        this.evaluationDaysLeft = evaluationDaysLeft;
    }

    public Calendar getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLicenseKey() {
        return this.licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public int getRegisteredProductId() {
        return this.registeredProductId;
    }

    public void setRegisteredProductId(int registeredProductId) {
        this.registeredProductId = registeredProductId;
    }

    public int getRegisteredSerialNumber() {
        return this.registeredSerialNumber;
    }

    public void setRegisteredSerialNumber(int registeredSerialNumber) {
        this.registeredSerialNumber = registeredSerialNumber;
    }

    public boolean isCheatDetected() {
        return this.cheatDetected;
    }

    public void setCheatDetected(boolean cheatDetected) {
        this.cheatDetected = cheatDetected;
    }

    public boolean isEvaluationExpired() {
        return this.evaluationExpired;
    }

    public void setEvaluationExpired(boolean evaluationExpired) {
        this.evaluationExpired = evaluationExpired;
    }

    public boolean isEvaluationInProgress() {
        return this.evaluationInProgress;
    }

    public void setEvaluationInProgress(boolean evaluationInProgress) {
        this.evaluationInProgress = evaluationInProgress;
    }

    public boolean isRegistered() {
        return this.registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
