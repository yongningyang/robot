package com.innsmap.domain.bean;

/**
 * Created by long on 2018/9/12.
 */

public class ChargeData {
//    id":6,"code":"MIN_VAl","value":"20","desc":"最低充电阈值","group":"charge","remarkLow":"5","remarkHigh":"100"
    private int id;
    private String code;
    private String value;
    private int chargeValue;
    private String[] workValue;
    private String desc;
    private String group;
    private String remarkLow;
    private String remarkHigh;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRemarkLow() {
        return remarkLow;
    }

    public void setRemarkLow(String remarkLow) {
        this.remarkLow = remarkLow;
    }

    public String getRemarkHigh() {
        return remarkHigh;
    }

    public void setRemarkHigh(String remarkHigh) {
        this.remarkHigh = remarkHigh;
    }

    public int getChargeValue() {
        return chargeValue;
    }

    public void setChargeValue(int chargeValue) {
        this.chargeValue = chargeValue;
    }

    public String[] getWorkValue() {
        return workValue;
    }

    public void setWorkValue(String[] workValue) {
        this.workValue = workValue;
    }
}
