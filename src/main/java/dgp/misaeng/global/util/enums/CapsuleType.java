package dgp.misaeng.global.util.enums;

public enum CapsuleType {
    MULTI("종합"),
    CARBS("탄수화물"),
    PROTEIN("단백질");

    private String message;

    CapsuleType(String message) {
        this.message = message;
    }
}
