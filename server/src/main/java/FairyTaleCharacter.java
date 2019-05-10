public abstract class FairyTaleCharacter implements FairyTaleObject{
    public String doAction(String action){
        return PersonActions.valueOf(action).getLine();
    }

    public abstract String getBodyPart(String nameOfPart, int index);

}
