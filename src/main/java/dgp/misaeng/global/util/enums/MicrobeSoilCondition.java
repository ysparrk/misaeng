package dgp.misaeng.global.util.enums;

public enum MicrobeSoilCondition {
    NORMAL("정상 상태"),
    OILY("기름짐 발생"),
    DUSTY("가루 날림 발생"),
    MOLDY("곰팡이 발생");

    private String message;

    MicrobeSoilCondition(String message) {
        this.message = message;
    }
}
