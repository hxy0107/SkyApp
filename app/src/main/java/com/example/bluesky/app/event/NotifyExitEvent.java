package com.example.bluesky.app.event;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by bluesky on 16/6/19.
 */
public class NotifyExitEvent {
    SHARE_MEDIA share_media;

    public NotifyExitEvent(SHARE_MEDIA share_media) {
        this.share_media = share_media;
    }

    public SHARE_MEDIA getShare_media() {
        return share_media;
    }
}
