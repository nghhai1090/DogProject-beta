package dogproject;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextPane;

public class Notizwindow extends JFrame {
	private JTextPane termin=new JTextPane();
	private JTextPane notiz=new JTextPane();
	private Container c;
	private JButton button= new JButton("Close");
	private boolean closed=false;
	public Notizwindow(Date t,String n) {
		super.setSize(350,150);
		c= getContentPane();
		c.setLayout(new FlowLayout());
		termin.setText("You have Termin at : "+t);
		notiz.setText(n);
		button.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {setClosedTrue();dispose();}});
		c.add(termin);
		c.add(notiz);
		c.add(button);
		super.setAlwaysOnTop(true);
		super.setResizable(false);
		super.setLocationRelativeTo(null);
		super.setVisible(true);
	}
	public boolean getClosed() {return closed;}
	public void setClosedTrue() {closed=true;}
	public void setClosedFalse() {closed=false;}
}
