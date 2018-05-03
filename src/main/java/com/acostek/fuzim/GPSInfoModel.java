package com.acostek.fuzim;


public class GPSInfoModel {

    private int shipId;
    private double createTime;//时间
    private String gpsModeInd;//工作模式

    private double speed;
    private String gpsLatdir;
    private double latitude;
    private String gpsLondir;
    private double longitude;

    private String gpsVardir;//磁偏角
    private double gpsMagvar;

    private double gpsTrackTure;//地面航向角

    public GPSInfoModel(int shipId, double createTime, String gpsModeInd, double speed, String gpsLatdir, double latitude, String gpsLondir, double longitude, String gpsVardir, double gpsMagvar, double gpsTrackTure) {
        this.shipId = shipId;
        this.createTime = createTime;
        this.gpsModeInd = gpsModeInd;
        this.speed = speed;
        this.gpsLatdir = gpsLatdir;
        this.latitude = latitude;
        this.gpsLondir = gpsLondir;
        this.longitude = longitude;
        this.gpsVardir = gpsVardir;
        this.gpsMagvar = gpsMagvar;
        this.gpsTrackTure = gpsTrackTure;
    }

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(double createTime) {
        this.createTime = createTime;
    }

    public String getGpsModeInd() {
        return gpsModeInd;
    }

    public void setGpsModeInd(String gpsModeInd) {
        this.gpsModeInd = gpsModeInd;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getGpsLatdir() {
        return gpsLatdir;
    }

    public void setGpsLatdir(String gpsLatdir) {
        this.gpsLatdir = gpsLatdir;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getGpsLondir() {
        return gpsLondir;
    }

    public void setGpsLondir(String gpsLondir) {
        this.gpsLondir = gpsLondir;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGpsVardir() {
        return gpsVardir;
    }

    public void setGpsVardir(String gpsVardir) {
        this.gpsVardir = gpsVardir;
    }

    public double getGpsMagvar() {
        return gpsMagvar;
    }

    public void setGpsMagvar(double gpsMagvar) {
        this.gpsMagvar = gpsMagvar;
    }

    public double getGpsTrackTure() {
        return gpsTrackTure;
    }

    public void setGpsTrackTure(double gpsTrackTure) {
        this.gpsTrackTure = gpsTrackTure;
    }

    @Override
    public String toString() {
        return "{" +
                "shipId=" + shipId +
                ", createTime=" + createTime +
                ", gpsModeInd='" + gpsModeInd + '\'' +
                ", speed=" + speed +
                ", gpsLatdir='" + gpsLatdir + '\'' +
                ", latitude=" + latitude +
                ", gpsLondir='" + gpsLondir + '\'' +
                ", longitude=" + longitude +
                ", gpsVardir='" + gpsVardir + '\'' +
                ", gpsMagvar=" + gpsMagvar +
                ", gpsTrackTure=" + gpsTrackTure +
                '}';
    }

}

