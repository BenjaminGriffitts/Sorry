package Tile;
public class TileHighlight extends Tile{
    private int distance;
    TileHighlight(int d)
    {
        super(3);
        distance=d;
        
    }
    static public void highlightTiles(sorry.Piece p)
    {
        int Column=p.getColumn();
        int Row=p.getRow();
        sorry.Sorry.Owner team=p.getTeam();
        int distance=1;
        for(int i = 1; i<7;i++)
        {
            if(Tile.getTile(Row, Column) instanceof TileHome && team==((TileHome)Tile.getTile(Row, Column)).getTeam())
            {
                if(team==sorry.Sorry.Owner.Player1)
                {
                    Column+=1;
                }
                else if(team==sorry.Sorry.Owner.Player2)
                {
                    Row+=1;
                }
                else if(team==sorry.Sorry.Owner.Player3)
                {
                    Column-=1;
                }
                else if(team==sorry.Sorry.Owner.Player4)
                {
                    Row-=1;
                }
            }
            else
            {
                if(Row==0 && Column<Tile.getNumColumns()-1)
                {
                    Column+=1;
                }
                else if(Column==Tile.getNumColumns()-1 && Row<Tile.getNumRows()-1)
                {
                    Row+=1;
                }
                else if(Column>0 && Row==Tile.getNumRows()-1)
                {
                    Column-=1;
                }
                else if(Column==0 && Row>0)
                {
                    Row-=1;
                }
            }
            
            setTile(Row, Column, 3,distance);
            distance++;
        }
    }

    public int getDistance() {
        return distance;
    }
    
}
