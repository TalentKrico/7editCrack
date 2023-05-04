package com.hl7soft.sevenedit.lic;

import com.hl7soft.sevenedit.ed.Edition;
import com.hl7soft.sevenedit.util.licapi.License;
import com.hl7soft.sevenedit.util.licapi.LicenseManager;
import com.hl7soft.sevenedit.util.licapi.LicenseStatus;
import com.hl7soft.sevenedit.util.licapi.io.LicenseReader;

public class LicenseManager2 extends LicenseManager {
    private static LicenseManager _instance;

    private static final int EVAL_DAYS = 21;

    private static final String PRODUCT_ID = "com.7edit.2x";

    private static final String PWD = "com.7edit.2x";

    public LicenseManager2() {
        super("classloader.profile.ssc", "jar.global.settings.xm", 21);
    }

    public static LicenseManager getInstance() {
        if (_instance == null)
            _instance = new LicenseManager2();
        return _instance;
    }

    public LicenseStatus getLicenseStatus() {
        return getLicenseStatus("com.7edit.2x", "com.7edit.2x");
    }

    public static boolean isValidKey(String licenseKey) {
        License license = null;
        try {
            license = (new LicenseReader()).readFromKey(licenseKey, "com.7edit.2x");
        } catch (Exception e) {
            return false;
        }
        if (license == null)
            return false;
        if (license.getProductId() != Edition.VIEWER.getId() && license.getProductId() != Edition.STANDARD.getId() && license.getProductId() != Edition.PROFESSIONAL.getId())
            return false;
        if (license.getExpirationDate() != null && license.getExpirationDate().getTimeInMillis() < System.currentTimeMillis())
            return false;
        return true;
    }

    public void registerProduct(String licenseKey) {
        registerProduct("com.7edit.2x", licenseKey);
    }

    public static void main(String[] args) {
        String licenseKey = "FBED6C-2E3664-433B23-F61DE2";
        System.out.println(LicenseManager2.isValidKey(licenseKey));
    }
}
