package dgp.misaeng.global.util.enums;

public enum MicrobeSoilCondition {
    OILY("기름짐"),
    DUSTY("가루날림"),
    MOLDY("곰팡이 발생");

    private String message;

    MicrobeSoilCondition(String message) {
        this.message = message;
    }
}
