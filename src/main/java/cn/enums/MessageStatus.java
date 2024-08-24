package cn.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum MessageStatus {
    NotRead(0, "未读"),
    Read(1, "已读"),
    Agree(2, "同意"),
    Reject(3, "拒绝");

    private final int code;
    private final String value;

    MessageStatus(int code, String value) {
        this.value = value;
        this.code = code;
    }

    public static MessageStatus fromValue(String value) {
        for (MessageStatus deviceType : MessageStatus.values()) {
            if (Objects.equals(deviceType.getValue(), value)) {
                return deviceType;
            }
        }
        return null;
    }

    public static MessageStatus fromCode(int code) {
        for (MessageStatus deviceType : MessageStatus.values()) {
            if (deviceType.getCode() == code) {
                return deviceType;
            }
        }
        return null;
    }
}