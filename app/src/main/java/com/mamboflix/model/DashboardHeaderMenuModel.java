package com.mamboflix.model;


public class DashboardHeaderMenuModel {
    private String title;
    private boolean isExpanded;

    public DashboardHeaderMenuModel(String title, boolean isExpanded) {
        this.title = title;
        this.isExpanded = isExpanded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
