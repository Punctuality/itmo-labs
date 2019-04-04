public enum Pronouns implements FairyTaleObject{
    /*
    1\. Именительный - КТО? ЧТО?

    2\. Родительный - КОГО? ЧЕГО?

    3\. Дательный - КОМУ? ЧЕМУ?

    4\. Винительный - КОГО? ЧТО?

    5\. Творительный - КЕМ? ЧЕМ?

    6\. Предложный - О КОМ? О ЧЕМ?
     */

    HE(new Person("он его ему него им нем")),
    SHE(new Person("она ее ей нее ей ней")),
    IT(new Person("оно его ему него им нем")),
    YOU(new Person("ты тебя тебе тебя тобой тебе")),
    YOUF(new Person("вы вас вам вас вами вас")),
    I(new Person("я меня мне меня мной мне")),
    WE(new Person("мы нас нам нас нами нас")),
    THEY(new Person("они их им их ими них"));


    private Person pronoun;

    Pronouns(Person pronoun) {
        this.pronoun = pronoun;
    }

    public String doAction(String action){
        return PersonActions.valueOf(action).getLine();
    }

    public String getName(int index) {
        return this.pronoun.getName(index);
    }

    public String getBodyPart(String nameOfPart, int index) {
        return BodyParts.valueOf(nameOfPart).getName(index);
    }
}
