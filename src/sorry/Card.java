package sorry;
import java.util.ArrayList;
import java.util.Collections;
public class Card {
    static final int totalCards=45;
    static ArrayList<Card> Cards = new ArrayList<Card>();
    static enum specFunc{none, drawAgain, split, backwards, choice, swap, sorry};
    private specFunc cardFunc;
    private int type;
    Card(int _type)
    {
        type=_type;
        setFunc();
    }
    public static void resetDeck()
    {
        for(int i=0;i<5;i++)
        {
            Cards.add(new Card(1));
        }
        for(int i=2;i<12;i++)
        {
            for(int i1=0;i1<4;i1++)
            {
                Cards.add(new Card(i));
            }
        }
        Collections.shuffle(Cards);
    }
    public static Card TakeCard()
    {
        Card card=Cards.get(0);
        Cards.remove(0);
        return(card);
    }
    public int getType()
    {
        return(type);
    }
    public static boolean isEmpty()
    {
        return(Cards.isEmpty());
    }
    private void setFunc()
    {
        if(type==1)
            cardFunc=specFunc.none;
        if(type==2)
            cardFunc=specFunc.drawAgain;
        if(type==3)
            cardFunc=specFunc.none;
        if(type==4)
            cardFunc=specFunc.backwards;
        if(type==5)
            cardFunc=specFunc.none;
        if(type==6) //6 is 7
            cardFunc=specFunc.split;
        if(type==7) //7 is 8
            cardFunc=specFunc.none;
        if(type==8) //8 is 10
            cardFunc=specFunc.choice;
        if(type==9) //9 is 11
            cardFunc=specFunc.swap;
        if(type==10) //10 is 12
            cardFunc=specFunc.none;
        if(type==11) //11 is sorry
            cardFunc=specFunc.sorry;
        
    }
}