package cn.airanthem.xmu.query.enums;

public enum OrderTypeEnum {
    DESC("DESC"), ASC("ASC");

    private final String value;

    public String getValue() {
        return value;
    }

    OrderTypeEnum(String value) {
        this.value = value;
    }
}
