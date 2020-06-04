package com.haotang.petworker.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/10
 * @Description:
 */
public class IntegralDerail {


    /**
     * code : 0
     * data : {"integralWaterBill":[{"showName":"获得粉丝好评+1.0","amountStr":"+1.0","created":"2019-09-08 11:15","name":"获得粉丝好评"},{"showName":"获得粉丝好评+1.0","amountStr":"+1.0","created":"2019-09-08 10:50","name":"获得粉丝好评"},{"showName":"获得粉丝好评+1.0","amountStr":"+1.0","created":"2019-09-08 10:36","name":"获得粉丝好评"},{"showName":"获得粉丝好评+1.0","amountStr":"+1.0","created":"2019-09-08 10:32","name":"获得粉丝好评"}]}
     * msg : 操作成功
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<IntegralWaterBillBean> integralWaterBill;

        public List<IntegralWaterBillBean> getIntegralWaterBill() {
            return integralWaterBill;
        }

        public void setIntegralWaterBill(List<IntegralWaterBillBean> integralWaterBill) {
            this.integralWaterBill = integralWaterBill;
        }

        public static class IntegralWaterBillBean {
            /**
             * showName : 获得粉丝好评+1.0
             * amountStr : +1.0
             * created : 2019-09-08 11:15
             * name : 获得粉丝好评
             */

            private String showName;
            private String amountStr;
            private String created;
            private String name;

            public String getShowName() {
                return showName;
            }

            public void setShowName(String showName) {
                this.showName = showName;
            }

            public String getAmountStr() {
                return amountStr;
            }

            public void setAmountStr(String amountStr) {
                this.amountStr = amountStr;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
