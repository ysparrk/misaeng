package dgp.misaeng.global.util.enums;

public enum EnvironmentState {
    HIGH("높음"),
    GOOD("적절"),
    LOW("낮음");

    private String message;

    EnvironmentState(String message) {
        this.message = message;
    }
}
