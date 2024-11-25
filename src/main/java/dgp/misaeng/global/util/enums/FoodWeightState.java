package dgp.misaeng.global.util.enums;

public enum FoodWeightState {
    GOOD("적절"),
    FULL("음식 과다");

    private String message;

    FoodWeightState(String message) {
        this.message = message;
    }
}
