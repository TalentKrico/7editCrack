package com.hl7soft.sevenedit.util.licapi;

import java.util.Calendar;

/**
 * @Author Krico
 * @Date 2023/5/4 16:36
 * @Version V1.0
 */
public class License {
    int serialNumber;

    int productId;

    Calendar expirationDate;

    public License(int serialNumber, int productId, Calendar expirationDate) {
        this.serialNumber = serialNumber;
        this.productId = productId;
        this.expirationDate = expirationDate;
    }

    public License(int serialNumber, int productId) {
        this(serialNumber, productId, null);
    }

    public Calendar getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("serialNumber=").append(this.serialNumber).append('\n');
        sb.append("productId=").append(this.productId).append('\n');
        if (this.expirationDate != null) {
            sb.append("expiration=").append("" + this.expirationDate
                            .get(5) + "/" + this.expirationDate.get(5) + "/" + this.expirationDate
                            .get(2) + 1)
                    .append('\n');
        } else {
            sb.append("expiration=none").append('\n');
        }
        return sb.toString();
    }
}