package dgp.misaeng.global.util.enums;

public enum DeviceMode {
    GENERAL("일반"),
    AUTO("자동"),
    DEHUMID("제습"),
    SAVING("절전");

    private String message;

    DeviceMode(String message) {
        this.message = message;
    }
}
