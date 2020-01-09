package com.lyloou.flow.util;

import com.lyloou.flow.model.FlowItem;

import java.util.Collections;
import java.util.List;

public class Utransfer {
    public static void sortItems(List<FlowItem> items) {
        Collections.sort(items, (o1, o2) -> {
            if (o1 == null || o2 == null) {
                return -1;
            }
            String o1TimeStart = o1.getTimeStart();
            String o2TimeStart = o2.getTimeStart();
            if (o1TimeStart == null || o2TimeStart == null) {
                return -1;
            }
            return o2TimeStart.compareTo(o1TimeStart);
        });
    }
}
