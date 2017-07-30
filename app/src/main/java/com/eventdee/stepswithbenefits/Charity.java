package com.eventdee.stepswithbenefits;

public class Charity {

    private String name;
    private String tagline;
    private int logo;
    private int background;

    public Charity(String name, String tagline, int logo, int background) {
        this.name = name;
        this.tagline = tagline;
        this.logo = logo;
        this.background = background;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
