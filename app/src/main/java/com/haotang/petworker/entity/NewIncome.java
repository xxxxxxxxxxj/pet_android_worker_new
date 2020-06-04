package com.haotang.petworker.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/1
 * @Description:
 */
public class NewIncome {


    /**
     * code : 0
     * data : {"totalIncome":0,"months":[{"month":"2019-11-01","isactive":1},{"month":"2019-10-01","isactive":0},{"month":"2019-09-01","isactive":0}],"incomeExplain":"因结算周期原因，请在当月第一周结束后查看上月奖励薪资","monthDayInfo":"本月截止至2019-11-04","list":[{"containInfo":"包含3项：服务订单、卡销售、其他提成","amount":"0.00","name":"提成","list":[{"amount":"0.00","instro":"推荐学员，推荐人奖励500元；推荐成手，推荐人奖励1000元；","name":"服务订单"},{"amount":"0.00","name":"卡销售"},{"containInfo":"包含4项：犬证、体内驱虫、体外驱虫、其他","amount":"0.00","name":"其他提成","list":[{"amount":"0.00","name":"犬证"},{"amount":"0.00","name":"体内驱虫"},{"amount":"0.00","name":"体外驱虫"},{"amount":"0.00","name":"其他提成"}]}]},{"containInfo":"包含2项：奖励、补助","amount":"0.00","name":"奖励/补助","list":[{"containInfo":"包含4项：推荐奖、指导人奖、十三薪、其他奖励","amount":"0.00","name":"奖励","list":[{"amount":"0.00","instro":"推荐学员，推荐人奖励500元；推荐成手，推荐人奖励1000元；","name":"推荐奖"},{"amount":"0.00","instro":"指导人有奖励您知道吗？小伙伴们抓紧荣升指导人哦！","name":"指导新人"},{"amount":"0.00","instro":"小伙伴们每月按要求出勤，就可拿到本月匹配到的十三薪部分，为了拿十三薪加油吧！","name":"十三薪"},{"amount":"0.00","name":"其他奖励"}]},{"containInfo":"包含2项：新店补助、其他补助","amount":"0.00","name":"补助","list":[{"amount":"0.00","instro":"新人和/或新店人员前3个月不参予第一档提成考核这么大的福利您是否知悉？","name":"新店补助"},{"amount":"0.00","name":"其他补助"}]}]},{"containInfo":"包含2项：+其他、-其他","amount":"0.00","name":"+ - 其他","list":[{"containInfo":"包含1项：打赏","amount":"0.00","name":"+其他","list":[{"amount":"0.00","name":"打赏"}]},{"containInfo":"包含6项：个税、社保、工具扣款、处罚扣款、差评扣款、其他扣款","amount":"0.00","name":"-其他","list":[{"amount":"0.00","name":"个税"},{"amount":"0.00","name":"社保"},{"amount":"0.00","name":"工具扣款"},{"amount":"0.00","name":"处罚扣款"},{"amount":"0.00","name":"差评扣款"},{"amount":"0.00","name":"其他扣款"}]}]}]}
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

    public static class DataBean implements Serializable{
        /**
         * totalIncome : 0
         * months : [{"month":"2019-11-01","isactive":1},{"month":"2019-10-01","isactive":0},{"month":"2019-09-01","isactive":0}]
         * incomeExplain : 因结算周期原因，请在当月第一周结束后查看上月奖励薪资
         * monthDayInfo : 本月截止至2019-11-04
         * list : [{"containInfo":"包含3项：服务订单、卡销售、其他提成","amount":"0.00","name":"提成","list":[{"amount":"0.00","instro":"推荐学员，推荐人奖励500元；推荐成手，推荐人奖励1000元；","name":"服务订单"},{"amount":"0.00","name":"卡销售"},{"containInfo":"包含4项：犬证、体内驱虫、体外驱虫、其他","amount":"0.00","name":"其他提成","list":[{"amount":"0.00","name":"犬证"},{"amount":"0.00","name":"体内驱虫"},{"amount":"0.00","name":"体外驱虫"},{"amount":"0.00","name":"其他提成"}]}]},{"containInfo":"包含2项：奖励、补助","amount":"0.00","name":"奖励/补助","list":[{"containInfo":"包含4项：推荐奖、指导人奖、十三薪、其他奖励","amount":"0.00","name":"奖励","list":[{"amount":"0.00","instro":"推荐学员，推荐人奖励500元；推荐成手，推荐人奖励1000元；","name":"推荐奖"},{"amount":"0.00","instro":"指导人有奖励您知道吗？小伙伴们抓紧荣升指导人哦！","name":"指导新人"},{"amount":"0.00","instro":"小伙伴们每月按要求出勤，就可拿到本月匹配到的十三薪部分，为了拿十三薪加油吧！","name":"十三薪"},{"amount":"0.00","name":"其他奖励"}]},{"containInfo":"包含2项：新店补助、其他补助","amount":"0.00","name":"补助","list":[{"amount":"0.00","instro":"新人和/或新店人员前3个月不参予第一档提成考核这么大的福利您是否知悉？","name":"新店补助"},{"amount":"0.00","name":"其他补助"}]}]},{"containInfo":"包含2项：+其他、-其他","amount":"0.00","name":"+ - 其他","list":[{"containInfo":"包含1项：打赏","amount":"0.00","name":"+其他","list":[{"amount":"0.00","name":"打赏"}]},{"containInfo":"包含6项：个税、社保、工具扣款、处罚扣款、差评扣款、其他扣款","amount":"0.00","name":"-其他","list":[{"amount":"0.00","name":"个税"},{"amount":"0.00","name":"社保"},{"amount":"0.00","name":"工具扣款"},{"amount":"0.00","name":"处罚扣款"},{"amount":"0.00","name":"差评扣款"},{"amount":"0.00","name":"其他扣款"}]}]}]
         */

        private double totalIncome;
        private String incomeExplain;
        private String monthDayInfo;
        private List<MonthsBean> months;
        private List<ListBeanXX> list;

        public double getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(double totalIncome) {
            this.totalIncome = totalIncome;
        }

        public String getIncomeExplain() {
            return incomeExplain;
        }

        public void setIncomeExplain(String incomeExplain) {
            this.incomeExplain = incomeExplain;
        }

        public String getMonthDayInfo() {
            return monthDayInfo;
        }

        public void setMonthDayInfo(String monthDayInfo) {
            this.monthDayInfo = monthDayInfo;
        }

        public List<MonthsBean> getMonths() {
            return months;
        }

        public void setMonths(List<MonthsBean> months) {
            this.months = months;
        }

        public List<ListBeanXX> getList() {
            return list;
        }

        public void setList(List<ListBeanXX> list) {
            this.list = list;
        }

        public static class MonthsBean {
            /**
             * month : 2019-11-01
             * isactive : 1
             */

            private String month;
            private int isactive;

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public int getIsactive() {
                return isactive;
            }

            public void setIsactive(int isactive) {
                this.isactive = isactive;
            }
        }

        public static class ListBeanXX implements Serializable {
            /**
             * containInfo : 包含3项：服务订单、卡销售、其他提成
             * amount : 0.00
             * name : 提成
             * list : [{"amount":"0.00","instro":"推荐学员，推荐人奖励500元；推荐成手，推荐人奖励1000元；","name":"服务订单"},{"amount":"0.00","name":"卡销售"},{"containInfo":"包含4项：犬证、体内驱虫、体外驱虫、其他","amount":"0.00","name":"其他提成","list":[{"amount":"0.00","name":"犬证"},{"amount":"0.00","name":"体内驱虫"},{"amount":"0.00","name":"体外驱虫"},{"amount":"0.00","name":"其他提成"}]}]
             */

            private String containInfo;
            private String amount;
            private String name;
            private String role;
            private String royaltyPic;
            private String calculateExplain;
            private int type;
            private String workyear;
            private List<ListBeanX> list;
            private int tid;                //美容师等级 0：初级 1：中级 2：高级 3：首席
            private WorkerScheduleList workerScheduleList;

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public void setTid(int tid) {
                this.tid = tid;
            }

            public ListBeanXX.WorkerScheduleList getWorkerScheduleList() {
                return workerScheduleList;
            }

            public void setWorkerScheduleList(ListBeanXX.WorkerScheduleList workerScheduleList) {
                this.workerScheduleList = workerScheduleList;
            }

            public String getRoyaltyPic() {
                return royaltyPic;
            }

            public void setRoyaltyPic(String royaltyPic) {
                this.royaltyPic = royaltyPic;
            }

            public String getCalculateExplain() {
                return calculateExplain;
            }

            public void setCalculateExplain(String calculateExplain) {
                this.calculateExplain = calculateExplain;
            }

            public int getTid() {
                return tid;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getWorkyear() {
                return workyear;
            }

            public void setWorkyear(String workyear) {
                this.workyear = workyear;
            }

            public String getContainInfo() {
                return containInfo;
            }

            public void setContainInfo(String containInfo) {
                this.containInfo = containInfo;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ListBeanX> getList() {
                return list;
            }

            public void setList(List<ListBeanX> list) {
                this.list = list;
            }

            public static class ListBeanX implements Serializable{
                /**
                 * amount : 0.00
                 * instro : 推荐学员，推荐人奖励500元；推荐成手，推荐人奖励1000元；
                 * name : 服务订单
                 * containInfo : 包含4项：犬证、体内驱虫、体外驱虫、其他
                 * list : [{"amount":"0.00","name":"犬证"},{"amount":"0.00","name":"体内驱虫"},{"amount":"0.00","name":"体外驱虫"},{"amount":"0.00","name":"其他提成"}]
                 */

                private String amount;
                private int saleAmount;
                private String royalty;
                private String instro;
                private String name;
                private String containInfo;
                private List<ListBean> list;
                private List<WorkerRotaltyInfoMode> workerRotaltyInfo;

                public int getSaleAmount() {
                    return saleAmount;
                }

                public void setSaleAmount(int saleAmount) {
                    this.saleAmount = saleAmount;
                }

                public String getRoyalty() {
                    return royalty;
                }

                public void setRoyalty(String royalty) {
                    this.royalty = royalty;
                }

                public List<WorkerRotaltyInfoMode> getWorkerRotaltyInfoMap() {
                    return workerRotaltyInfo;
                }

                public void setWorkerRotaltyInfoMap(List<WorkerRotaltyInfoMode> workerRotaltyInfoMap) {
                    this.workerRotaltyInfo = workerRotaltyInfoMap;
                }

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }

                public String getInstro() {
                    return instro;
                }

                public void setInstro(String instro) {
                    this.instro = instro;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getContainInfo() {
                    return containInfo;
                }

                public void setContainInfo(String containInfo) {
                    this.containInfo = containInfo;
                }

                public List<ListBean> getList() {
                    return list;
                }

                public void setList(List<ListBean> list) {
                    this.list = list;
                }

                public static class ListBean implements Serializable{
                    /**
                     * amount : 0.00
                     * name : 犬证
                     */

                    private String amount;
                    private String name;
                    private String instro;
                    private String otherDates;

                    public String getOtherDates() {
                        return otherDates;
                    }

                    public void setOtherDates(String otherDates) {
                        this.otherDates = otherDates;
                    }

                    public String getInstro() {
                        return instro;
                    }

                    public void setInstro(String instro) {
                        this.instro = instro;
                    }

                    public String getAmount() {
                        return amount;
                    }

                    public void setAmount(String amount) {
                        this.amount = amount;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }
            }

            public static class WorkerScheduleList implements Serializable{
                private String name;
                private List<ScheduleDay> scheduleDay;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<ScheduleDay> getScheduleDay() {
                    return scheduleDay;
                }

                public void setScheduleDay(List<ScheduleDay> scheduleDay) {
                    this.scheduleDay = scheduleDay;
                }
            }
        }
    }
}
