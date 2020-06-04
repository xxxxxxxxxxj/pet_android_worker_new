package com.haotang.petworker.entity;

import com.haotang.petworker.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Notice implements Serializable {

	public String title;// 标题
	public long AnnouncementId;//
	public String created;//
	public String content;
	public String url;

	public Notice() {
	}

	public Notice(String title, long announcementId, String created, String content, String url) {
		this.title = title;
		AnnouncementId = announcementId;
		this.created = created;
		this.content = content;
		this.url = url;
	}

	public static Notice json2Entity(JSONObject json) {
		Notice notice = new Notice();
		try {
			if (json.has("title") && !json.isNull("title")) {
				notice.title = json.getString("title");
			}
			if (json.has("content") && !json.isNull("content")) {
				notice.content = json.getString("content");
			}
			if (json.has("url") && !json.isNull("url")) {
				notice.url = json.getString("url");
			}
			if (json.has("created") && !json.isNull("created")) {
				notice.created = Utils.ChangeTime(json.getString("created"));
			}
			if (json.has("id") && !json.isNull("id")) {
				notice.AnnouncementId = json.getLong("id");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notice;

	}
}
