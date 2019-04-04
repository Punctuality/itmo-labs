public enum BodyParts implements FairyTaleObject{
    CLOTH("одежда одежды одежде одежду одеждой одежде".split(" ")),
    NOSE("нос носа носу нос носом носе".split(" ")),
    BODY("тело тела телу тело телом теле".split(" ")),
    BLOOD("кровь крови крови кровь кровью крови".split(" ")),
    LEG("нога ноги ноге ногу ногой ноге".split(" "));


    public final int nForms = 6;

    private String[] lines;

    BodyParts(String[] lines) {
        this.lines = lines;
    }

    public String getName(int index) {
        try {
            return this.lines[index];
        } catch (IndexOutOfBoundsException exp) {
            return exp.toString();
        }
    }
}