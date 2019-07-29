package top.toptimus.common.enums;

/**
 * 判定条件
 */
public enum OperatorEnum {

    /**
     * 相等(EQ)、不等(NE)、小于(LT)、大于(GT)、小于或等于(LE)、大于或等于(GE)
     */
    EQ("=", 1), NE("!=", 2), LT("<", 3), GT(">", 4), LE("<=", 5), GE("<=", 6), IN("IN", 7), NOTIN("NOTIN", 8);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private OperatorEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (OperatorEnum c : OperatorEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}