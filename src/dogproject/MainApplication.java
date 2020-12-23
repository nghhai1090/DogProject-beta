package dogproject;
//created by hoang hai nguyen
// idea by hoang hai nguyen
// use some blocks of code from stack over flow
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static java.awt.GraphicsDevice.WindowTranslucency.*;
import java.awt.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
public class MainApplication {
    
    
    public static void main(String[] args) throws ParseException, BackingStoreException, UnsupportedAudioFileException, IOException, LineUnavailableException {
    	dogframe frame = new dogframe();
    	System.out.println("anfang "+frame.getX()+" "+frame.getY());
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	while(true) {
    		//System.out.println(frame.getTask());
    		int de= (int)( 4* Math.random());
    		int randX = (int)( ((int)screenSize.getWidth()-70)* Math.random());//in normal
    		int randY = (int)( ((int)screenSize.getHeight()-70)* Math.random());//in normal
    		if(frame.getTask()==true) {randX=(int)(screenSize.getWidth()*2/3);randY=(int)(screenSize.getHeight()*1/2);}
    		//System.out.println(randX+" "+randY+" "+de);
    		int deltaX = randX-frame.getX();
    		int deltaY = randY-frame.getY();
    		if(Math.abs(deltaX)>=Math.abs(deltaY)) {
    			if(deltaX>=0) {
    			    if(de<=2) {Testgif(5,deltaX,deltaY,frame,4,frame.getTask());}
    				else {Testgif(1,deltaX,deltaY,frame,2,frame.getTask());}
    		    }
    			else {
    				if(de<=2) {Testgif(8,deltaX,deltaY,frame,4,frame.getTask());}
    				else {Testgif(2,deltaX,deltaY,frame,2,frame.getTask());}
    			}
    		}
    		else if(Math.abs(deltaX)<Math.abs(deltaY)) {
    			if(deltaY<0) {Testgif(4,deltaX,deltaY,frame,2,frame.getTask());}
    			else {Testgif(3,deltaX,deltaY,frame,2,frame.getTask());}
    		}
    		if(de>1&&!frame.getTask()) { 
    			if(de==2) {
    				Testgif(7,0,0,frame,2,false);
    				try {Thread.sleep(3000);}catch(Exception e) {}
    			}
    			else {
    		        Testgif(6,0,0,frame,2,false);
    				try {Thread.sleep(5900);}catch(Exception e) {}
    			}
    		}
    	}
    }
    
    public static void Testgif(int i, int x, int y,dogframe frame, int s,boolean task) throws ParseException, BackingStoreException, LineUnavailableException, IOException, UnsupportedAudioFileException {
    	frame.changegif(i);
        Timer t=move(frame, x,y,s);
        if(!task) {
        	while(t.isRunning()) {
        		if(frame.getmove()==false||frame.updatetask()) {t.stop();}
        		System.out.println("updatetask:"+frame.updatetask());}
        }
        else if(task) {
        	Date tr=new Date();
        	String n="";
        	while(t.isRunning()) {}
        	//change gif ?
            //make window
        	tr=frame.getTermin();n=frame.getNotiz();
        	Notizwindow noti=new Notizwindow(tr,n);
        	frame.bark();
        	frame.changegif(9);
        	boolean closed=noti.getClosed();
        	while(!closed) {closed=noti.getClosed();System.out.println(closed);}
        	frame.stopBark();
        	System.out.println(closed);
        	noti.setClosedFalse();
        	frame.setTaskFalse();
        	frame.finished();
        }
        //System.out.println("reached");      
    }
    public static Timer move(JWindow frame, int deltaX, int deltaY,int s) {
    	int xMoveBy = deltaX > 0 ? s : -s;
        int yMoveBy = deltaY > 0 ? s : -s;
        int targetX = frame.getX() + deltaX;
        int targetY = frame.getY() + deltaY;
        Timer timer = new Timer(75, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int frameX = frame.getX();
                int frameY = frame.getY();
                if (deltaX > 0) {
                    frameX = Math.min(targetX, frameX + xMoveBy);
                } else if (deltaX<0) {
                    frameX = Math.max(targetX, frameX + xMoveBy);
                }
                else {
                	frameX = Math.max(targetX, frameX);
                }
                if (deltaY > 0) {
                    frameY = Math.min(targetY, frameY + yMoveBy);
                } else if (deltaY<0) {
                    frameY = Math.max(targetY, frameY + yMoveBy);
                }
                else {
                	frameY = Math.max(targetY, frameY);
                }
                frame.setLocation(frameX, frameY);
               // System.out.println("reached "+frame.getX()+" "+frame.getY());
                if (frameX == targetX && frameY == targetY) {
                    ((Timer)e.getSource()).stop();
                   // System.out.println("reached "+frame.getX()+" "+frame.getY()+" "+((Timer)e.getSource()).isRunning());
                }
            }
        });
        timer.start();
        return timer;
    }

}
