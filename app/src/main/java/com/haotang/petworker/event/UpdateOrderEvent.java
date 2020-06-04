package com.haotang.petworker.event;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-10 16:20
 */
public class UpdateOrderEvent {
    private boolean isRefersh;
    public UpdateOrderEvent(boolean isRefersh) {
        this.isRefersh = isRefersh;
    }

    public UpdateOrderEvent() {
    }

    public boolean isRefersh() {
        return isRefersh;
    }

    public void setRefersh(boolean refersh) {
        isRefersh = refersh;
    }
}
