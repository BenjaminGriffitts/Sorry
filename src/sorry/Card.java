package sorry;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;
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
                if(i>=8)
                    Cards.add(new Card(i+2));
                else if(i>=6)
                    Cards.add(new Card(i+1));
                else
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
        else if(type==2)
            cardFunc=specFunc.drawAgain;
        else if(type==3)
            cardFunc=specFunc.none;
        else if(type==4)
            cardFunc=specFunc.backwards;
        else if(type==5)
            cardFunc=specFunc.none;
        else if(type==7) 
            cardFunc=specFunc.split;
        else if(type==8) 
            cardFunc=specFunc.none;
        else if(type==10) 
            cardFunc=specFunc.choice;
        else if(type==11) 
            cardFunc=specFunc.swap;
        else if(type==12) 
            cardFunc=specFunc.none;
        else if(type==13) 
            cardFunc=specFunc.sorry;
    }

    public specFunc getCardFunc() {
        return cardFunc;
    }
    public void draw(Graphics2D g,int x, int y, int width, int height) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.setFont(new Font("Arial",Font.BOLD,30));
        g.drawString(""+type, x+width/2-8, y+height/2+2);
        if(cardFunc==specFunc.split)
        {
            
            g.drawString("Split?", (x+width/4)+6, y+height-7);
            g.setFont(new Font("Monospaced",Font.BOLD,20));
            g.drawString("Yes", x+1, y+height-7);
            g.drawString("No", x+width-30, y+height-7);
        }
    }
}