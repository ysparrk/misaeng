package dgp.misaeng.global.util.enums;

public enum MicrobeColor {
    RED("빨강", 255, 0, 0),
    ORANGE("주황", 255, 147, 27),
    YELLOW("노랑", 240, 240, 21),
    GREEN("초록", 11, 210, 71),
    BLUE("파랑", 0, 122, 255),
    PURPLE("보라", 80, 0, 191),
    MAGENTA("자홍", 164, 0, 197),
    PINK("분홍", 255, 1, 230),
    BROWN("갈색", 100, 59, 12),
    BLACK("검정", 13, 4, 4),
    WHITE("흰색", 255, 255, 255),
    LIME("라임", 164, 249, 46),
    CYAN("청록", 36, 226, 239),
    DARK_BLUE("진파랑", 61, 115, 214);

    private final String name;
    private final int red;
    private final int green;
    private final int blue;

    MicrobeColor(String name, int red, int green, int blue) {
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String getName() {
        return name;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public String toString() {
        return String.format("%s (RGB: %d, %d, %d)", name, red, green, blue);
    }
}
