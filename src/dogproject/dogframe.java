package dogproject;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
public class dogframe extends JWindow {
    private  ImageIcon icon2=new ImageIcon(getClass().getClassLoader().getResource("dog walk left.gif"));
    private JLabel gif2=new JLabel(icon2);
    private ImageIcon icon1=new ImageIcon(getClass().getClassLoader().getResource("dog walk right.gif"));
    private JLabel gif1=new JLabel(icon1);
    private  ImageIcon icon3=new ImageIcon(getClass().getClassLoader().getResource("dog walk down.gif"));
    private JLabel gif3=new JLabel(icon3);
    private  ImageIcon icon4=new ImageIcon(getClass().getClassLoader().getResource("dog walk up.gif"));
    private JLabel gif4=new JLabel(icon4);
    private  ImageIcon icon5=new ImageIcon(getClass().getClassLoader().getResource("dog run right.gif"));
    private JLabel gif5=new JLabel(icon5);
    private  ImageIcon icon6=new ImageIcon(getClass().getClassLoader().getResource("dog sleep.gif"));
    private JLabel gif6=new JLabel(icon6);
    private  ImageIcon icon7=new ImageIcon(getClass().getClassLoader().getResource("dog wait.gif"));
    private JLabel gif7=new JLabel(icon7);
    private  ImageIcon icon8=new ImageIcon(getClass().getClassLoader().getResource("dog run left.gif"));
    private JLabel gif8=new JLabel(icon8);
    private  ImageIcon icon9=new ImageIcon(getClass().getClassLoader().getResource("dog bark (1).gif"));
    private JLabel gif9=new JLabel(icon9);
	private JLabel gif;
	private Clip clip;
	private TrayIcon trayIcon;
	private SystemTray tray;
	private int X,Y;
	private boolean move=true;
	private boolean issleep=false;
	private Image icon = new ImageIcon(getClass().getClassLoader().getResource("egg.png")).getImage();
    Preferences pref = Preferences.userNodeForPackage(Schedule.class);
    private AudioInputStream audioInputStream ; 
	private Date termin;
	private String notiz;
	private boolean task=false;
	private Schedule s;
	public dogframe() throws ParseException, UnsupportedAudioFileException, IOException {
		 super.setSize(100,100);
         super.setBackground(new Color(0,0,0,0));
         this.gif=gif1;
         super.add(gif);
         super.setLocationRelativeTo(null);
         super.setAlwaysOnTop (true);
         super.setVisible(true);
         setTray();
         setButton();
         setDrag();
         setTermin();
	}
	public void setTermin() throws ParseException {
		System.out.println("Termin num: "+pref.getInt("terminNum", 0));
		System.out.println("Termin added: "+pref.get("termin:1",""));
		if(pref.getInt("terminNum", 0)!=0) {termin=new SimpleDateFormat("mm,HH,dd,MM,yyyy").parse(pref.get("termin:1",""));notiz=pref.get("notiz:1","");}
		else {termin=null;}
	}
	public void bark() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		audioInputStream=AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("beep-08b.wav"));
		clip = AudioSystem.getClip(); 
		clip.open(audioInputStream); 
	    clip.loop(clip.LOOP_CONTINUOUSLY); 
	    clip.start();
	}
	public void stopBark() {clip.stop();clip.close();}
	public Date getTermin() {return termin;}
	public String getNotiz() {return notiz;}
	public boolean updatetask() throws ParseException {
		 Calendar cal = Calendar.getInstance();
 	     Date today= cal.getTime();
 	     if(termin!=null) {
 	         long difference_In_Minutes = ((termin.getTime()-today.getTime())/ (1000 * 60)) % 60; 
 	         System.out.println(difference_In_Minutes);
 	         if(difference_In_Minutes<=1) {setTermin();task=true;return true;}
 	     }
 	     System.out.println("Termin in dogframe:"+termin);
 	     setTermin();
 	     task=false;
 	     return false;	
	}
	public void setTaskFalse() {this.task=false;}
    public void finished() throws ParseException, BackingStoreException {
    	ArrayList<termin> arr=new ArrayList<termin>();
    	int num =pref.getInt("terminNum",0);
		for(int i=1; i<=pref.getInt("terminNum",0); i++) {
		    Date d=new SimpleDateFormat("mm,HH,dd,MM,yyyy").parse(pref.get("termin:"+String.valueOf(i),""));
			String s=pref.get("notiz:"+String.valueOf(i),"");
			termin t=new termin(d,s); 
			arr.add(t);
		}
		//System.out.println(arr.size());
		arr.remove(0);
		//System.out.println(arr.size());
		for(int i=0; i<arr.size(); i++) {
			String s=arr.get(i).getString();
			DateFormat dateFormat = new SimpleDateFormat("mm,HH,dd,MM,yyyy");  
 		    String d = dateFormat.format(arr.get(i).getDate());  
			pref.put("termin:"+String.valueOf(i+1),d);
        	pref.put("notiz:"+String.valueOf(i+1),s);
        	pref.putInt("terminNum",num-1);
		}
		if(arr.size()==0) {pref.putInt("terminNum",0);pref.clear();}
		setTermin();
    }
	public boolean getTask() {return task;}
	public void setButton() {
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if(getmove()) {setMoveFalse();}
            	else {setMoveTrue();}
                System.out.println("clicked");
            }
        });
	}
	public void setDrag() {
		 addMouseListener(new MouseAdapter() {
		        public void mousePressed(MouseEvent me) {
		          X=me.getX();
		          Y=me.getY();
		        }
		    });
		    addMouseMotionListener(new MouseMotionAdapter() {
		        public void mouseDragged(MouseEvent me) {
		          Point p = getLocation();
		          setLocation(p.x + (me.getX()-X), p.y + (me.getY()-Y));
		          setMoveFalse();
		        }
		    });
	}
	public boolean getmove() {return move;}
	public boolean getsleep() {return issleep;}
	public void hideaway() {super.hide();super.setLocation(-100,-100);System.out.println("sleep");}
	public void setMoveFalse() {this.move=false;}
	public void setMoveTrue() {this.move=true;}
	public void setTray() {
	    tray = SystemTray.getSystemTray();
	    PopupMenu menu = new PopupMenu();
	    MenuItem exit = new MenuItem("Exit");
	    exit.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	            close();
	        }
	    });
	    MenuItem stop = new MenuItem("Stop");
	    stop.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	            setMoveFalse();
	        }
	    });
	    MenuItem sleep = new MenuItem("Sleep");
	    sleep.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	        	if(issleep==false) {
	        		hideaway();
	        		setMoveFalse();
	                issleep=true;
	            }
	        }
	    });
	    MenuItem maketermin= new MenuItem("Make Termin");
	    maketermin.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		s=new Schedule();
	    		try {
					setTermin();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    });
	    MenuItem wake = new MenuItem("Wake Up");
	    wake.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	        	if(issleep==true) {
	        		setLocationRelativeTo(null);
	        		changegif(7);
	        		setVisible(true);
	        		setMoveTrue();
	                issleep=false;
	            }
	        }
	    });
	    menu.add(maketermin);
	    menu.addSeparator();
	    menu.add(sleep);
	    menu.addSeparator();
	    menu.add(wake);
	    menu.addSeparator();
	    menu.add(exit);
	    trayIcon = new TrayIcon(icon,"dog", menu);
	    trayIcon.setImageAutoSize(true);
	    try
	    {
	        tray.add(trayIcon);
	    } catch (AWTException e) {
	        e.printStackTrace();
	    }
	}
	public void close() {
	    System.exit(0);
	}
	public void changegif(int i) {
		if(i==1) {
			super.setVisible(false);
			super.remove(gif);
			this.gif=this.gif1;
			super.add(gif);
			super.setVisible(true);	
		}
		else if(i==2) {
		    super.setVisible(false);
		    super.remove(gif);
		    this.gif=this.gif2;
		    super.add(gif);
		    super.setVisible(true);	
		}
		else if(i==3) {
		    super.setVisible(false);
		    super.remove(gif);
		    this.gif=this.gif3;
		    super.add(gif);
		    super.setVisible(true);	
		}
		else if(i==4)  {
		    super.setVisible(false);
		    super.remove(gif);
		    this.gif=this.gif4;
		    super.add(gif);
		    super.setVisible(true);	
		}
		else if(i==5)  {
		    super.setVisible(false);
		    super.remove(gif);
		    this.gif=this.gif5;
		    super.add(gif);
		    super.setVisible(true);	
		}
		else if(i==6)  {
		    super.setVisible(false);
		    super.remove(gif);
		    this.gif=this.gif6;
		    super.add(gif);
		    super.setVisible(true);	
		}
		else if(i==7)  {
		    super.setVisible(false);
		    super.remove(gif);
		    this.gif=this.gif7;
		    super.add(gif);
		    super.setVisible(true);	
		}
		else if(i==8) {
			 super.setVisible(false);
			 super.remove(gif);
			 this.gif=this.gif8;
			 super.add(gif);
			 super.setVisible(true);	
		}
		else {
			super.setVisible(false);
			super.remove(gif);
			this.gif=this.gif9;
			super.add(gif);
			super.setVisible(true);	
		}
	}
}

