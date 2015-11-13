package sorry;

import Menu.CardsInst;
import Menu.Menu;
import Menu.Instructions;
import Menu.HowToWinInst;
import Menu.MoveInst;
import Menu.Pause;
import Menu.*;
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
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + 688;
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + 688;
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;
    Image sorryBoard;
    Image sorryBoardBlue;
    Image sorryBoardYellow;
    Image sorryBoardGreen;
    Image sorryBoardRed;
    
    Image deckOfCards;

    int numRows = Tile.getNumRows();
    int numColumns = Tile.getNumColumns();
    
    public enum Owner{Player1, Player2, Player3, Player4};
    public enum WinState{none,p1,p2,p3,p4};
    WinState winner;
    boolean MoveFinished; //if true next click is drawing a card
    Card currentCardType;
    Owner currentPlayer;
    
    public static boolean pause;
    
    static Font CardFont = new Font("Arial",Font.BOLD,20);
    Piece selectedP=null;
    
    
    int SlidersRow[]={0,0,1           ,9           ,numRows-1   ,numRows-1,6,numRows-2};
    int SlidersCol[]={1,9,numColumns-1,numColumns-1,numColumns-2,6        ,0,0};
    int HomeRow[]={numRows-3,0,2           ,numRows-1};
    int HomeCol[]={0        ,2,numColumns-1,numColumns-3};
    public static Menu gui=new Menu();
    public static Instructions inst=new Instructions();
    public static MoveInst move=new MoveInst();
    public static CardsInst card=new CardsInst();
    public static HowToWinInst win=new HowToWinInst();
    public static Pause paused=new Pause();
    public static boolean GameStart=false;

    static int mouseX=0;
    static int mouseY=0;
    Sound bgsound=null;

    public static Sorry frame1;
    public static void main(String[] args) {
        frame1 = new Sorry();
        frame1.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        inst.setVisible(false);
        inst.setLocationRelativeTo(null);
        move.setVisible(false);
        move.setLocationRelativeTo(null);
        card.setVisible(false);
        card.setLocationRelativeTo(null);
        win.setVisible(false);
        win.setLocationRelativeTo(null);
        paused.setVisible(false);
        paused.setLocationRelativeTo(null);
    }

    public Sorry() {

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    
                    if(pause || winner!=WinState.none)
                        return;
                        
                    int xpos= e.getX()-getX(0);
                    int ypos= e.getY()-getY(0);
                    
                    int row=ypos/(getHeight2()/numRows);
                    int column=xpos/(getWidth2()/numColumns);
                    
                    if(MoveFinished)
                    {
                        currentCardType=Card.TakeCard();
                        MoveFinished=!MoveFinished;
                    }
                    else if(!MoveFinished)
                    {
                        Piece p=Piece.isPieceThere(row,column);
                        if(p!=null && currentCardType!=null && p.getTeam()==currentPlayer)
                        {
                            currentCardType.setP(p);
                            p.setC(Color.MAGENTA);
                        }
                        
                        if(currentCardType.getCardFunc()==Card.specFunc.split && currentCardType.getP()!=null)
                        {
                            if(BoundingBox.isMouseIn("yes", Card.option1[0], Card.option1[1], CardFont, g) && currentCardType.getSpaceLeft()==0 && !currentCardType.getSelectPiece())
                            {
                                //need code
                                
                                TileHighlight.highlightTiles(currentCardType.getP());
                                currentCardType.setSpaceLeft(7);
                            }
                            else if(BoundingBox.isMouseIn("no", Card.option2[0], Card.option2[1], CardFont, g) && currentCardType.getSpaceLeft()==0&& !currentCardType.getSelectPiece())
                            {
                                //sets card to none so that it can use move(), then sets card to null and resets cards color
                                currentCardType.setFunc(Card.specFunc.none);
                                currentCardType.getP().move(currentCardType,numRows,numColumns);
                                finishMove();
                            }
                            else if(currentCardType.getSpaceLeft()==7 && !currentCardType.getSelectPiece())
                            {
                                if(Tile.getTile(row, column) instanceof TileHighlight)
                                {
                                    currentCardType.getP().move(numRows, numColumns, ((TileHighlight)Tile.getTile(row, column)).getDistance());
                                    currentCardType.setSpaceLeft( 7 - ((TileHighlight)Tile.getTile(row, column)).getDistance() );
                                    setup();
                                    currentCardType.setSelectPiece(true);
                                }
                            }
                            else if(currentCardType.getSpaceLeft()<7 && p!=null && currentCardType.getSpaceLeft()!=0 && currentCardType.getSelectPiece())
                            {
                                currentCardType.getP().move(numRows, numColumns, currentCardType.getSpaceLeft());
                                finishMove();
                            }
                            else if(!currentCardType.getP().checkCanMove(currentPlayer))
                            {
                                finishMove();
                            }
                            
                        }
                        else if(currentCardType.getCardFunc()==Card.specFunc.swap && currentCardType.getP()!=null && !Piece.safety(currentCardType.getP()))
                        {
                            if(!currentCardType.getP().checkCanMove(currentPlayer))
                            {
                                finishMove();
                            }
                            else if(BoundingBox.isMouseIn("yes", Card.option1[0], Card.option1[1], CardFont, g))
                            {
                                if(!currentCardType.getP().isInStart())
                                    currentCardType.setSelectPiece(true);
                            }
                            else if(BoundingBox.isMouseIn("no", Card.option2[0], Card.option2[1], CardFont, g))
                            {
                                //sets card to none so that it can use move(), then sets card to null and resets cards color
                                currentCardType.setFunc(Card.specFunc.none);
                                currentCardType.getP().move(currentCardType,numRows,numColumns);
                                finishMove();
                            }
                        }
                        else if(currentCardType.getCardFunc()==Card.specFunc.choice && currentCardType.getP()!=null)
                        {
                            if(!currentCardType.getP().checkCanMove(currentPlayer))
                            {
                                finishMove();
                            }
                            else if(BoundingBox.isMouseIn("yes", Card.option1[0], Card.option1[1], CardFont, g))
                            {
                                //need code
                                currentCardType.setFunc(Card.specFunc.backwards);
                                currentCardType.getP().move(currentCardType,numRows,numColumns);
                                finishMove();
                                
                            }
                            else if(BoundingBox.isMouseIn("no", Card.option2[0], Card.option2[1], CardFont, g))
                            {
                                //sets card to none so that it can use move(), then sets card to null and resets cards color
                                currentCardType.setFunc(Card.specFunc.none);
                                currentCardType.getP().move(currentCardType,numRows,numColumns);
                                finishMove();
                            }
                        }
                        else if(currentCardType.getCardFunc()==Card.specFunc.sorry && currentCardType.getP()!=null)
                        {
                                if(currentCardType.getP().isInStart() && Piece.checkSorry(currentPlayer))
                                    currentCardType.setSelectPiece(true);
                                else if(!Piece.checkSorry(currentCardType.getP()) || !Piece.checkSorry(currentPlayer))
                                    finishMove();
                                else
                                    Piece.resetColors();
                        }
                        else if(currentCardType.getP()!=null && p!=null && p.getTeam()==currentPlayer)
                        {
                            if((currentCardType.getType()==1 || currentCardType.getType()==2) || !currentCardType.getP().checkCanMove(currentPlayer, currentCardType.getP()))
                            {
                                currentCardType.getP().move(currentCardType,numRows,numColumns);
                                if(currentCardType.getCardFunc()!=Card.specFunc.drawAgain)
                                {
                                    changeTeam();
                                    currentCardType=null;
                                }
                                MoveFinished=!MoveFinished;
                                Piece.resetColors();
                            }
                        }

                        
                        if(currentCardType != null && currentCardType.getSelectPiece())
                        {
                            Piece tempPiece = Piece.isPieceThere(row, column);
                            if(currentCardType.getCardFunc()==Card.specFunc.swap)
                            {
                                if(tempPiece != null && !tempPiece.isInStart() && !currentCardType.getP().isInStart() && !Piece.safety(tempPiece) &&
                                        !Piece.safety(currentCardType.getP()))
                                {
                                    Piece.swapPieces(currentCardType.getP(),tempPiece);
                                    finishMove();
                                }
                            }
                            else if(currentCardType.getCardFunc()==Card.specFunc.sorry)
                            {
                                if(tempPiece!=null && !tempPiece.isInStart() && currentCardType.getP().isInStart() && !Piece.safety(tempPiece) &&
                                        !Piece.safety(currentCardType.getP()))
                                {
                                    Piece.Sorry(currentCardType.getP(),tempPiece);
                                    finishMove();
                                }
                            }
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
        mouseX= e.getX();
        mouseY= e.getY();
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
                if (e.VK_ESCAPE == e.getKeyCode())
                {
                    pause=true;
                    paused.setVisible(true);
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
        g.setColor(Color.black);

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
        boardCycle();

        g.drawImage(deckOfCards,getX(0)+(getWidth2()/2)-125,getY(0)+(getHeight2()/2)-30,250,250,this);
        
        g.setColor(Color.black);
        
        for(int i=0;i<Piece.numPieces();i++)
        {
            Piece.getPiece(i).draw(g,getX(0),getY(0),getHeight2()/numRows,getWidth2()/numColumns);
        }
        
        
        g.setColor(Color.BLACK);
//        //horizontal lines
//        for (int zi=1;zi<numRows;zi++)
//        {
//            g.drawLine(getX(0) ,getY(0)+zi*getHeight2()/numRows ,
//            getX(getWidth2()) ,getY(0)+zi*getHeight2()/numRows );
//        }
//        //vertical lines
//        for (int zi=1;zi<numColumns;zi++)
//        {+
//            g.drawLine(getX(0)+zi*getWidth2()/numColumns ,getY(0) ,
//            getX(0)+zi*getWidth2()/numColumns,getY(getHeight2())  );
//        }
        for(int i=0;i<numRows;i++)
            for(int i1=0;i1<numColumns;i1++)
            {
                if(Tile.getTile(i,i1) instanceof TileHighlight)
                {
                    g.setStroke(new BasicStroke(3.0f));
                    g.setColor(Color.MAGENTA);
                    g.drawRect(getX(0)+i1*(getWidth2()/numColumns), getY(0)+i*(getHeight2()/numRows), getWidth2()/numColumns, getHeight2()/numRows);
                }
            }
        if(currentCardType!=null)
        {
            
            currentCardType.draw(g, getX(0)+6*getWidth2()/numColumns, getY(0)+4*getHeight2()/numRows,4*getWidth2()/numColumns,2*getHeight2()/numRows);
            
        }
        g.setColor(Color.BLACK);
        g.drawRect(mouseX, mouseY, 2, 2);
        gOld.drawImage(image, 0, 0, null);
    }
    /////////////////////////////////////////////////////////////////////////
    public void boardCycle(){
    
        boardCycler2();
        boardCycler1();
    }
/////////////////////////////////////////////////////////////////////////
    public void boardCycler1(){
    
        
        if(currentPlayer==Owner.Player1)
        {
            g.drawImage(sorryBoardBlue,getX(0),getY(0),getWidth2(),getHeight2(),this);
        }
        else if(currentPlayer==Owner.Player2)
        {
            g.drawImage(sorryBoardYellow,getX(0),getY(0),getWidth2(),getHeight2(),this);
        }
        else if(currentPlayer==Owner.Player3)
        {
            g.drawImage(sorryBoardGreen,getX(0),getY(0),getWidth2(),getHeight2(),this);
        }
        else
        {
            g.drawImage(sorryBoardRed,getX(0),getY(0),getWidth2(),getHeight2(),this);
        }
        
        
    }
    /////////////////////////////////////////////////////////////////////////
    public void boardCycler2(){
    
        
        if(currentPlayer==Owner.Player1)
        {
            g.drawImage(sorryBoardRed,getX(0),getY(0),getWidth2(),getHeight2(),this);
        }
        else if(currentPlayer==Owner.Player2)
        {
            g.drawImage(sorryBoardBlue,getX(0),getY(0),getWidth2(),getHeight2(),this);
        }
        else if(currentPlayer==Owner.Player3)
        {
            g.drawImage(sorryBoardYellow,getX(0),getY(0),getWidth2(),getHeight2(),this);
        }
        else
        {
            g.drawImage(sorryBoardGreen,getX(0),getY(0),getWidth2(),getHeight2(),this);
        }
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
        setupPiece();
        Piece.resetColors();
        pause=false;
        winner = WinState.none;
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
            Tile.setBoard();
            sorryBoard=Toolkit.getDefaultToolkit().getImage("./Sorry_Board.jpg");
            sorryBoardBlue=Toolkit.getDefaultToolkit().getImage("./Sorry_BoardBlue.jpg");
            sorryBoardYellow=Toolkit.getDefaultToolkit().getImage("./Sorry_BoardYellow.jpg");
            sorryBoardGreen=Toolkit.getDefaultToolkit().getImage("./Sorry_BoardGreen.jpg");
            sorryBoardRed=Toolkit.getDefaultToolkit().getImage("./Sorry_BoardRed.jpg");
            deckOfCards=Toolkit.getDefaultToolkit().getImage("./Deck_of_Cards.png");
            bgsound = new Sound("./Mariachi.wav");
            Card.resetDeck();
            setup();
            setupPiece();
            reset();
        }
        if (bgsound.donePlaying)
        {
            bgsound = new Sound("./Mariachi.wav");
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
                    Tile.setTile(zrow, zcolumn,0);
            }
        }
        for(int i=0;i<SlidersRow.length;i++)
        {
            Tile.setTile(SlidersRow[i], SlidersCol[i], 1);
        }
        for(int i=0;i<HomeRow.length;i++)
        {
            if(i==0)
            {
                Tile.setTile(HomeRow[i], HomeCol[i],Owner.Player1,6);
                for(int i1=1;i1<=6;i1++)
                    Tile.setTile(HomeRow[i], HomeCol[i]+(i1*1), Owner.Player1,6-i1);
            }
            else if(i==1)
            {
                Tile.setTile(HomeRow[i], HomeCol[i],Owner.Player2,6);
                for(int i1=1;i1<=6;i1++)
                    Tile.setTile(HomeRow[i]+(i1*1), HomeCol[i], Owner.Player2,6-i1);
            }
            else if(i==2)
            {
                Tile.setTile(HomeRow[i], HomeCol[i],Owner.Player3,6);
                for(int i1=1;i1<=6;i1++)
                    Tile.setTile(HomeRow[i], HomeCol[i]-(i1*1), Owner.Player3,6-i1);
            }
            else if(i==3)
            {
                Tile.setTile(HomeRow[i], HomeCol[i],Owner.Player4,6);
                for(int i1=1;i1<=6;i1++)
                    Tile.setTile(HomeRow[i]-(i1*1), HomeCol[i], Owner.Player4,6-i1);
            }
        }
        Tile.setTile(0,0,4);
        Tile.setTile(0,numColumns-1,4);
        Tile.setTile(numRows-1,0,4);
        Tile.setTile(numRows-1,numColumns-1,4);
        
        
    }
    public void setupPiece()
    {
        Piece.clearPieces();
        for(int i=0;i<4;i++)
        {
            for(int i1=0;i1<4;i1++)
            {
                if(i==0)
                    new Piece(1,numRows-5,Owner.Player1);
                if(i==1)
                    new Piece(4,1,Owner.Player2);
                if(i==2)
                    new Piece(numColumns-2,4,Owner.Player3);
                if(i==3)
                    new Piece(numColumns-5,numRows-2,Owner.Player4);
            }
        }
    }
    
    public void changeTeam()
    {
        if(currentPlayer==Owner.Player1)
        {
            currentPlayer=Owner.Player2;
            sorryBoard=Toolkit.getDefaultToolkit().getImage("./Sorry_BoardYellow.jpg");
        }
        else if(currentPlayer==Owner.Player2)
        {
            currentPlayer=Owner.Player3;
            sorryBoard=Toolkit.getDefaultToolkit().getImage("./Sorry_BoardGreen.jpg");
        }
        else if(currentPlayer==Owner.Player3)
        {
            currentPlayer=Owner.Player4;
            sorryBoard=Toolkit.getDefaultToolkit().getImage("./Sorry_BoardRed.jpg");
        }
        else if(currentPlayer==Owner.Player4)
        {
            currentPlayer=Owner.Player1;
            sorryBoard=Toolkit.getDefaultToolkit().getImage("./Sorry_BoardBlue.jpg");
        }
    }
    public void finishMove()
    {
        currentCardType=null;
        MoveFinished=!MoveFinished;
        if(Piece.checkWin(currentPlayer))
        {
            whoWon();
        }
        
        changeTeam();
        Piece.resetColors();
    }
    public void whoWon()
    {
        if(currentPlayer==Owner.Player1)
            winner=WinState.p1;
        else if(currentPlayer==Owner.Player2)
            winner=WinState.p2;
        else if(currentPlayer==Owner.Player3)
            winner=WinState.p3;
        else
            winner=WinState.p4;
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
