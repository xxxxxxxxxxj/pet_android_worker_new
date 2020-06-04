package com.haotang.petworker.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/7
 * @Description:
 */
public class RankingListDetail {


    /**
     * code : 0
     * data : {"shopList":[{"realName":"孙群铸","workerId":1369,"shopName":"回龙观店","avatar":"/static/avatar/1521456217712_616.jpg?v=2","shopId":19,"income":19.88},{"realName":"张记鲁","workerId":1414,"shopName":"回龙观店","avatar":"/static/avatar/1530786360335_542.jpg?v=2","shopId":19,"income":19.88}],"shopNum":2,"cityAmount":121.1,"shopAmount":121.1,"noRanking":"暂无排名","cityNum":2,"list":[{"realName":"孙群铸","workerId":1369,"shopName":"回龙观店","avatar":"/static/avatar/1521456217712_616.jpg?v=2","shopId":19,"income":19.88},{"realName":"张记鲁","workerId":1414,"shopName":"回龙观店","avatar":"/static/avatar/1530786360335_542.jpg?v=2","shopId":19,"income":19.88},{"realName":"李新","workerId":1594,"shopName":"回龙观店","avatar":"/static/avatar/1555060000650_819.jpg","shopId":11,"income":19.88}],"workerInfo":{"avatar":"/static/avatar/1530786360335_542.jpg?v=2","realName":"张记鲁"}}
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
        /**
         * shopList : [{"realName":"孙群铸","workerId":1369,"shopName":"回龙观店","avatar":"/static/avatar/1521456217712_616.jpg?v=2","shopId":19,"income":19.88},{"realName":"张记鲁","workerId":1414,"shopName":"回龙观店","avatar":"/static/avatar/1530786360335_542.jpg?v=2","shopId":19,"income":19.88}]
         * shopNum : 2
         * cityAmount : 121.1
         * shopAmount : 121.1
         * noRanking : 暂无排名
         * cityNum : 2
         * list : [{"realName":"孙群铸","workerId":1369,"shopName":"回龙观店","avatar":"/static/avatar/1521456217712_616.jpg?v=2","shopId":19,"income":19.88},{"realName":"张记鲁","workerId":1414,"shopName":"回龙观店","avatar":"/static/avatar/1530786360335_542.jpg?v=2","shopId":19,"income":19.88},{"realName":"李新","workerId":1594,"shopName":"回龙观店","avatar":"/static/avatar/1555060000650_819.jpg","shopId":11,"income":19.88}]
         * workerInfo : {"avatar":"/static/avatar/1530786360335_542.jpg?v=2","realName":"张记鲁"}
         */

        private int shopNum;
        private double cityAmount;
        private double shopAmount;
        private String noRanking;
        private int cityNum;
        private WorkerInfoBean workerInfo;
        private List<ShopListBean> shopList;
        private List<ListBean> list;

        public int getShopNum() {
            return shopNum;
        }

        public void setShopNum(int shopNum) {
            this.shopNum = shopNum;
        }

        public double getCityAmount() {
            return cityAmount;
        }

        public void setCityAmount(double cityAmount) {
            this.cityAmount = cityAmount;
        }

        public double getShopAmount() {
            return shopAmount;
        }

        public void setShopAmount(double shopAmount) {
            this.shopAmount = shopAmount;
        }

        public String getNoRanking() {
            return noRanking;
        }

        public void setNoRanking(String noRanking) {
            this.noRanking = noRanking;
        }

        public int getCityNum() {
            return cityNum;
        }

        public void setCityNum(int cityNum) {
            this.cityNum = cityNum;
        }

        public WorkerInfoBean getWorkerInfo() {
            return workerInfo;
        }

        public void setWorkerInfo(WorkerInfoBean workerInfo) {
            this.workerInfo = workerInfo;
        }

        public List<ShopListBean> getShopList() {
            return shopList;
        }

        public void setShopList(List<ShopListBean> shopList) {
            this.shopList = shopList;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class WorkerInfoBean {
            /**
             * avatar : /static/avatar/1530786360335_542.jpg?v=2
             * realName : 张记鲁
             */

            private String avatar;
            private String realName;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }
        }

        public static class ShopListBean {
            /**
             * realName : 孙群铸
             * workerId : 1369
             * shopName : 回龙观店
             * avatar : /static/avatar/1521456217712_616.jpg?v=2
             * shopId : 19
             * income : 19.88
             */

            private String realName;
            private int workerId;
            private String shopName;
            private String avatar;
            private int shopId;
            private double income;

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public int getWorkerId() {
                return workerId;
            }

            public void setWorkerId(int workerId) {
                this.workerId = workerId;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getShopId() {
                return shopId;
            }

            public void setShopId(int shopId) {
                this.shopId = shopId;
            }

            public double getIncome() {
                return income;
            }

            public void setIncome(double income) {
                this.income = income;
            }
        }

        public static class ListBean {
            /**
             * realName : 孙群铸
             * workerId : 1369
             * shopName : 回龙观店
             * avatar : /static/avatar/1521456217712_616.jpg?v=2
             * shopId : 19
             * income : 19.88
             */

            private String realName;
            private int workerId;
            private String shopName;
            private String avatar;
            private int shopId;
            private double income;

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public int getWorkerId() {
                return workerId;
            }

            public void setWorkerId(int workerId) {
                this.workerId = workerId;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getShopId() {
                return shopId;
            }

            public void setShopId(int shopId) {
                this.shopId = shopId;
            }

            public double getIncome() {
                return income;
            }

            public void setIncome(double income) {
                this.income = income;
            }
        }
    }
}
