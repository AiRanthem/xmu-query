package cn.airanthem.xmu.query.enums;

public enum BiLogic {
    AND("And"), OR("Or"), NOT("Not");

    private final String value;

    BiLogic(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
