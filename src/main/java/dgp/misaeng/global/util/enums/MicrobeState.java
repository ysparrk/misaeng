package dgp.misaeng.global.util.enums;

public enum MicrobeState {
    FORBIDDEN("금지 음식"),
    EMPTY("자리 비움"),
    COMPLETE("미생물 분해 완료"),
    PROCESSING("미생물 분해 중");

    private String message;

    MicrobeState(String message) {
        this.message = message;
    }
}
