public enum Prepositions {
    ON("на"),
    IN("в"),
    UNDER("под"),
    NOT("не"),
    WHAT("что"),
    TO("до"),
    ALMOST("чуть"),
    BUT("но"),
    THAT("это"),
    SOME("некоторое"),
    BUTN("а"),
    THEN("потом"),
    FROM("c"),
    THOSE("этих"),
    SUCH("такой"),
    THATS("поэтому"),
    WHEN("когда"),
    BUTF("однако"),
    ENTIRE("всю"),
    AND("и");
//    ITCH();




    private String preposition;

    Prepositions(String preposition) {
        this.preposition = preposition;
    }

    public String getLine() {
        return this.preposition;
    }
}
