package com.noticiasrss.data.sqlite.entities;

public class Source {
    private int id;
    private String siteLink;
    private long addDate;
    private long lastDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSiteLink() {
        return siteLink;
    }

    public void setSiteLink(String siteLink) {
        this.siteLink = siteLink;
    }

    public long getAddDate() {
        return addDate;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public long getLastDate() {
        return lastDate;
    }

    public void setLastDate(long lastDate) {
        this.lastDate = lastDate;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", siteLink='" + siteLink + '\'' +
                '}';
    }
}
