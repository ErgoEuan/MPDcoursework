package org.me.gcu.EQS1707289;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Earthquake {

    private String title;
    private String description;
    private LocalDateTime originDate;
    private String location;
    private Integer depth;
    private Double magnitude;
    private String link;
    private LocalDateTime publishedDate;
    private String category;
    private Double locationLat;
    private Double locationLong;

    public Earthquake(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getOriginDate() {
        return originDate;
    }

    public void setOriginDate(LocalDateTime originDate) {
        this.originDate = originDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(Double locationLong) {
        this.locationLong = locationLong;
    }

    public Earthquake(String title, String description, LocalDateTime originDate, String location,
                      Integer depth, Double magnitude, String link, LocalDateTime publishedDate,
                      String category, Double locationLat, Double locationLong) {
        this.title = title;
        this.description = description;
        this.originDate = originDate;
        this.location = location;
        this.depth = depth;
        this.magnitude = magnitude;
        this.link = link;
        this.publishedDate = publishedDate;
        this.category = category;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
    }

}
