package dgp.misaeng.global.util.enums;

public enum MicrobeMood {
    SMILE("기분 좋음"),
    BAD("기분 나쁨");

    private String message;

    MicrobeMood(String message) {
        this.message = message;
    }
}
