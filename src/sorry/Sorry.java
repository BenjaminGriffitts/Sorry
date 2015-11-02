package sorry;

import Tile.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class Sorry extends JFrame implements Runnable {
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + 526;
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + 526;
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;
    Image sorryBoard;

    final int numRows = 15;
    final int numColumns = 15;
    
    enum Owner{Player1, Player2, Player3, Player4};
    boolean MoveFinished; //if true next click is drawing a card
    Card currentCardType;
    Owner currentPlayer;
    
    Tile board[][];
    int SlidersRow[]={0,0,1           ,9           ,numRows-1   ,numRows-1,5,numRows-2};
    int SlidersCol[]={1,9,numColumns-1,numColumns-1,numColumns-2,5        ,0,0};
    static Menu gui=new Menu();
    static boolean GameStart=false;

    static Sorry frame1;
    public static void main(String[] args) {
        frame1 = new Sorry();
        frame1.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
    }

    public Sorry() {

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    int xpos= e.getX()-getX(0);
                    int ypos= e.getY()-getY(0);
                    
                    int row=ypos/(getHeight2()/numRows);
                    int column=xpos/(getWidth2()/numColumns);
                    
                    if(MoveFinished)
                    {
                        currentCardType=Card.TakeCard();
                        System.out.println(currentCardType.getType());
                        MoveFinished=!MoveFinished;
                    }
                    else if(!MoveFinished)
                    {
                        Piece p=Piece.isPieceThere(row,column);
                        if(p!=null && p.getTeam()==currentPlayer)
                        {
                            
                            p.move(currentCardType,numRows,numColumns);
                            if(currentCardType.getCardFunc()!=Card.specFunc.drawAgain)
                                changeTeam();
                            
                            MoveFinished=!MoveFinished;
                        }

                    }
                    if(Card.isEmpty())
                        Card.resetDeck();
                    
                    
                    
                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_RIGHT == e.getKeyCode())
                {
                   
                }
                if (e.VK_LEFT == e.getKeyCode())
                {
                    
                }
                if (e.VK_UP == e.getKeyCode())
                {
                    
                }
                if (e.VK_DOWN == e.getKeyCode())
                {
                    
                }
                if (e.VK_D == e.getKeyCode())
                {
                    
                }
                if (e.VK_A == e.getKeyCode())
                {
                    
                }
                if (e.VK_W == e.getKeyCode())
                {
                   
                }
                if (e.VK_S == e.getKeyCode())
                {
                   
                }
                if (e.VK_E == e.getKeyCode())
                {
                    reset();
                }

                repaint();
            }
        });
        init();
        start();
    }




    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

//fill background
        g.setColor(Color.cyan);

        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
        
        g.drawImage(sorryBoard,getX(0),getY(0),getWidth2(),getHeight2(),this);

        g.setColor(Color.black);
        
        
        Piece.draw(g,getX(0),getY(0),getHeight2()/numRows,getWidth2()/numColumns);
        
        g.setColor(Color.BLACK);
//        //horizontal lines
//        for (int zi=1;zi<numRows;zi++)
//        {
//            g.drawLine(getX(0) ,getY(0)+zi*getHeight2()/numRows ,
//            getX(getWidth2()) ,getY(0)+zi*getHeight2()/numRows );
//        }
//        //vertical lines
//        for (int zi=1;zi<numColumns;zi++)
//        {
//            g.drawLine(getX(0)+zi*getWidth2()/numColumns ,getY(0) ,
//            getX(0)+zi*getWidth2()/numColumns,getY(getHeight2())  );
//        }
        if(currentCardType!=null)
        {
            currentCardType.draw(g, getX(0)+6*getWidth2()/numColumns, getY(0)+5*getHeight2()/numRows,3*getWidth2()/numColumns,5*getHeight2()/numRows);
        }
        gOld.drawImage(image, 0, 0, null);
    }


////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.04;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
        currentPlayer=Owner.Player1;
        MoveFinished=true;
        currentCardType=null;
        
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
            board = new Tile [numRows][numColumns];
            sorryBoard=Toolkit.getDefaultToolkit().getImage("./Sorry_Board.jpg");
            Card.resetDeck();
            setup();
            setupPiece();
            reset();
        }
                    
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public void setup(){
        for (int zrow = 0;zrow < numRows;zrow++)
        {
            for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
            {
                if(zrow==0 || zrow==numRows-1 || zcolumn==0 || zcolumn==numColumns-1)
                    board[zrow][zcolumn]= new Tile(0);
            }
        }
        for(int i=0;i<SlidersRow.length;i++)
        {
            board[SlidersRow[i]][SlidersCol[i]]=new Tile(1);
        }
        
    }
    public void setupPiece()
    {
        for(int i=0;i<4;i++)
        {
//            for(int i1=0;i1<4;i1++)
//            {
                if(i==0)
                    new Piece(4,1,Owner.Player1);
                if(i==1)
                    new Piece(numColumns-2,4,Owner.Player2);
                if(i==2)
                    new Piece(numColumns-5,numRows-2,Owner.Player3);
                if(i==3)
                    new Piece(1,numRows-5,Owner.Player4);
//            }
        }
    }
    public void changeTeam()
    {
        if(currentPlayer==Owner.Player1)
            currentPlayer=Owner.Player2;
        else if(currentPlayer==Owner.Player2)
            currentPlayer=Owner.Player3;
        else if(currentPlayer==Owner.Player3)
            currentPlayer=Owner.Player4;
        else if(currentPlayer==Owner.Player4)
            currentPlayer=Owner.Player1;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE );
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    public int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }

    public int getHeight2() {
        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
    }
}
