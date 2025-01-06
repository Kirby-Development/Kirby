package dev.kirby.hwid;

public enum Salt {
    LUCKY,
    Kirby,
    Gabibbo,
    SimoNegro,
    SborrEcho,
    CheccoGlione,
    ThomasTurbato,
    ;

    public static String get(int i) {
        Salt[] values = Salt.values();
        return values[Math.abs(i % values.length)].name();
    }

}
