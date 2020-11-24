package com.cgg.ghmcpollingapp.source;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "polling_master")
public class PollingEntity {

    @PrimaryKey
    @NonNull
    private Integer id;
    private String zone_id;
    private String zone_name;
    private String circle_id;
    private String circle_name;
    private String ward_id;
    private String ward_name;
    private String sector_id;
    private String sector_name;
    private String ps_no;
    private String ps_name;
    private String ps_from_cnt;
    private String ps_to_cnt;
    private String ps_total_cnt;

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(String circle_id) {
        this.circle_id = circle_id;
    }

    public String getCircle_name() {
        return circle_name;
    }

    public void setCircle_name(String circle_name) {
        this.circle_name = circle_name;
    }

    public String getWard_id() {
        return ward_id;
    }

    public void setWard_id(String ward_id) {
        this.ward_id = ward_id;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }

    public String getSector_id() {
        return sector_id;
    }

    public void setSector_id(String sector_id) {
        this.sector_id = sector_id;
    }

    public String getSector_name() {
        return sector_name;
    }

    public void setSector_name(String sector_name) {
        this.sector_name = sector_name;
    }

    public String getPs_no() {
        return ps_no;
    }

    public void setPs_no(String ps_no) {
        this.ps_no = ps_no;
    }

    public String getPs_name() {
        return ps_name;
    }

    public void setPs_name(String ps_name) {
        this.ps_name = ps_name;
    }

    public String getPs_from_cnt() {
        return ps_from_cnt;
    }

    public void setPs_from_cnt(String ps_from_cnt) {
        this.ps_from_cnt = ps_from_cnt;
    }

    public String getPs_to_cnt() {
        return ps_to_cnt;
    }

    public void setPs_to_cnt(String ps_to_cnt) {
        this.ps_to_cnt = ps_to_cnt;
    }

    public String getPs_total_cnt() {
        return ps_total_cnt;
    }

    public void setPs_total_cnt(String ps_total_cnt) {
        this.ps_total_cnt = ps_total_cnt;
    }
}
