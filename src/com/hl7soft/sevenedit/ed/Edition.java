package com.hl7soft.sevenedit.ed;

/**
 * @Author Krico
 * @Date 2023/5/4 16:35
 * @Version V1.0
 */
public enum Edition {
    VIEWER(10100, "Viewer"),
    STANDARD(10102, "Standard"),
    PROFESSIONAL(10101, "Professional");

    int id;

    String name;

    Edition(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}
