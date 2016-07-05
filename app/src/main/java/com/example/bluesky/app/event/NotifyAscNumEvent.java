package com.example.bluesky.app.event;

/**
 * Created by bluesky on 16/6/20.
 */
public class NotifyAscNumEvent {
    int pos;

    public NotifyAscNumEvent(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
