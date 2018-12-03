package com.innsmap.domain.bean;

import java.io.Serializable;

/**
 * Created by long on 2018/8/15.
 */

public class EventData implements Serializable{
    private int activityId;
    private int activityStatus;
    private int showWay; // 1：链接  2：图片
    private String activityName;
    private String buildId;
    private String startTime;
    private String endTime;
    private String voiceCommand;
    private String[] voiceCommands;
    private String[] voiceCommandsPY;
    private String tts;
    private String url;
    private String imageAddress;
    private String[] imageUrls;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getShowWay() {
        return showWay;
    }

    public void setShowWay(int showWay) {
        this.showWay = showWay;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getVoiceCommand() {
        return voiceCommand;
    }

    public void setVoiceCommand(String voiceCommand) {
        this.voiceCommand = voiceCommand;
    }

    public String[] getVoiceCommandsPY() {
        return voiceCommandsPY;
    }

    public void setVoiceCommandsPY(String[] voiceCommandsPY) {
        this.voiceCommandsPY = voiceCommandsPY;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String[] getVoiceCommands() {
        return voiceCommands;
    }

    public void setVoiceCommands(String[] voiceCommands) {
        this.voiceCommands = voiceCommands;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }
}
