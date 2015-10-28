package sorry;
import java.util.ArrayList;
import java.util.Collections;
public class Card {
    static final int totalCards=45;
    static ArrayList<Card> Cards = new ArrayList<Card>();
    private int type;
    Card(int _type)
    {
        type=_type;
    }
    public static void resetDeck()
    {
        for(int i=0;i<5;i++)
        {
            Cards.add(new Card(0));
        }
        for(int i=1;i<11;i++)
        {
            for(int i1=0;i<4;i++)
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
}
