package com.innsmap.domain.bean;

/**
 * Created by long on 2018/8/13.
 */

public class PoiAliasData {
    private int id;
    private int poiId;
    private String buildId;
    private String poiName;
    private String poiAlias;
    private String updDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoiId() {
        return poiId;
    }

    public void setPoiId(int poiId) {
        this.poiId = poiId;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getPoiAlias() {
        return poiAlias;
    }

    public void setPoiAlias(String poiAlias) {
        this.poiAlias = poiAlias;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }
}
