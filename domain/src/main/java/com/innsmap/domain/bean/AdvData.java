package com.innsmap.domain.bean;

import java.io.Serializable;

/**
 * Created by long on 2018/8/15.
 */

public class AdvData implements Serializable{
    private int advertId;
    private String advertName;
    private String startDate;
    private String endDate;
    private int advertType;//1图片2视频
    private String url;
    private String imagePath;
    private String[] imagePaths;
    private int advertCycle;
    private String thumbnailPath;
    private int playLength;

    public int getAdvertId() {
        return advertId;
    }

    public void setAdvertId(int advertId) {
        this.advertId = advertId;
    }

    public String getAdvertName() {
        return advertName;
    }

    public void setAdvertName(String advertName) {
        this.advertName = advertName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getAdvertType() {
        return advertType;
    }

    public void setAdvertType(int advertType) {
        this.advertType = advertType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String[] getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(String[] imagePaths) {
        this.imagePaths = imagePaths;
    }

    public int getAdvertCycle() {
        return advertCycle;
    }

    public void setAdvertCycle(int advertCycle) {
        this.advertCycle = advertCycle;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public int getPlayLength() {
        return playLength;
    }

    public void setPlayLength(int playLength) {
        this.playLength = playLength;
    }
}
