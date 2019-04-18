package indi.key.mipsemulator.storage;

import javafx.scene.paint.Color;

public enum RegisterType {

    /* Universal */
    ZERO,
    AT(Group.RESERVED),
    V0(Group.V),
    V1(Group.V),
    A0(Group.A),
    A1(Group.A),
    A2(Group.A),
    A3(Group.A),
    T0(Group.T),
    T1(Group.T),
    T2(Group.T),
    T3(Group.T),
    T4(Group.T),
    T5(Group.T),
    T6(Group.T),
    T7(Group.T),
    S0(Group.S),
    S1(Group.S),
    S2(Group.S),
    S3(Group.S),
    S4(Group.S),
    S5(Group.S),
    S6(Group.S),
    S7(Group.S),
    T8(Group.T),
    T9(Group.T),
    K0(Group.K),
    K1(Group.K),
    GP(Group.OTHER),
    SP(Group.OTHER),
    FP(Group.OTHER),
    RA(Group.OTHER),
    /* External */
    PC,
    HI,
    LO;

    private enum Group {
        RESERVED(Color.GRAY),
        V(Color.ORANGE),
        A(Color.RED),
        T(Color.PURPLE),
        S(Color.BLUE),
        K(Color.GREEN),
        OTHER(Color.GRAY),
        UNDIFINED(Color.BLACK);

        Color color;

        Group(Color color) {
            this.color = color;
        }
    }

    private Group group;

    RegisterType() {
        this(Group.UNDIFINED);
    }

    RegisterType(Group group) {
        this.group = group;
    }

    public Color getColor() {
        return group.color;
    }

    public static RegisterType of(int index) {
        return values()[index];
    }

    @Override
    public String toString() {
        return "$" + name().toLowerCase();
    }
}
