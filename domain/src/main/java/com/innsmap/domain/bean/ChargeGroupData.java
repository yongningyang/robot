package com.innsmap.domain.bean;

import java.util.ArrayList;

/**
 * Created by long on 2018/9/12.
 */

public class ChargeGroupData {
    private ArrayList<ChargeData> charge;
    private ArrayList<ChargeData> work;

    public ArrayList<ChargeData> getCharge() {
        return charge;
    }

    public void setCharge(ArrayList<ChargeData> charge) {
        this.charge = charge;
    }

    public ArrayList<ChargeData> getWork() {
        return work;
    }

    public void setWork(ArrayList<ChargeData> work) {
        this.work = work;
    }
}
