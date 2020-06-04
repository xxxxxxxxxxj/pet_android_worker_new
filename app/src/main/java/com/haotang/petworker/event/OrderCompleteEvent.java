package com.haotang.petworker.event;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-10 16:33
 */
public class OrderCompleteEvent {
    private int orderId;
    public OrderCompleteEvent(int orderId) {
        this.orderId = orderId;
    }

    public OrderCompleteEvent() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
