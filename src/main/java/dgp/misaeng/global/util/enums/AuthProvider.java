package dgp.misaeng.global.util.enums;

public enum AuthProvider {
    KAKAO("카카오"),
    NAVER("네이버"),
    GOOGLE("구글");

    private String message;

    AuthProvider(String message) {
        this.message = message;
    }
}
