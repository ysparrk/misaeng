package dgp.misaeng.global.util.enums;

public enum MicrobeMessage {
    GOOD("건강"),
    BAD("안좋음"),
    FULL("음식 과다"),
    DANGER("위험");

    private String message;

    MicrobeMessage(String message) {
        this.message = message;
    }
}
