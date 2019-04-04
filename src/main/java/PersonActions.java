public enum PersonActions {
    FOLLOWED("последовал"),
    STRECHED("растянулся"),
    TAKEOFF("снимая"),
    FELT("почувствовал"),
    ATTACKED("напали"),
    STARTEDP("принялись"),
    STARTED("принялся"),
    ITCHED("чесался"),
    WANTED("хотелось"),
    HELPED("помогало"),
    WAIT("дожидаться"),
    ENDURED("терпел"),
    SLIPPED("соскочил"),
    SATISFY("насытятся"),
    HEWAS("был"),
    DECIDED("решил"),
    SIT("сидеть"),
    THROWING("сбрасывать"),
    BITE("кусать");


    private String verb;

    PersonActions(String verb) {
        this.verb = verb;
    }

    public String getLine() {
        return this.verb;
    }
}
