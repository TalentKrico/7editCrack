package com.hl7soft.sevenedit.util.licapi;

import com.hl7soft.sevenedit.util.licapi.io.LicenseReader;
import com.hl7soft.sevenedit.util.licapi.util.XOREncryption;
import java.util.Calendar;
import java.util.prefs.Preferences;

public class LicenseManager {
    XOREncryption encryption = new XOREncryption("4##34$^%213A");

    int defaultMaximumEvaluationDays = 30;

    String primaryRecordKey = "primary.rec";

    String secondaryRecordKey = "secondary.rec";

    public LicenseManager(String primaryRecordKey, String secondaryRecordKey, int defaultMaximumEvaluationDays) {
        this.primaryRecordKey = primaryRecordKey;
        this.secondaryRecordKey = secondaryRecordKey;
        this.defaultMaximumEvaluationDays = defaultMaximumEvaluationDays;
    }

    public LicenseStatus getLicenseStatus(String productId, String password) {
        try {
            LicenseStatus status = new LicenseStatus();
            String licenseKey = readLicenseKey(productId);
            License license = parseLicense(licenseKey, password);
            Calendar evalStart = null;
            try {
                evalStart = getEvaluationStartDate(productId);
            } catch (Exception e) {
                if (getSecondaryRecord(productId) != null)
                    try {
                        restorePrimaryRecord(productId);
                        evalStart = getEvaluationStartDate(productId);
                    } catch (Exception ex) {
                        status.cheatDetected = true;
                        status.evaluationExpired = true;
                        return status;
                    }
            }
            boolean temporaryLicense = (license != null && license.getExpirationDate() != null);
            boolean productRegistered = (license != null) ? (!temporaryLicense) : false;
            if (!productRegistered && evalStart == null)
                if (getSecondaryRecord(productId) != null) {
                    try {
                        restorePrimaryRecord(productId);
                        evalStart = getEvaluationStartDate(productId);
                    } catch (Exception e) {
                        status.cheatDetected = true;
                        status.evaluationExpired = true;
                        return status;
                    }
                } else {
                    evalStart = Calendar.getInstance();
                    evalStart.setTimeInMillis(getCurrentTimeMillis());
                    storeEvaluationStart(productId, evalStart);
                }
            int daysEvaluated = 0, maxDaysToEvaluate = 0, evaluationDaysLeft = 0;
            boolean evaluationExpired = false;
            boolean cheatDetected = false;
            if (evalStart != null) {
                daysEvaluated = (int)((getCurrentTimeMillis() - evalStart.getTimeInMillis()) / 86400000L);
                maxDaysToEvaluate = temporaryLicense ? (int)((license.getExpirationDate().getTimeInMillis() - evalStart.getTimeInMillis()) / 86400000L) : this.defaultMaximumEvaluationDays;
                evaluationDaysLeft = maxDaysToEvaluate - daysEvaluated;
                evaluationExpired = (evaluationDaysLeft <= 0);
                long lastRun = getLastRunTimestamp(productId);
                if (lastRun > 0L)
                    if (getCurrentTimeMillis() < lastRun) {
                        cheatDetected = true;
                        evaluationExpired = true;
                    }
                if (daysEvaluated > 2)
                    storeSecondaryRecord(productId, evalStart);
            }
            status.daysEvaluated = daysEvaluated;
            status.evalStart = evalStart;
            status.cheatDetected = cheatDetected;
            status.evaluationDaysLeft = evaluationDaysLeft;
            status.evaluationExpired = evaluationExpired;
            status.evaluationInProgress = !evaluationExpired;
            status.registered = productRegistered;
            status.expirationDate = (license != null) ? license.getExpirationDate() : null;
            status.licenseKey = licenseKey;
            status.registeredProductId = (license != null) ? license.getProductId() : 0;
            status.registeredSerialNumber = (license != null) ? license.getSerialNumber() : 0;
            return status;
        } catch (Exception e) {
            throw new RuntimeException("Error getting license status.", e);
        }
    }

    public void registerProduct(String productId, String licenseKey) {
        writeLicenseKey(productId, licenseKey);
    }

    private void restorePrimaryRecord(String productId) {
        String str = getSecondaryRecord(productId);
        int idx = str.indexOf('|');
        String tsStr = str.substring(0, idx);
        long ts = Long.parseLong(tsStr);
        Calendar evalStart = Calendar.getInstance();
        evalStart.setTimeInMillis(ts);
        storeEvaluationStart(productId, evalStart);
    }

    protected long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    private void storeEvaluationStart(String productId, Calendar calendar) {
        storeEvaluationStart(productId, "" + calendar.getTimeInMillis());
    }

    private void storeEvaluationStart(String productId, String value) {
        writePrimaryRecord(productId, encrypt(value));
    }

    private Calendar getEvaluationStartDate(String productId) {
        String evalStartTimestamp = readPrimaryRecord(productId);
        if (evalStartTimestamp != null) {
            evalStartTimestamp = decrypt(evalStartTimestamp);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(evalStartTimestamp));
            return calendar;
        }
        return null;
    }

    private String getSecondaryRecord(String productId) {
        String str = readSecondaryRecord(productId);
        return (str != null) ? decrypt(str) : null;
    }

    private License parseLicense(String key, String password) {
        try {
            return (new LicenseReader()).readFromKey(key, password);
        } catch (Exception exception) {
            return null;
        }
    }

    private long getLastRunTimestamp(String productId) {
        try {
            String str = getSecondaryRecord(productId);
            if (str != null) {
                int idx = str.indexOf('|');
                String ts = str.substring(0, idx);
                String ts2 = str.substring(idx + 1);
                return Long.parseLong(ts) + Long.parseLong(ts2);
            }
            return -1L;
        } catch (Exception e) {
            return 0L;
        }
    }

    private void storeSecondaryRecord(String productId, Calendar evalStart) {
        long ts = getCurrentTimeMillis();
        String value = "" + evalStart.getTimeInMillis() + "|" + evalStart.getTimeInMillis();
        writeSecondaryRecord(productId, encrypt(value));
    }

    private String encrypt(String str) {
        return this.encryption.encrypt(str);
    }

    private String decrypt(String str) {
        return this.encryption.decrypt(str);
    }

    public int getDefaultMaximumEvaluationDays() {
        return this.defaultMaximumEvaluationDays;
    }

    public void setDefaultMaximumEvaluationDays(int defaultMaximumEvaluationDays) {
        this.defaultMaximumEvaluationDays = defaultMaximumEvaluationDays;
    }

    private String readLicenseKey(String productId) {
        Preferences p = Preferences.userRoot();
        return p.get("license.key." + productId, null);
    }

    private void writeLicenseKey(String productId, String licenseKey) {
        Preferences p = Preferences.userRoot();
        p.put("license.key." + productId, licenseKey);
    }

    private String readPrimaryRecord(String productId) {
        Preferences p = Preferences.userRoot();
        return p.get(this.primaryRecordKey, null);
    }

    private void writePrimaryRecord(String productId, String recordValue) {
        Preferences p = Preferences.userRoot();
        p.put(this.primaryRecordKey, recordValue);
    }

    private String readSecondaryRecord(String productId) {
        Preferences p = Preferences.userRoot();
        return p.get(this.secondaryRecordKey, null);
    }

    private void writeSecondaryRecord(String productId, String recordValue) {
        Preferences p = Preferences.userRoot();
        p.put(this.secondaryRecordKey, recordValue);
    }
}
