package dgp.misaeng.global.util.enums;

public enum FoodCategory {
    KIMCHI("김치 및 절임류"),
    RICE("밥/주식 및 면류"),
    STIR_FRIED("볶음/구이 및 조림류"),
    FRIED("튀김 및 전/부침류"),
    SEAFOOD("해산물 요리"),
    SALAD("샐러드 및 과채류"),
    BREAD("빵 및 곡류"),
    DAIRY("유제품/계란 및 디저트"),
    OTHER("기타 음식 및 간식"),
    NONE_FOOD("음식이 아닌 항목");

    private String message;

    FoodCategory(String message) {
        this.message = message;
    }

}
