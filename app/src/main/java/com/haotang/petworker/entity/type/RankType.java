package com.haotang.petworker.entity.type;

/**
 * 作者：灼星
 * 时间：2020-04-20
 * 级别类型
 */
public enum RankType {
    primary(0,"初级"),middleRank(1,"中级"),
    heightRank(2,"高级"),seatOfHonour(3,"首席");
    private int type;
    private String name;

    RankType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String getName(int type){
        switch (type){
            case 3:
                return seatOfHonour.getName();
            case 2:
                return heightRank.getName();
            case 1:
                return middleRank.getName();
            case 0:
                return primary.getName();
        }

        return "暂无此级别";
    }
}
