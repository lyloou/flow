package com.lyloou.flow.util;

public interface PermissionListener {
    String name();

    Runnable whenShouldShowRequest();

    Runnable whenGranted();
}
