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
    
    Piece(int _column,int _row, Sorry.Owner _team)
    {
        Column = _column;
        Row = _row;
        team = _team;
        startColumn = Column;
        startRow = Row;
        Pieces.add(this);
    }
    static public void draw(Graphics2D g,int x, int y,int diameter)
    {
        
        for(Piece i: Pieces)
        {
            if(i.team==Sorry.Owner.Player1)
                g.setColor(Color.BLUE);
            else if(i.team==Sorry.Owner.Player2)
                g.setColor(Color.YELLOW);
            else if(i.team==Sorry.Owner.Player3)
                g.setColor(Color.GREEN);
            else
                g.setColor(Color.RED);
            g.fillOval(x+i.Column*diameter, y+i.Row*diameter, diameter, diameter);
        }
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

    public Sorry.Owner getTeam() {
        return team;
    }
    
    public void move(int c,int nRows,int nCol)
    {
        if(c==1)
        {
            if(Row==startRow && Column==startColumn)
            {
                if(team==Sorry.Owner.Player1)
                    Row-=1;
                else if(team==Sorry.Owner.Player2)
                    Column+=1;
                else if(team==Sorry.Owner.Player3)
                    Row+=1;
                else
                    Column-=1;
            }
            else
            {
                moveDir(nRows,nCol,1);
            }
        }
    }
    public void moveDir(int nRows, int nCol,int Distance)
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

}
