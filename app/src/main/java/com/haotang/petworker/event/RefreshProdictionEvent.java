package com.haotang.petworker.event;

/**
 * @author:姜谷蓄
 * @Date:2019/10/17
 * @Description:
 */
public class RefreshProdictionEvent {
        private boolean isRefresh;

        public RefreshProdictionEvent() {
        }

        public RefreshProdictionEvent(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        public boolean isRefresh() {
            return isRefresh;
        }

        public void setRefresh(boolean refresh) {
            isRefresh = refresh;
        }
}
