package Tile;
import sorry.*;
public class TileHome extends Tile{
    private Sorry.Owner team;
    private int distanceFromEnd;
    TileHome(Sorry.Owner _team, int d)
    {
        super(2);
        team=_team;
        distanceFromEnd=d;
    }

    public Sorry.Owner getTeam() {
        return team;
    }

    public int getDistanceFromEnd() {
        return distanceFromEnd;
    }
    
    
}
