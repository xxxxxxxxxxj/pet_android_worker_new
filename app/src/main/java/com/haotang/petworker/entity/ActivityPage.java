package com.haotang.petworker.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-01-07 16:19
 */
public class ActivityPage  implements Serializable {
    private String backup;
    private String activityPic;
    private int countType;
    private int point;
    private int id;
    private boolean isDelete;

    @Override
    public String toString() {
        return "ActivityPage{" +
                "backup='" + backup + '\'' +
                ", activityPic='" + activityPic + '\'' +
                ", countType=" + countType +
                ", point=" + point +
                ", id=" + id +
                ", isDelete=" + isDelete +
                '}';
    }

    public static ActivityPage json2Entity(JSONObject jobj) {
        ActivityPage activityPage = new ActivityPage();
        try {
            if (jobj.has("backup") && !jobj.isNull("backup")) {
                activityPage.setBackup(jobj.getString("backup"));
            }
            if (jobj.has("activityPic") && !jobj.isNull("activityPic")) {
                activityPage.setActivityPic(jobj.getString("activityPic"));
            }
            if (jobj.has("countType") && !jobj.isNull("countType")) {
                activityPage.setCountType(jobj.getInt("countType"));
            }
            if (jobj.has("point") && !jobj.isNull("point")) {
                activityPage.setPoint(jobj.getInt("point"));
            }
            if (jobj.has("id") && !jobj.isNull("id")) {
                activityPage.setId(jobj.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activityPage;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getActivityPic() {
        return activityPic;
    }

    public void setActivityPic(String activityPic) {
        this.activityPic = activityPic;
    }

    public int getCountType() {
        return countType;
    }

    public void setCountType(int countType) {
        this.countType = countType;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
