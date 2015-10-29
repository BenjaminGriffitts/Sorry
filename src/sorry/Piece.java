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

}
