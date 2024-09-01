package cn.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum MessageType {
    simple_message(0, "simple_message"),
    system_info(1, "system_info"),
    system_notice(2, "system_notice"),
    disk_expansion_application(3, "disk_expansion_application"),
    heartbeat(4, "heartbeat"),
    disk_expansion_application_deal(5, "disk_expansion_application_deal");

    private final int code;
    private final String value;

    MessageType(int code, String value) {
        this.value = value;
        this.code = code;
    }

    public static MessageType fromValue(String value) {
        for (MessageType deviceType : MessageType.values()) {
            if (Objects.equals(deviceType.getValue(), value)) {
                return deviceType;
            }
        }
        return null;
    }

    public static MessageType fromCode(int code) {
        for (MessageType deviceType : MessageType.values()) {
            if (deviceType.getCode() == code) {
                return deviceType;
            }
        }
        return null;
    }
}