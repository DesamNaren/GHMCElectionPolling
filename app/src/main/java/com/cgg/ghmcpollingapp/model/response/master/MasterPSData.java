package com.cgg.ghmcpollingapp.model.response.master;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "PS_Master_Tbl")
public class MasterPSData {

    @PrimaryKey(autoGenerate = true)
    private int pos_id;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("zone_id")
    @Expose
    private String zoneId;
    @SerializedName("zone_name")
    @Expose
    private String zoneName;
    @SerializedName("circle_id")
    @Expose
    private String circleId;
    @SerializedName("circle_name")
    @Expose
    private String circleName;
    @SerializedName("ward_id")
    @Expose
    private String wardId;
    @SerializedName("ward_name")
    @Expose
    private String wardName;
    @SerializedName("sector_id")
    @Expose
    private String sectorId;
    @SerializedName("sector_name")
    @Expose
    private String sectorName;
    @SerializedName("ps_no")
    @Expose
    private String psNo;
    @SerializedName("ps_name")
    @Expose
    private String psName;
    @SerializedName("ps_id")
    @Expose
    private String psId;
    @SerializedName("ps_from_cnt")
    @Expose
    private String psFromCnt;
    @SerializedName("ps_to_cnt")
    @Expose
    private String psToCnt;
    @SerializedName("ps_total_cnt")
    @Expose
    private String psTotalCnt;


    public int getPos_id() {
        return pos_id;
    }

    public void setPos_id(int pos_id) {
        this.pos_id = pos_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getSectorId() {
        return sectorId;
    }

    public void setSectorId(String sectorId) {
        this.sectorId = sectorId;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getPsNo() {
        return psNo;
    }

    public void setPsNo(String psNo) {
        this.psNo = psNo;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getPsFromCnt() {
        return psFromCnt;
    }

    public void setPsFromCnt(String psFromCnt) {
        this.psFromCnt = psFromCnt;
    }

    public String getPsToCnt() {
        return psToCnt;
    }

    public void setPsToCnt(String psToCnt) {
        this.psToCnt = psToCnt;
    }

    public String getPsTotalCnt() {
        return psTotalCnt;
    }

    public void setPsTotalCnt(String psTotalCnt) {
        this.psTotalCnt = psTotalCnt;
    }
}
