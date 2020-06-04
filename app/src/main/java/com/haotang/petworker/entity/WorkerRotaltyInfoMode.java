package com.haotang.petworker.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者：灼星
 * 时间：2020-04-20
 */
public class WorkerRotaltyInfoMode implements Serializable {

    /**
     * totalServiceDedutorAmount : 0.0
     * cityFlag : false
     * cityId : 2
     * groupShopFlag : false
     */
    /**
     * oldShopDeductorAmount : 100.0
     * oldShopRate : 0.01
     * totalServiceDedutorAmount : 100.0
     * shortOfRateAmount : 100.0
     * nextRateAmount : 100.0
     * nextRates : 0.09
     * isMaxRateFlag : false
     */

    private double totalServiceDedutorAmount;
    private boolean cityFlag;
    private int cityId;
    private boolean groupShopFlag;
    private double oldShopDeductorAmount;
    private String oldShopRate;
    private double shortOfRateAmount;
    private double nextRateAmount;
    private String nextRates;
    private boolean isMaxRateFlag;
    /**
     * oldShopDeductorAmount : 100.0
     * newShopDeductorAmount : 1300.0
     * newShopRate : 2%
     * totalServiceDedutorAmount : 100.0
     * shortOfRateAmount : 100.0
     * nextRateAmount : 100.0
     * nextRates : 9%
     * isNewShop : false
     */

    private double newShopDeductorAmount;
    private String newShopRate;
    private boolean isNewShop;


    public double getTotalServiceDedutorAmount() {
        return totalServiceDedutorAmount;
    }

    public void setTotalServiceDedutorAmount(double totalServiceDedutorAmount) {
        this.totalServiceDedutorAmount = totalServiceDedutorAmount;
    }

    public boolean isCityFlag() {
        return cityFlag;
    }

    public void setCityFlag(boolean cityFlag) {
        this.cityFlag = cityFlag;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public boolean isGroupShopFlag() {
        return groupShopFlag;
    }

    public void setGroupShopFlag(boolean groupShopFlag) {
        this.groupShopFlag = groupShopFlag;
    }

    public double getOldShopDeductorAmount() {
        return oldShopDeductorAmount;
    }

    public void setOldShopDeductorAmount(double oldShopDeductorAmount) {
        this.oldShopDeductorAmount = oldShopDeductorAmount;
    }

    public String getOldShopRate() {
        return oldShopRate;
    }

    public void setOldShopRate(String oldShopRate) {
        this.oldShopRate = oldShopRate;
    }

    public double getShortOfRateAmount() {
        return shortOfRateAmount;
    }

    public void setShortOfRateAmount(double shortOfRateAmount) {
        this.shortOfRateAmount = shortOfRateAmount;
    }

    public double getNextRateAmount() {
        return nextRateAmount;
    }

    public void setNextRateAmount(double nextRateAmount) {
        this.nextRateAmount = nextRateAmount;
    }

    public String getNextRates() {
        return nextRates;
    }

    public void setNextRates(String nextRates) {
        this.nextRates = nextRates;
    }

    public boolean isIsMaxRateFlag() {
        return isMaxRateFlag;
    }

    public void setIsMaxRateFlag(boolean isMaxRateFlag) {
        this.isMaxRateFlag = isMaxRateFlag;
    }

    public double getNewShopDeductorAmount() {
        return newShopDeductorAmount;
    }

    public void setNewShopDeductorAmount(double newShopDeductorAmount) {
        this.newShopDeductorAmount = newShopDeductorAmount;
    }

    public String getNewShopRate() {
        return newShopRate;
    }

    public void setNewShopRate(String newShopRate) {
        this.newShopRate = newShopRate;
    }

    public boolean isIsNewShop() {
        return isNewShop;
    }

    public void setIsNewShop(boolean isNewShop) {
        this.isNewShop = isNewShop;
    }
}
