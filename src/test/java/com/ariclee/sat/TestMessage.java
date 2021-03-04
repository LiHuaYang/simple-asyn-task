package com.ariclee.sat;

import com.ariclee.sat.DelayTaskBaseMessage;

/**
 * @author lihy
 * @version 1.0  2021/3/4
 */
public class TestMessage extends DelayTaskBaseMessage {

    private String content;

    public TestMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
