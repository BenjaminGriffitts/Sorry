package sorry;
import java.util.ArrayList;
import java.awt.*;
public class Piece {
    private int Column;
    private int Row;
    private int startColumn;
    private int startRow;
    private Sorry.Owner team;
    static ArrayList<Piece> Pieces =new ArrayList<Piece>();
    private Color c=null;
    
    Piece(int _column,int _row, Sorry.Owner _team)
    {
        Column = _column;
        Row = _row;
        team = _team;
        startColumn = Column;
        startRow = Row;
        setColor();
        Pieces.add(this);
    }
    public void draw(Graphics2D g,int x, int y,int height,int width)
    {
            g.setColor(c);
            g.fillOval(x+Column*width, y+Row*height, width, height);
    }
    static public Piece isPieceThere(int row, int column)
    {
        for(Piece i: Pieces)
        {
            if(i.Row==row && i.Column==column)
                return(i);
                
        }
        return(null);
    }
    static public Piece isPieceThere(int row, int column,Piece p)
    {
        for(Piece i: Pieces)
        {
            if(i.Row==row && i.Column==column && i!=p)
                return(i);
                
        }
        return(null);
    }
    private void setColor()
    {
        if(team==Sorry.Owner.Player1)
            {
                c=Color.BLUE;
            }
        else if(team==Sorry.Owner.Player2)
            {
                c=Color.YELLOW;
            }
        else if(team==Sorry.Owner.Player3)
            {
                c=Color.GREEN;
            }
        else
            {
                c=Color.RED;
            }
    }
    static public void resetColors()
    {
        //resets all colors back to there original
        for(Piece i:Pieces)
            if(i.team==Sorry.Owner.Player1)
                {
                    i.c=Color.BLUE;
                }
            else if(i.team==Sorry.Owner.Player2)
                {
                    i.c=Color.YELLOW;
                }
            else if(i.team==Sorry.Owner.Player3)
                {
                    i.c=Color.GREEN;
                }
            else
                {
                    i.c=Color.RED;
                }
    }
    static public int numPieces()
    {
        return(Pieces.size());
    }
    static public Piece getPiece(int i)
    {
        return(Pieces.get(i));
    }

    public Sorry.Owner getTeam() {
        return team;
    }

    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }
    
    public void move(Card c,int nRows,int nCol)
    {
        if(c.getCardFunc()==Card.specFunc.none || c.getCardFunc()==Card.specFunc.drawAgain)
        {
            if(c.getType()==1 || c.getType()==2)
            {
                if(Row==startRow && Column==startColumn)
                {
                    if(team==Sorry.Owner.Player2)
                        Row-=1;
                    else if(team==Sorry.Owner.Player3)
                        Column+=1;
                    else if(team==Sorry.Owner.Player4)
                        Row+=1;
                    else
                        Column-=1;
                    
                    if(c.getType()==2)
                        moveDir(nRows,nCol,1);
                }
                else
                {
                    moveDir(nRows,nCol,c.getType());
                }
            }
            else
            {
                moveDir(nRows,nCol,c.getType());
            }
        }
        else if(c.getCardFunc()==Card.specFunc.backwards)
        {
            moveDir(nRows,nCol,-c.getType());
        }
        
        //Checks if you land on another Piece and that other piece is not your own, resets piece back to start
        Piece p = isPieceThere(Row,Column,this);
        if(p!=null && p.team!=team)
        {
            p.Column=p.startColumn;
            p.Row=p.startRow;
        }
    }
    public void moveDir(int nRows, int nCol,int Distance)
    {
        if(Distance>0)
        {
            for(int i=0; i<Distance; i++)
            {
                if(Row==0 && Column<nCol-1)
                {
                    Column+=1;
                }
                else if(Column==nCol-1 && Row<nRows-1)
                {
                    Row+=1;
                }
                else if(Column>0 && Row==nRows-1)
                {
                    Column-=1;
                }
                else if(Column==0 && Row>0)
                {
                    Row-=1;
                }
            }
        }
        else if(Distance<0)
        {
            for(int i=0; i>Distance; i--)
            {
                if(Row==0 && Column>0)
                {
                    Column-=1;
                }
                else if(Column==nCol-1 && Row>0)
                {
                    Row-=1;
                }
                else if(Column<nCol-1 && Row==nRows-1)
                {
                    Column+=1;
                }
                else if(Column==0 && Row<nRows-1)
                {
                    Row+=1;
                }
            }
        }
    }

}
