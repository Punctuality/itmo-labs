public enum Conceptions implements FairyTaleObject{
/*
    1\. Именительный - КТО? ЧТО?

    2\. Родительный - КОГО? ЧЕГО?

    3\. Дательный - КОМУ? ЧЕМУ?

    4\. Винительный - КОГО? ЧТО?

    5\. Творительный - КЕМ? ЧЕМ?

    6\. Предложный - О КОМ? О ЧЕМ?
     */

    NIGHT("ночь ночи ночи ночь ночью ночи".split(" ")),
    AIR("воздух воздуха воздуху воздух воздухом воздухе".split(" ")),
    STUFFY("душный душного душному душный душнным душном".split(" ")),
    TIME("время времени времени время временем времени".split(" ")),
    ADVISE("совет совета совету совет советом совете".split(" ")),
    TRUTH("правда правды правде правду правдой правде".split(" "));

    public final int nForms = 6;

    private String[] lines;

    Conceptions(String[] lines){
        this.lines = lines;
    }

    public String getName(int index){
        try {
            return this.lines[index];
        } catch (IndexOutOfBoundsException exp){
            return exp.toString();
        }
    }
}
