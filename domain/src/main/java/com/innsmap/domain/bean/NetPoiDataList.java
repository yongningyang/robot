package com.innsmap.domain.bean;

import java.util.ArrayList;

/**
 * Created by long on 2018/8/13.
 */

public class NetPoiDataList {
    private int pagesize;
    private int currentpage;
    private int totalpage;
    private int totalcount;
    private ArrayList<NetPoiData> datalist;

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public ArrayList<NetPoiData> getDatalist() {
        return datalist;
    }

    public void setDatalist(ArrayList<NetPoiData> datalist) {
        this.datalist = datalist;
    }
}
