package com.haotang.petworker.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/4/20
 * @Description:
 */
public class ServiceOrderDetail {

    /**
     * code : 0
     * data : {"data":[{"date":"2020-04-01","totalPrice":44.5,"orderNum":2,"serviceInfo":[{"servicePrice":42,"appointment":"14:47:0","serviceName":"美容套餐"},{"servicePrice":2.5,"appointment":"16:47:0","serviceName":"美容套餐"}]},{"date":"2020-04-02","totalPrice":103,"orderNum":2,"serviceInfo":[{"servicePrice":51.5,"appointment":"12:30:0","serviceName":"洗护套餐"},{"servicePrice":51.5,"appointment":"16:00:0","serviceName":"洗护套餐"}]}]}
     * msg : 操作成功
     */

    private int code;
    private DataBeanX data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBeanX {
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * date : 2020-04-01
             * totalPrice : 44.5
             * orderNum : 2
             * serviceInfo : [{"servicePrice":42,"appointment":"14:47:0","serviceName":"美容套餐"},{"servicePrice":2.5,"appointment":"16:47:0","serviceName":"美容套餐"}]
             */

            private String date;
            private double totalPrice;
            private int orderNum;
            private boolean isSelected;
            private List<ServiceInfoBean> serviceInfo;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public double getTotalPrice() {
                return totalPrice;
            }

            public void setTotalPrice(double totalPrice) {
                this.totalPrice = totalPrice;
            }

            public int getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(int orderNum) {
                this.orderNum = orderNum;
            }

            public List<ServiceInfoBean> getServiceInfo() {
                return serviceInfo;
            }

            public void setServiceInfo(List<ServiceInfoBean> serviceInfo) {
                this.serviceInfo = serviceInfo;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public static class ServiceInfoBean {
                /**
                 * servicePrice : 42
                 * appointment : 14:47:0
                 * serviceName : 美容套餐
                 */

                private double servicePrice;
                private String appointment;
                private String serviceName;

                public double getServicePrice() {
                    return servicePrice;
                }

                public void setServicePrice(double servicePrice) {
                    this.servicePrice = servicePrice;
                }

                public String getAppointment() {
                    return appointment;
                }

                public void setAppointment(String appointment) {
                    this.appointment = appointment;
                }

                public String getServiceName() {
                    return serviceName;
                }

                public void setServiceName(String serviceName) {
                    this.serviceName = serviceName;
                }
            }
        }
    }
}
