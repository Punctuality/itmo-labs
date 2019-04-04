import java.util.Arrays;

public class Person extends FairyTaleCharacter{
    /*
1\. Именительный - КТО? ЧТО?

2\. Родительный - КОГО? ЧЕГО?

3\. Дательный - КОМУ? ЧЕМУ?

4\. Винительный - КОГО? ЧТО?

5\. Творительный - КЕМ? ЧЕМ?

6\. Предложный - О КОМ? О ЧЕМ?
 */
    private final String[] name;

    Person(){
        this.name = "Я Меня Мне Меня Мной Мне".split(" ");
    }

    Person(String name_variations){
        String[] temp = name_variations.split(" ");
        if(temp.length != 6){
            throw new WrongNameException(temp);
        }
        this.name = temp.clone();
    }

    public enum bodyParts {
        CLOTH("одежда одежды одежде одежду одеждой одежде".split(" ")),
        NOSE("нос носа носу нос носом носе".split(" ")),
        BODY("тело тела телу тело телом теле".split(" ")),
        BLOOD("кровь крови крови кровь кровью крови".split(" ")),
        LEG("нога ноги ноге ногу ногой ноге".split(" "));


        public final int nForms = 6;

        private String[] lines;

        bodyParts(String[] lines) {
            this.lines = lines;
        }

        public String getLine(int index) {
            try {
                return this.lines[index];
            } catch (IndexOutOfBoundsException exp) {
                return exp.toString();
            }
        }
    }

    @Override
    public String getName(int index) {
        try {
            return this.name[index];
        } catch (IndexOutOfBoundsException exp) {
            return exp.toString();
        }
    }

    @Override
    public String getBodyPart(String nameOfPart, int index) {
        return BodyParts.valueOf(nameOfPart).getName(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Person.class){
            Person temp = (Person) obj;
            boolean ans = true;
            for(int i = 0; i < 6; i++){
                ans = ans && ((Person) obj).getName(i).equals(this.getName(i));
            }
            return ans;
        }
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.name);
    }

    @Override
    public int hashCode() {
        return 90441;
    }
}

class WrongNameException extends RuntimeException{
    WrongNameException(String[] names){
        initCause(
        new IllegalArgumentException("Wrong amount of names, should be 6\n"
        +"Got: "+Arrays.toString(names)+" length: "+names.length));
    }
}