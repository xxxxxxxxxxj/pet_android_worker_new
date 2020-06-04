package com.haotang.petworker.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeWorkerServiceList implements Serializable {
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
	public int homeMaxTime;
	public int homeMinTime;
	public int shopMaxTime;
	public int shopMinTime;

	@Override
	public String toString() {
		return "HomeWorkerServiceList [baseHomeDuration=" + baseHomeDuration
				+ ", baseShopDuration=" + baseShopDuration
				+ ", countHomeDuration=" + countHomeDuration
				+ ", countShopDuration=" + countShopDuration
				+ ", homeDuration=" + homeDuration + ", id=" + id + ", name="
				+ name + ", petId=" + petId + ", serviceId=" + serviceId
				+ ", shopDuration=" + shopDuration + ", size=" + size
				+ ", workerId=" + workerId + ", homeMaxTime=" + homeMaxTime
				+ ", homeMinTime=" + homeMinTime + ", shopMaxTime="
				+ shopMaxTime + ", shopMinTime=" + shopMinTime + "]";
	}

	public static HomeWorkerServiceList json2Entity(JSONObject json) {
		HomeWorkerServiceList homeWorkerServiceList = new HomeWorkerServiceList();
		try {
			if (json.has("shopMaxTime") && !json.isNull("shopMaxTime")) {
				homeWorkerServiceList.shopMaxTime = json.getInt("shopMaxTime");
			}
			if (json.has("shopMinTime") && !json.isNull("shopMinTime")) {
				homeWorkerServiceList.shopMinTime = json.getInt("shopMinTime");
			}
			if (json.has("homeMaxTime") && !json.isNull("homeMaxTime")) {
				homeWorkerServiceList.homeMaxTime = json.getInt("homeMaxTime");
			}
			if (json.has("homeMinTime") && !json.isNull("homeMinTime")) {
				homeWorkerServiceList.homeMinTime = json.getInt("homeMinTime");
			}
			if (json.has("baseHomeDuration")
					&& !json.isNull("baseHomeDuration")) {
				homeWorkerServiceList.baseHomeDuration = json
						.getInt("baseHomeDuration");
			}
			if (json.has("baseShopDuration")
					&& !json.isNull("baseShopDuration")) {
				homeWorkerServiceList.baseShopDuration = json
						.getInt("baseShopDuration");
			}
			if (json.has("homeDuration") && !json.isNull("homeDuration")) {
				homeWorkerServiceList.homeDuration = json
						.getInt("homeDuration");
			}
			if (json.has("id") && !json.isNull("id")) {
				homeWorkerServiceList.id = json.getInt("id");
			}
			if (json.has("petId") && !json.isNull("petId")) {
				homeWorkerServiceList.petId = json.getInt("petId");
			}
			if (json.has("serviceId") && !json.isNull("serviceId")) {
				homeWorkerServiceList.serviceId = json.getInt("serviceId");
			}
			if (json.has("shopDuration") && !json.isNull("shopDuration")) {
				homeWorkerServiceList.shopDuration = json
						.getInt("shopDuration");
			}
			if (json.has("size") && !json.isNull("size")) {
				homeWorkerServiceList.size = json.getInt("size");
			}
			if (json.has("workerId") && !json.isNull("workerId")) {
				homeWorkerServiceList.workerId = json.getInt("workerId");
			}
			if (json.has("name") && !json.isNull("name")) {
				homeWorkerServiceList.name = json.getString("name");
			}
			homeWorkerServiceList.countHomeDuration = homeWorkerServiceList.baseHomeDuration
					+ homeWorkerServiceList.homeDuration;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return homeWorkerServiceList;
	}
}
