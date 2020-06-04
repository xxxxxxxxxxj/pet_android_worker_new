package com.haotang.petworker.entity;

import org.json.JSONObject;

/**
 * 美容师作品
 * 
 * @author Administrator
 * 
 */
public class Production {
	public int id;
	public int workerid;
	public String image;
	public String smallimage;
	public String title;
	public String des;
	public String time;
	public int auditState;

	public static Production json2Entity(JSONObject json) {
		Production pr = new Production();
		try {
			if (json.has("id") && json.has("id")) {
				pr.id = json.getInt("id");
			}
			if (json.has("workerId") && json.has("workerId")) {
				pr.workerid = json.getInt("workerId");
			}
			if (json.has("imgUrl") && json.has("imgUrl")
					&& !"".equals(json.getString("imgUrl").trim())) {
				pr.image = json.getString("imgUrl");
			}
			if (json.has("smallImgUrl") && json.has("smallImgUrl")
					&& !"".equals(json.getString("smallImgUrl").trim())) {
				pr.smallimage = json.getString("smallImgUrl");
			}
			if (json.has("title") && json.has("title")) {
				pr.title = json.getString("title");
			}
			if (json.has("description") && json.has("description")) {
				pr.des = json.getString("description");
			}
			if (json.has("created") && json.has("created")) {
				pr.time = json.getString("created");
			}
			if (json.has("auditState")&&!json.isNull("auditState")){
				pr.auditState = json.getInt("auditState");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}
}
