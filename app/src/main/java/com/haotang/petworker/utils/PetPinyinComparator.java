package com.haotang.petworker.utils;

import com.haotang.petworker.entity.NewPetBean;

import java.util.Comparator;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/6/20 15:54
 */
public class PetPinyinComparator implements Comparator<NewPetBean> {

    public int compare(NewPetBean o1, NewPetBean o2) {
        if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}