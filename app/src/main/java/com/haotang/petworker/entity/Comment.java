package com.haotang.petworker.entity;

import org.json.JSONArray;
import org.json.JSONObject;

public class Comment {
	public int id;
	public String image;
	public String name;
	public String content;
	public String time;
	public int grade;
	public String commentTags;
	public String userAvatar;
	public String userName;
	public int level = -1;// 评论级别 0：差评 1：中评 2：好评 -1：未评分
	public double specialty;// 专业评分
	public double service;// 服务评分
	public double ontime;// 守时评分
	public String[] images;// 作品图片
	public String orderNum;
	public String startTime;

	public static Comment json2Entity(JSONObject json) {
		Comment comment = new Comment();
		try {
			if (json.has("id") && !json.isNull("id")) {
				comment.id = json.getInt("id");
			}
			if (json.has("avatar") && !json.isNull("avatar")
					&& !"".equals(json.getString("avatar").trim())) {
				comment.image = json.getString("avatar");
			}
			if (json.has("realName") && !json.isNull("realName")) {
				comment.name = json.getString("realName");
			}
			if (json.has("content") && !json.isNull("content")) {
				comment.content = json.getString("content");
			}
			if (json.has("created") && !json.isNull("created")) {
				String time = json.getString("created");
				comment.time = formatTime(time);

			}
			if (json.has("credit") && !json.isNull("credit")) {
				comment.level = json.getInt("credit");

			}
			if (json.has("grade") && !json.isNull("grade")) {
				comment.grade = json.getInt("grade");

			}
			if (json.has("commentTags") && !json.isNull("commentTags")) {
				comment.commentTags = json.getString("commentTags");

			}


			if (json.has("grade1") && !json.isNull("grade1")) {
				comment.specialty = json.getDouble("grade1");

			}
			if (json.has("grade2") && !json.isNull("grade2")) {
				comment.service = json.getDouble("grade2");

			}
			if (json.has("grade3") && !json.isNull("grade3")) {
				comment.ontime = json.getDouble("grade3");

			}

			if (json.has("orderNum") && !json.isNull("orderNum")) {
				comment.orderNum = json.getString("orderNum");

			}

			if (json.has("user")&&!json.isNull("user")){
				JSONObject user = json.getJSONObject("user");
				if (user.has("avatar")&&!user.isNull("avatar")){
					comment.userAvatar = user.getString("avatar");
				}
				if (user.has("userName")&&!user.isNull("userName")){
					comment.userName = user.getString("userName");
				}
			}

			if (json.has("startTime") && !json.isNull("startTime")) {
				comment.startTime = json.getString("startTime");

			}
			if (json.has("imgList") && !json.isNull("imgList")) {
				JSONArray arr = json.getJSONArray("imgList");
				int length = arr.length() > 3 ? 3 : arr.length();
				if (length > 0) {
					comment.images = new String[length];
				}
				for (int i = 0; i < length; i++) {
					comment.images[i] = arr.getString(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comment;
	}

	private static String formatTime(String time) {
		time = time.replace("年", "-");
		time = time.replace("月", "-");
		time = time.replace("日", " ");
		time = time.replace("时", ":");
		time = time.replace("分", "");
		return time;
	}
}
