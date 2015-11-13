package sorry;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;
public class Card {
    static final int totalCards=45;
    static ArrayList<Card> Cards = new ArrayList<Card>();
    //position for yes option
    static int option1[]={0,0};
    //position for no option
    static int option2[]={0,0};
    static enum specFunc{none, drawAgain, split, backwards, choice, swap, sorry, splitted};
    private specFunc cardFunc;
    private int type;
    private Piece p;
    private boolean selectPiece;
    Image cardImage;
    private int spaceLeft;
    
    Card(int _type)
    {
        type=_type;
        resetFunc();
        selectPiece=false;
        cardImage=Toolkit.getDefaultToolkit().getImage("./Card.png");
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
//Test specific cards with this code
        Card card=null;
        while(true)
        {
        card=Cards.get(0);
        Cards.remove(0);
        if(card.type==2 || card.type==4 || card.type==5)
            break;
        }
        
//        Card card=Cards.get(0);
//        Cards.remove(0);
            
        return(card);
    }

    public boolean getSelectPiece() {
        return selectPiece;
    }

    public void setSelectPiece(boolean selectPiece) {
        this.selectPiece = selectPiece;
    }
    
    public int getType()
    {
        return(type);
    }
    public static boolean isEmpty()
    {
        return(Cards.isEmpty());
    }
    private void resetFunc()
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
    public void setFunc(specFunc i){
        cardFunc=i;
    }

    public Piece getP() {
        return p;
    }

    public void setP(Piece p) {
        this.p = p;
    }
    public int getSpaceLeft() {
        return spaceLeft;
    }

    public void setSpaceLeft(int spaceLeft) {
        this.spaceLeft = spaceLeft;
    }
    
    public void draw(Graphics2D g,int x, int y, int width, int height) {
        option1[0]=x+5;
        option1[1]=y+height-7;
        option2[0]=x+width-35;
        option2[1]=y+height-7;
        g.setColor(Color.WHITE);
//        g.fillRect(x, y, width, height);
        g.drawImage(cardImage,x, y, width, height,Sorry.frame1);
        g.setColor(Color.BLACK);
//        g.drawRect(x, y, width, height);
        g.setFont(new Font("Arial",Font.BOLD,30));
        if(type<13)
            g.drawString(""+type, x+width/2-8, y+height/2+2);
        else
            g.drawString("Sorry", x+width/2-28, y+height/2+2);
        //When cardFunc is split and when a piece is selected you see the options
        if(cardFunc==specFunc.split && p!=null && !p.isInStart() && spaceLeft==0 && !selectPiece)
        {
            g.drawString("Split?", (x+width/4)+6, y+height-7);
            g.setFont(Sorry.CardFont);
            String yes= "Yes";
            String no="No";
            if(BoundingBox.isMouseIn(yes, option1[0], option1[1], Sorry.CardFont,g))
                    g.setColor(Color.red);
            
            g.drawString("Yes", option1[0], option1[1]);
            
            if(BoundingBox.isMouseIn(no, option2[0], option2[1], Sorry.CardFont,g))
                g.setColor(Color.red);
            else
                g.setColor(Color.BLACK);
            
            g.drawString("No", option2[0], option2[1]);
        }
        //When cardFunc is swap and when a piece is selected you see the options
        else if(!selectPiece && cardFunc==specFunc.swap && p!=null && !p.isInStart())
        {
            g.drawString("Swap?", (x+width/4), y+height-7);
            g.setFont(Sorry.CardFont);
            String yes= "Yes";
            String no="No";
            if(BoundingBox.isMouseIn(yes, option1[0], option1[1], Sorry.CardFont,g))
                    g.setColor(Color.red);
            
            g.drawString("Yes", option1[0], option1[1]);
            
            if(BoundingBox.isMouseIn(no, option2[0], option2[1], Sorry.CardFont,g))
                g.setColor(Color.red);
            else
                g.setColor(Color.BLACK);
            
            g.drawString("No", option2[0], option2[1]);
        }
        else if(cardFunc==specFunc.choice && p!=null && !p.isInStart())
        {
            g.setFont(new Font("Arial",Font.BOLD,25));
            g.drawString("1 Back?", (x+width/4), y+height-7);
            g.setFont(Sorry.CardFont);
            String yes= "Yes";
            String no="No";
            if(BoundingBox.isMouseIn(yes, option1[0], option1[1], Sorry.CardFont,g))
                    g.setColor(Color.red);
            
            g.drawString("Yes", option1[0], option1[1]);
            
            if(BoundingBox.isMouseIn(no, option2[0], option2[1], Sorry.CardFont,g))
                g.setColor(Color.red);
            else
                g.setColor(Color.BLACK);
            
            g.drawString("No", option2[0], option2[1]);
        }
   }
    
}