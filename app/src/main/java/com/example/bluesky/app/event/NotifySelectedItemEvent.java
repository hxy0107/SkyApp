package com.example.bluesky.app.event;

/**
 * Created by bluesky on 16/6/19.
 */
public class NotifySelectedItemEvent {
    int pos;

    public NotifySelectedItemEvent(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
