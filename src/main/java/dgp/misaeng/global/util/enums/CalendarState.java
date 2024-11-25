package dgp.misaeng.global.util.enums;

public enum CalendarState {
    FORBIDDEN("금지 음식"),
    EMPTY("자리 비움"),
    COMPLETE("미생물 분해");

    private String message;

    CalendarState(String message) {
        this.message = message;
    }
}
