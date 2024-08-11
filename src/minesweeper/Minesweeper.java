
package minesweeper;



import javafx.scene.input.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.EventListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Minesweeper extends Application implements EventListener
{

    public static void main(String[] args) 
    {
        launch(args);
    }
    
    BorderPane pane = new BorderPane();;
    Scene board;
    
    
    int columns = 16;
    int rows = 16;
    int totalCells = columns * rows;
    int cellCount = 0;
    
    int mines = 0;
    int totalMines = 70;
    int cellX = 50;
    int cellY = 85;
    
    int deathRow;
    int deathColumn;
    
    double mouseX;
    double mouseY;
    
    boolean opening = true;
    boolean dead;
    boolean won;
    
    Button open;
    Button flag;
    
    Random rand = new Random();
    
    HBox controls = new HBox();
    
    Cell[][] minefield = new Cell[rows][columns];
    ImageView cell;
    ImageView smiley;
    
    
    
    

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException 
    {
        // <editor-fold defaultstate="collapsed" desc="Making the Board">
        
        Rectangle rect1 = new Rectangle(40, 15, 10, 70 + (rows * 25)); //Borders
        Rectangle rect2 = new Rectangle(40, 15, 20 + (columns * 25), 10);
        Rectangle rect3 = new Rectangle(40, 80 + (rows * 25), 20 + (columns * 25), 10);
        Rectangle rect4 = new Rectangle(50 + (columns * 25), 15, 10, 70 + (rows * 25));
        Rectangle rect5 = new Rectangle(40, 75, 10 + (columns * 25), 10);
        
        Rectangle rect6 = new Rectangle(125, 25, (columns * 25) - 220, 50); //Light background
        Rectangle rect7 = new Rectangle(275, 25, (columns * 25) - 220, 50);
        
        rect1.setFill(Color.GRAY);
        rect2.setFill(Color.GRAY);
        rect3.setFill(Color.GRAY);
        rect4.setFill(Color.GRAY);
        rect5.setFill(Color.GRAY);
        rect6.setFill(Color.LIGHTGRAY);
        rect7.setFill(Color.LIGHTGRAY);

        smiley = new ImageView(new Image(new FileInputStream("src/images/facesmile.gif")));
        smiley.setFitWidth(50);
        smiley.setPreserveRatio(true);
        
        open = new Button("Open");
        open.setPrefSize(75, 50);
        open.setOnAction(e -> {open();});
        
        flag = new Button("Flag");
        flag.setPrefSize(75, 50);
        flag.setOnAction(e -> {flag();});
 
        controls.setPadding(new Insets(25, 0, 0, 50));
        
        controls.setSpacing(100);
        controls.getChildren().addAll(open, smiley, flag);
        pane.getChildren().addAll(rect6, rect7);
        pane.setCenter(controls);
        pane.getChildren().addAll(rect1, rect2, rect3, rect4, rect5);
        
        board = new Scene(pane, 100 + (columns * 25), 125 + (rows * 25));
        
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(board);
        primaryStage.show(); 
        // </editor-fold>
        
        board.setOnMouseClicked(mouseHandler);
        startGame();

    }
    
    public void startGame() throws FileNotFoundException
    {
        // <editor-fold defaultstate="collapsed" desc="Creating Cells">
        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < columns; c++)
            { 
                minefield[r][c] = new Cell(c, r, cellX, cellY);
                cellX += 25;
            }
            cellX = 50;
            cellY += 25;
        }   

        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < columns; c++)
            { 
                cell = new ImageView(minefield[r][c].getImage());
                cell.setFitWidth(25);
                cell.setX(minefield[r][c].getX());
                cell.setY(minefield[r][c].getY());
                cell.setPreserveRatio(true);         
                pane.getChildren().add(cell);
            }
        }
          // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Mine Generation">
        while (mines != totalMines)
        {
            int mineRow = rand.nextInt(rows) + 0;
            int mineColumn = rand.nextInt(columns) + 0;
            
            for (int r = 0; r < rows; r++)
            {
                for (int c = 0; c < columns; c++) 
                {
                    if (mineRow == minefield[r][c].getRow() && mineColumn == minefield[r][c].getColumn() && !(minefield[r][c].isMine()))
                    {
                        minefield[r][c].addMine(true);
                        
                        //Show Mines
                        //cell = new ImageView(minefield[r][c].getImage()); 
                        
                        cell = new ImageView(new Image(new FileInputStream("src/images/blank.gif")));
                        cell.setFitWidth(25);
                        cell.setX(minefield[r][c].getX());
                        cell.setY(minefield[r][c].getY());
                        cell.setPreserveRatio(true);         
                        pane.getChildren().add(cell);
                        mines++; 
                    }
                }
            }
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Mine Detection">
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < columns; c++) 
            {
                if (!(minefield[r][c].isMine())) //Selected, Empty Cell
                {
                    if (r > 0) //If the Upper 3 Cells are mines...
                    {
                        if (minefield[r - 1][c].isMine()) // If N is a mine
                        {
                            minefield[r][c].setMineCount(1);
                        }
                    
                        if (c > 0)
                        {
                            if (minefield[r - 1][c - 1].isMine()) //If NW is a mine
                            {
                                minefield[r][c].setMineCount(1);
                            }
                        }   
                    
                        if (c < columns - 1)
                        {
                            if (minefield[r - 1][c + 1].isMine()) //If NE is a mine
                            {
                                minefield[r][c].setMineCount(1);
                            }
                        }
                    }
                    
                    if (r < rows - 1) //If the Bottom 3 Cells are mines...
                    {
                        if (minefield[r + 1][c].isMine()) //If S is a mine
                        {
                            minefield[r][c].setMineCount(1);
                        }
                        
                        if (c > 0)
                        {
                            if (minefield[r + 1][c - 1].isMine()) //If SW is a mine
                            {
                                minefield[r][c].setMineCount(1);
                            }
                        }   
                    
                        if (c < columns - 1)
                        {
                            if (minefield[r + 1][c + 1].isMine()) //If SE is a mine
                            {
                                minefield[r][c].setMineCount(1);
                            }
                        }
                    }
                    
                    if (c > 0) //If W is a mine...
                    {
                        if (minefield[r][c - 1].isMine()) //If SE is a mine
                        {
                            minefield[r][c].setMineCount(1);
                        }
                    }
                    
                    if (c < columns - 1) //If E is a mine...
                    {
                        if (minefield[r][c + 1].isMine()) //If SE is a mine
                        {
                            minefield[r][c].setMineCount(1);
                        }
                    }
                }
            }
        }
        // </editor-fold>   
        
        cellX = 50;
        cellY = 85;
        mines = 0;
        
        System.out.println("Complete!");
    }
    
    
    public void open()
    {
        if (open.getText().equals("Open"))
        {
            opening = true;
        }
        
        else 
        {
            open.setText("Open");
        }
    }
    
    
    public void flag()
    {
        if (flag.getText().equals("Flag"))
        {
            opening = false;
        }
        
        else 
        {
            flag.setText("Flag");
        }
    }
    
    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
 
        @Override
        public void handle(MouseEvent e) 
        {
            mouseX = e.getX();
            mouseY = e.getY();
            
            System.out.println(mouseX);
            System.out.println(mouseY);
            
            if (!(dead) || !(won))
            {
                // <editor-fold defaultstate="collapsed" desc="Opening Cells">

                if (opening)
                {
                    for (int r = 0; r < rows; r++)
                    {
                        for (int c = 0; c < columns; c++) 
                        {
                            if (minefield[r][c].getX() < mouseX && minefield[r][c].getX() + 25 > mouseX && minefield[r][c].getY() < mouseY && minefield[r][c].getY() + 25 > mouseY && !(minefield[r][c].isFlagged()))
                            {
                                try {openCell(r, c);} catch (FileNotFoundException ex) {}

                                if (minefield[r][c].isMine()) //If you click on a mine...
                                {
                                    won = true;
                                    dead = true;
                                    deathRow = r;
                                    deathColumn = c;
                                }
                                
                            }
                        }
                    }
                }
                // </editor-fold>
                
                // <editor-fold defaultstate="collapsed" desc="Flagging Cells">
                else  
                {
                    for (int r = 0; r < rows; r++)
                    {
                        for (int c = 0; c < columns; c++) 
                        {
                            if (minefield[r][c].getX() < mouseX && minefield[r][c].getX() + 25 > mouseX && minefield[r][c].getY() < mouseY && minefield[r][c].getY() + 25 > mouseY)
                            {
                                if (!(minefield[r][c].isFlagged())) //Adding a flag
                                {
                                    System.out.println("Adding a Flag");
                                    System.out.println("Column: "+minefield[r][c].getColumn());
                                    System.out.println("Row: "+minefield[r][c].getRow());
                                    minefield[r][c].addFlag(true);
                                    try { minefield[r][c].openCell();} catch (FileNotFoundException ex) {}
                                    cell = new ImageView(minefield[r][c].getImage());
                                    cell.setFitWidth(25);
                                    cell.setX(minefield[r][c].getX());
                                    cell.setY(minefield[r][c].getY());
                                    cell.setPreserveRatio(true);         
                                    pane.getChildren().add(cell);
                                }

                                else if ((minefield[r][c].isFlagged())) //Removing a flag
                                {
                                    System.out.println("Column: "+minefield[r][c].getColumn());
                                    System.out.println("Row: "+minefield[r][c].getRow());
                                    minefield[r][c].addFlag(false);
                                    cell = new ImageView(minefield[r][c].getImage());
                                    cell.setFitWidth(25);
                                    cell.setX(minefield[r][c].getX());
                                    cell.setY(minefield[r][c].getY());
                                    cell.setPreserveRatio(true);         
                                    pane.getChildren().add(cell);
                                }
                            }
                        }
                    }
                }
                
                // </editor-fold>
                
                // <editor-fold defaultstate="collapsed" desc="Death">
                if (dead)
                {
                    try {smiley.setImage(new Image(new FileInputStream("src/images/facedead.gif")));} catch (FileNotFoundException ex) {}

                    for (int rw = 0; rw < rows; rw++)
                    {   
                        for (int cn = 0; cn < columns; cn++) 
                        {
                            
                            if (minefield[rw][cn].isMine() && !(minefield[rw][cn].isFlagged()))
                            {
                                try {cell = new ImageView(new Image(new FileInputStream("src/images/bombrevealed.gif")));} catch (FileNotFoundException ex) {}
                                cell.setFitWidth(25);
                                cell.setX(minefield[rw][cn].getX());
                                cell.setY(minefield[rw][cn].getY());
                                cell.setPreserveRatio(true);         
                                pane.getChildren().add(cell);
                            }

                            if (!minefield[rw][cn].isMine() && minefield[rw][cn].isFlagged())
                            {
                                try {cell = new ImageView(new Image(new FileInputStream("src/images/bombmisflagged.gif")));} catch (FileNotFoundException ex) {}
                                cell.setFitWidth(25);
                                cell.setX(minefield[rw][cn].getX());
                                cell.setY(minefield[rw][cn].getY());
                                cell.setPreserveRatio(true);         
                                pane.getChildren().add(cell);
                            }
                            
                        }
                    }
                    
                    try { minefield[deathRow][deathColumn].openCell();} catch (FileNotFoundException ex) {}
                    cell = new ImageView(minefield[deathRow][deathColumn].getImage());
                    cell.setFitWidth(25);
                    cell.setX(minefield[deathRow][deathColumn].getX());
                    cell.setY(minefield[deathRow][deathColumn].getY());
                    cell.setPreserveRatio(true);         
                    pane.getChildren().add(cell);
                }
                // </editor-fold>
                
                // <editor-fold defaultstate="collapsed" desc="Win">
                boolean noMines = true;
                for (int r = 0; r < rows; r++)
                {
                    for (int c = 0; c < columns; c++) 
                    {
                        if (minefield[r][c].isMine() && !(minefield[r][c].isFlagged()))
                        {
                            noMines = false;
                        }
                    }
                }
                if (noMines)
                {
                    try {smiley.setImage(new Image(new FileInputStream("src/images/facewin.gif")));} catch (FileNotFoundException ex) {}
                    won = true;
                }
                // </editor-fold>
            }
            if (mouseX > 225.0 && mouseY > 25.0 && mouseX < 275.0 && mouseY < 75.0) // Clicking Smiley;
            {
                try {
                  smiley.setImage(new Image(new FileInputStream("src/images/facesmile.gif")));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Minesweeper.class.getName()).log(Level.SEVERE, null, ex);
                }
                pane.getChildren().removeAll();
                try {startGame();} catch (FileNotFoundException ex) {}
                System.out.println("Resetting...");
            }
        }
        
    };
    
    public void openCell(int r, int c) throws FileNotFoundException
    {
        
        System.out.println("Column: "+minefield[r][c].getColumn());
        System.out.println("Row: "+minefield[r][c].getRow());
        try { minefield[r][c].openCell();} catch (FileNotFoundException ex) {}
        
        cell = new ImageView(minefield[r][c].getImage());
        cell.setFitWidth(25);
        cell.setX(minefield[r][c].getX());
        cell.setY(minefield[r][c].getY());
        cell.setPreserveRatio(true);         
        pane.getChildren().add(cell);
        
        
        if (minefield[r][c].nearbyMines() == 0 && !minefield[r][c].isMine())
        {
            floodFill(r, c);
        }
    }
    public void floodFill(int r, int c) throws FileNotFoundException
    {   
        
        // <editor-fold defaultstate="collapsed" desc="Opening Multiple Cells">
    
        if (r > 0) //If the Upper 3 Cells are empty...
        {
            if (true) // If N is a empty
            {       
                openCell(r - 1, c);
            }

            if (c > 0)
            {
                if (!minefield[r - 1][c - 1].isMine() && !minefield[r - 1][c - 1].isRevealed()) //If NW is a empty
                {
                    openCell(r - 1, c - 1);
                }
            }



            if (c < columns - 1)
            {
                if (!minefield[r - 1][c + 1].isMine() && !minefield[r - 1][c + 1].isRevealed()) //If NE is a empty
                {
                    openCell(r - 1, c + 1);
                }
            }
        }

        if (r < rows - 1) //If the Bottom 3 Cells are empty...
        {
            if (!minefield[r + 1][c].isMine() && !minefield[r + 1][c].isRevealed()) //If S is a empty
            {
                openCell(r + 1, c);
            }

            if (c > 0)
            {
                if (!minefield[r + 1][c - 1].isMine() && !minefield[r + 1][c - 1].isRevealed()) //If SW is a empty
                {
                    openCell(r + 1, c - 1);
                }
            }   

            if (c < columns - 1)
            {
                if (!minefield[r + 1][c + 1].isMine() && !minefield[r + 1][c + 1].isRevealed()) //If SE is a empty
                {
                    openCell(r + 1, c + 1);
                }
            }
        }

        if (c > 0) //If W is a empty...
        {
            if (!minefield[r][c - 1].isMine() && !minefield[r][c - 1].isRevealed()) //If W is a empty
            {
                openCell(r, c - 1);
            }
        }

        if (c < columns - 1) //If E is a empty...
        {
            if (!minefield[r][c + 1].isMine() && !minefield[r][c + 1].isRevealed()) //If E is a empty
            {
                openCell(r, c + 1);
            }
        }
        // </editor-fold>
   
    }
    
}
        

