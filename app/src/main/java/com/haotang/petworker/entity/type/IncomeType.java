package com.haotang.petworker.entity.type;

/**
 * 作者：灼星
 * 时间：2020-04-20
 * 收入类型
 */
public enum IncomeType {
    COMMON_INCOME(0),PUSH_MONEY(1),ALLOWANCE(2),OTHER(3);
    private int type;
    private IncomeType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }}
