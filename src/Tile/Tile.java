package Tile;
import sorry.*;
public abstract class Tile {
    static private Tile board[][];
    static final int numRows = 16;
    static final int numColumns = 16;
    private int type;
    public Tile(int _type)
    {
        type=_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static int getNumRows() {
        return numRows;
    }

    public static int getNumColumns() {
        return numColumns;
    }
    public static void setBoard(){
        board = new Tile [numRows][numColumns];
    }
    public static void setTile(int rows,int columns, int type)
    {
        if(type==0)
            board[rows][columns]=new TileNormal();
        else if(type==1)
            board[rows][columns]=new TileSliders();
    }
    public static void setTile(int rows, int columns, int type, int distance)
    {
        board[rows][columns]=new TileHighlight(distance);
    }
    public static void setTile(int rows,int columns, Sorry.Owner team, int d)
    {
        board[rows][columns]=new TileHome(team,d);
    }
    public static Tile getTile(int rows, int columns)
    {
        if(board[rows][columns]!=null)
            return (board[rows][columns]);
        return null;
    }
    
    
}