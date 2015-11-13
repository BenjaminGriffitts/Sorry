package sorry;
import Tile.*;
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
    private int howFar;
    private Image image;
    
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
//            g.fillOval(x+Column*width, y+Row*height, width, height);
            g.drawImage(image,x+Column*width,y+Row*height,width,height,Sorry.frame1);
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
                image=Toolkit.getDefaultToolkit().getImage("./Blue_Piece.png");
            }
        else if(team==Sorry.Owner.Player2)
            {
                c=Color.YELLOW;
                image=Toolkit.getDefaultToolkit().getImage("./Yellow_Piece.png");
            }
        else if(team==Sorry.Owner.Player3)
            {
                c=Color.GREEN;
                image=Toolkit.getDefaultToolkit().getImage("./Green_Piece.png");
            }
        else
            {
                c=Color.RED;
                image=Toolkit.getDefaultToolkit().getImage("./Red_Piece.png");
            }
        
//        i=Toolkit.getDefaultToolkit().getImage("./Blue_Piece.png");
    }
    static public void resetColors()
    {
        //resets all colors back to there original
        for(Piece i:Pieces)
            if(i.team==Sorry.Owner.Player1)
            {
                i.c=Color.BLUE;
                i.image=Toolkit.getDefaultToolkit().getImage("./Blue_Piece.png");
            }
        else if(i.team==Sorry.Owner.Player2)
            {
                i.c=Color.YELLOW;
                i.image=Toolkit.getDefaultToolkit().getImage("./Yellow_Piece.png");
            }
        else if(i.team==Sorry.Owner.Player3)
            {
                i.c=Color.GREEN;
                i.image=Toolkit.getDefaultToolkit().getImage("./Green_Piece.png");
            }
        else
            {
                i.c=Color.RED;
                i.image=Toolkit.getDefaultToolkit().getImage("./Red_Piece.png");
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

    public int getColumn() {
        return Column;
    }

    public int getRow() {
        return Row;
    }

    public void setColumn(int Column) {
        this.Column = Column;
    }

    public void setRow(int Row) {
        this.Row = Row;
    }

    public int getHowFar() {
        return howFar;
    }

    
    
    
    public void setC(Color c) {
        if(c==Color.MAGENTA)
        {
            image=Toolkit.getDefaultToolkit().getImage("./MAGENTA_Piece.png");
        }
        else if(c==Color.BLUE)
        {
            image=Toolkit.getDefaultToolkit().getImage("./Blue_Piece.png");
        }
        else if(c==Color.YELLOW)
        {
            image=Toolkit.getDefaultToolkit().getImage("./Yellow_Piece.png");
        }
        else if(c==Color.GREEN)
        {
            image=Toolkit.getDefaultToolkit().getImage("./Green_Piece.png");
        }
        else
        {
            image=Toolkit.getDefaultToolkit().getImage("./Red_Piece.png");
        }
        this.c=c;
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
                    {
                        Row=0;
                        Column = 4;
                    }
                    else if(team==Sorry.Owner.Player3)
                    {
                        Column=nCol-1;
                        Row = 4;
                    }
                    else if(team==Sorry.Owner.Player4)
                    {
                        Row=nRows-1;
                        Column = nCol-5;
                    }
                    else
                    {
                        Column=0;
                        Row = nRows-5;
                    }
                    
                    if(c.getType()==2)
                        moveDir(nRows,nCol,1);
                }
                else
                {
                    if(Tile.getTile(Row, Column) instanceof TileHome && ((TileHome)Tile.getTile(Row, Column)).getDistanceFromEnd()>=c.getType())
                        moveDir(nRows,nCol,c.getType());
                    else if(howFar>=c.getType())
                        moveDir(nRows,nCol,c.getType());
                }
            }
            else
            {
                if(howFar>=c.getType())
                    moveDir(nRows,nCol,c.getType());
            }
        }
        else if(c.getCardFunc()==Card.specFunc.backwards)
        {
            if(c.getType()==10)
                moveDir(nRows,nCol,-1);
            else
                moveDir(nRows,nCol,-c.getType());
        }
        
        //Checks if you land on another Piece and that other piece is not your own, resets piece back to start
        Piece p = isPieceThere(Row,Column,this);
        if(p!=null && p.team!=team)
        {
            p.Column=p.startColumn;
            p.Row=p.startRow;
            p.howFarTillHome();
        }
        
        if(Tile.getTile(Row,Column) instanceof TileSliders && howFar>6 && c.getType()!=4)
        {
            if(Row==6 || Column==6 || Row==9 || Column==9)
                moveDir(nRows,nCol,4);
            else
                moveDir(nRows,nCol,3);
        }
        
        howFarTillHome();
            
    }
    public void move(int nRows, int nCol, int distance)
    {
        moveDir(nRows, nCol, distance);
    }
    public void moveDir(int nRows, int nCol,int Distance)
    {
        if(Distance>0)
        {
            for(int i=0; i<Distance; i++)
            {
                if(Tile.getTile(Row, Column) instanceof TileHome && team==((TileHome)Tile.getTile(Row, Column)).getTeam())
                {
                    if(team==Sorry.Owner.Player1)
                    {
                        Column+=1;
                    }
                    else if(team==Sorry.Owner.Player2)
                    {
                        Row+=1;
                    }
                    else if(team==Sorry.Owner.Player3)
                    {
                        Column-=1;
                    }
                    else if(team==Sorry.Owner.Player4)
                    {
                        Row-=1;
                    }
                }
                else
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
        else if(Distance<0)
        {
            for(int i=0; i>Distance; i--)
            {
                if(Tile.getTile(Row, Column) instanceof TileHome && team==((TileHome)Tile.getTile(Row, Column)).getTeam() && ((TileHome)Tile.getTile(Row, Column)).getDistanceFromEnd()<6)
                {

                    if(team==Sorry.Owner.Player1)
                    {
                        Column-=1;
                    }
                    else if(team==Sorry.Owner.Player2)
                    {
                        Row-=1;
                    }
                    else if(team==Sorry.Owner.Player3)
                    {
                        Column+=1;
                    }
                    else if(team==Sorry.Owner.Player4)
                    {
                        Row+=1;
                    }

                }
                else
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
    public static void swapPieces(Piece piece1,Piece piece2)
    {
        int pieceRow = piece1.Row;
        int pieceCol = piece1.Column;
        piece1.Row = piece2.Row;
        piece1.Column = piece2.Column;
        piece2.Row = pieceRow;
        piece2.Column = pieceCol;
        piece1.howFarTillHome();
        piece2.howFarTillHome();
    }
    public static void Sorry(Piece piece1,Piece piece2)
    {
        piece1.Row = piece2.Row;
        piece1.Column = piece2.Column;
        piece2.Column=piece2.startColumn;
        piece2.Row=piece2.startRow;
        piece1.howFarTillHome();
        piece2.howFarTillHome();
    }
    public boolean isInStart()
    {
        if(Row == startRow && Column == startColumn)
            return(true);
        else 
            return(false);
    }
    
    public boolean checkCanMove(Sorry.Owner currentTeam)
    {
        for(Piece temp: Pieces)
        {
            if(temp!=null && temp.team==currentTeam && !temp.isInStart() && !temp.safety(temp))
            {
                return(true);
            }
        }
                
        return(false);
    }
    //Returns true if piece is safe
    static public boolean safety(Piece p)
    {
        if(Tile.getTile(p.Row, p.Column) instanceof TileHome && ((TileHome)Tile.getTile(p.Row, p.Column)).getDistanceFromEnd()<6 && 
                ((TileHome)Tile.getTile(p.Row, p.Column)).getTeam()==p.getTeam())
            return(true);
        
        return(false);
    }
    public int howFarTillHome()
    {
        if(Tile.getTile(Row,Column) instanceof TileHome)
        {
            howFar=((TileHome)Tile.getTile(Row,Column)).getDistanceFromEnd();
            return howFar;
        }
        else if(!isInStart())
        {
            int row=Row;
            int col=Column;
            int hF=0;
            int nCol=Tile.getNumColumns();
            int nRows=Tile.getNumRows();
            for(int i=0; i<60; i++)
            {
                if(row==0 && col<nCol-1)
                {
                    col+=1;
                }
                else if(col==nCol-1 && row<nRows-1)
                {
                    row+=1;
                }
                else if(col>0 && row==nRows-1)
                {
                    col-=1;
                }
                else if(col==0 && row>0)
                {
                    row-=1;
                }
                
                
                hF+=1;
                if(Tile.getTile(row,col) instanceof TileHome && ((TileHome)Tile.getTile(row,col)).getTeam()==team)
                    break;
            }
            howFar=hF+6;
            return hF;
        }
        else
        {
            howFar=66;
            return 66;
        }
    }
    static public boolean checkSorry(Sorry.Owner currentTeam)
    {
        for(Piece temp: Pieces)
        {
            if(temp!=null && temp.team!=currentTeam && !temp.isInStart() && !temp.safety(temp))
            {
                return(true);
            }
        }
                
        return(false);
    }
    static public boolean checkSorry(Piece p)
    {
        for(Piece temp: Pieces)
        {
            if(temp!=null && temp.team==p.team && temp.isInStart())
                return(true);
        }
        return(false);
    }
    public boolean checkCanMove(Sorry.Owner currentTeam, Piece p)
    {
        for(Piece temp: Pieces)
        {
            if(!p.isInStart())
            {
                
            }
            else if(temp!=null && temp.team==currentTeam && !temp.isInStart() && temp!=p)
            {
                return(true);
            }
        }
                
        return(false);
    }
    static public void clearPieces()
    {
        Pieces.clear();
    }
}
