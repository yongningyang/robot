package com.innsmap.domain.bean;

/**
 * Created by long on 2018/8/13.
 */

public class NetPoiData {
    private int poiId;
    private String poiName;
    private String poiBtypeName;
    private String poiStypeName;
    private String photoPath;
    private String addr;
    private String tel;
    private String buildId;
    private String floorId;
    private String logo;
    private float x;
    private float y;

    public int getPoiId() {
        return poiId;
    }

    public void setPoiId(int poiId) {
        this.poiId = poiId;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getPoiBtypeName() {
        return poiBtypeName;
    }

    public void setPoiBtypeName(String poiBtypeName) {
        this.poiBtypeName = poiBtypeName;
    }

    public String getPoiStypeName() {
        return poiStypeName;
    }

    public void setPoiStypeName(String poiStypeName) {
        this.poiStypeName = poiStypeName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
