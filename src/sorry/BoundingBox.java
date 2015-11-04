package sorry;
import java.awt.*;
public class BoundingBox {
    //tests if the mouse is hovering over a string using FontMetrics
    static public boolean isMouseIn(String strg,int xleft,int ybot,Font font, Graphics g)
    {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int ytop=ybot-metrics.getHeight();
        int xright=xleft+metrics.stringWidth(strg);
        if(Sorry.mouseX>xleft && Sorry.mouseX<xright && Sorry.mouseY<ybot && Sorry.mouseY>ytop)
            return(true);
        
        return(false);
    }
}
