/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;

/**
 *
 * @author 161465
 */
public class Cell {
    
    int row;
    int column;
    int xLoc;
    int yLoc;
    boolean isMine;
    boolean isFlagged;
    boolean revealed;
    
    Image cell;
    Image open0;
    Image open1;
    Image open2;
    Image open3;
    Image open4;
    Image open5;
    Image open6;
    Image open7;
    Image open8;
    Image flagged;
    Image bomb;
    Image bombDeath;
    Image blank;
    
    int mineCount;
    
    
    
    
    public Cell(int c, int r, int x, int y) throws FileNotFoundException
    {
        column = c;
        row = r;
        xLoc = x;
        yLoc = y;
        
        cell = new Image(new FileInputStream("src/images/blank.gif"));
        
        blank = new Image(new FileInputStream("src/images/blank.gif"));
        open0 = new Image(new FileInputStream("src/images/open0.gif"));
        open1 = new Image(new FileInputStream("src/images/open1.gif"));
        open2 = new Image(new FileInputStream("src/images/open2.gif"));
        open3 = new Image(new FileInputStream("src/images/open3.gif"));
        open4 = new Image(new FileInputStream("src/images/open4.gif"));
        open5 = new Image(new FileInputStream("src/images/open5.gif"));
        open6 = new Image(new FileInputStream("src/images/open6.gif"));
        open7 = new Image(new FileInputStream("src/images/open7.gif"));
        open8 = new Image(new FileInputStream("src/images/open8.gif"));
        
        flagged = new Image(new FileInputStream("src/images/bombflagged.gif"));
                
        bomb =  new Image(new FileInputStream("src/images/bombrevealed.gif"));
        bombDeath =  new Image(new FileInputStream("src/images/bombdeath.gif"));
    }
    
    public void openCell() throws FileNotFoundException
    {
        revealed = true;
        
        if (isMine)
        {
            System.out.println("dead");
            cell = bombDeath;
        }
        
        if (isFlagged)
        {
            cell = flagged;
        }
        
        if (!(isMine) && !(isFlagged)) 
        {
            cell = open0;
            
            if (mineCount == 1)
            {
                cell = open1;
            }
            
            if (mineCount == 2)
            {
                cell = open2;
            }
            
            if (mineCount == 3)
            {
                cell = open3;
            }
            
            if (mineCount == 4)
            {
                cell = open4;
            }
            
            if (mineCount == 5)
            {
                cell = open5;
            }
            
            if (mineCount == 6)
            {
                cell = open6;
            }
            
            if (mineCount == 7)
            {
                cell = open7;
            }
            
            if (mineCount == 7)
            {
                cell = open8;
            }
            
        }
    }
    
    //Cell Location
    public int getColumn(){return column;}
    public int getRow(){return row;}
    public int getX(){return xLoc;}
    public int getY(){return yLoc;}
    
    //Cell Status
    public Image getImage(){return cell;}
    public boolean isMine(){return isMine;}
    public boolean isFlagged(){return isFlagged;}
    public int nearbyMines(){return mineCount;}
    public boolean isRevealed(){return revealed;}
    
    
    //Changing Cells
    public void addMine(boolean status) throws FileNotFoundException{isMine = status; cell = new Image(new FileInputStream("src/images/bombrevealed.gif"));}
    public void addFlag(boolean status)
    {
        isFlagged = status;
        if (!(status))
        {
            cell = blank;
        }
    }
    public void setMineCount(int mines){mineCount += mines;}
    
}
