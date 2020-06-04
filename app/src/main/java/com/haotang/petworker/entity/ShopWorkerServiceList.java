package com.haotang.petworker.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopWorkerServiceList implements Serializable {
	public int baseHomeDuration;
	public int baseShopDuration;
	public int countHomeDuration;
	public int countShopDuration;
	public int homeDuration;
	public int id;
	public String name;
	public int petId;
	public int serviceId;
	public int shopDuration;
	public int size;
	public int workerId;
	public int shopMaxTime;
	public int shopMinTime;
	public int homeMaxTime;
	public int homeMinTime;

	@Override
	public String toString() {
		return "ShopWorkerServiceList [baseHomeDuration=" + baseHomeDuration
				+ ", baseShopDuration=" + baseShopDuration
				+ ", countHomeDuration=" + countHomeDuration
				+ ", countShopDuration=" + countShopDuration
				+ ", homeDuration=" + homeDuration + ", id=" + id + ", name="
				+ name + ", petId=" + petId + ", serviceId=" + serviceId
				+ ", shopDuration=" + shopDuration + ", size=" + size
				+ ", workerId=" + workerId + ", shopMaxTime=" + shopMaxTime
				+ ", shopMinTime=" + shopMinTime + ", homeMaxTime="
				+ homeMaxTime + ", homeMinTime=" + homeMinTime + "]";
	}

	public static ShopWorkerServiceList json2Entity(JSONObject json) {
		ShopWorkerServiceList shopWorkerServiceList = new ShopWorkerServiceList();
		try {
			if (json.has("homeMaxTime") && !json.isNull("homeMaxTime")) {
				shopWorkerServiceList.homeMaxTime = json.getInt("homeMaxTime");
			}
			if (json.has("homeMinTime") && !json.isNull("homeMinTime")) {
				shopWorkerServiceList.homeMinTime = json.getInt("homeMinTime");
			}
			if (json.has("shopMaxTime") && !json.isNull("shopMaxTime")) {
				shopWorkerServiceList.shopMaxTime = json.getInt("shopMaxTime");
			}
			if (json.has("shopMinTime") && !json.isNull("shopMinTime")) {
				shopWorkerServiceList.shopMinTime = json.getInt("shopMinTime");
			}
			if (json.has("baseHomeDuration")
					&& !json.isNull("baseHomeDuration")) {
				shopWorkerServiceList.baseHomeDuration = json
						.getInt("baseHomeDuration");
			}
			if (json.has("baseShopDuration")
					&& !json.isNull("baseShopDuration")) {
				shopWorkerServiceList.baseShopDuration = json
						.getInt("baseShopDuration");
			}
			if (json.has("homeDuration") && !json.isNull("homeDuration")) {
				shopWorkerServiceList.homeDuration = json
						.getInt("homeDuration");
			}
			if (json.has("id") && !json.isNull("id")) {
				shopWorkerServiceList.id = json.getInt("id");
			}
			if (json.has("petId") && !json.isNull("petId")) {
				shopWorkerServiceList.petId = json.getInt("petId");
			}
			if (json.has("serviceId") && !json.isNull("serviceId")) {
				shopWorkerServiceList.serviceId = json.getInt("serviceId");
			}
			if (json.has("shopDuration") && !json.isNull("shopDuration")) {
				shopWorkerServiceList.shopDuration = json
						.getInt("shopDuration");
			}
			if (json.has("size") && !json.isNull("size")) {
				shopWorkerServiceList.size = json.getInt("size");
			}
			if (json.has("workerId") && !json.isNull("workerId")) {
				shopWorkerServiceList.workerId = json.getInt("workerId");
			}
			if (json.has("name") && !json.isNull("name")) {
				shopWorkerServiceList.name = json.getString("name");
			}
			shopWorkerServiceList.countShopDuration = shopWorkerServiceList.baseShopDuration
					+ shopWorkerServiceList.shopDuration;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return shopWorkerServiceList;
	}
}
