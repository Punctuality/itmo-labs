public enum Places implements FairyTaleObject{
    /*
    1\. Именительный - КТО? ЧТО?

    2\. Родительный - КОГО? ЧЕГО?

    3\. Дательный - КОМУ? ЧЕМУ?

    4\. Винительный - КОГО? ЧТО?

    5\. Творительный - КЕМ? ЧЕМ?

    6\. Предложный - О КОМ? О ЧЕМ?
     */
    WALL("стойка стойки стойке стойку стойкой стойке".split(" ")),
    SHELF("полка полки полке полку полкой полке".split(" ")),
    DOWN("вниз внизу".split(" ")),
    FLOOR("пол пола полу пол полкой полке".split(" "));

    public final int nForms = 6;

    private String[] lines;

    Places(String[] lines){
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
