package dogproject;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.prefs.*;
public class Schedule extends JFrame {
    private Container c;
    private String termin="";
    private String notiz="";
    private JButton ok= new JButton("Set");
    private JButton cancel = new JButton("Cancel");
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private JComboBox<Integer> timeList;
    private JComboBox<Integer> minuteList;
    private JTextField text;
    Preferences pref=Preferences.userNodeForPackage(this.getClass());
	public Schedule() {
		super.setSize(250,200);
		c= getContentPane();
		timeList = new JComboBox<Integer>();
		for(int i= 1; i<=24; i++) {timeList.addItem(i);}
		minuteList = new JComboBox<Integer>();
		for(int i= 0; i<=59; i++) {minuteList.addItem(i);}
		c.setLayout(new FlowLayout());
		c.add(new JLabel("Hour"));
		c.add(timeList);
		c.add(new JLabel("Minute"));
		c.add(minuteList);
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
	    datePanel = new JDatePanelImpl(model,p);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		c.add(datePicker);
		text = new JTextField(10);
		c.add(text);
		c.add(ok);
		c.add(cancel);
		setButton();
		super.setResizable(false);
		super.setLocationRelativeTo(null);
		super.setVisible(true);
	}
	public boolean checkPref() {
		//check if pref have <5 termin
		if(pref.getInt("terminNum",0)<5) {return true;}
		return false;
	}  
	public void sortpref() throws ParseException {
		//sort pref by date time
		ArrayList<termin> arr=new ArrayList<termin>();
		for(int i=1; i<=pref.getInt("terminNum",0); i++) {
		    Date d=new SimpleDateFormat("mm,HH,dd,MM,yyyy").parse(pref.get("termin:"+String.valueOf(i),""));
			String s=pref.get("notiz:"+String.valueOf(i),"");
			termin t=new termin(d,s); 
			arr.add(t);
		}
		Collections.sort(arr, new Comparator<termin>() { public int compare(termin o1, termin o2) {return -(o2.getDate().compareTo(o1.getDate())); } });
		for(int i=0; i<arr.size(); i++) {
			String s=arr.get(i).getString();
			DateFormat dateFormat = new SimpleDateFormat("mm,HH,dd,MM,yyyy");  
 		    String d = dateFormat.format(arr.get(i).getDate());  
			pref.put("termin:"+String.valueOf(i+1),d);
        	pref.put("notiz:"+String.valueOf(i+1),s);
		}
	}
	public void printTermin() {
		for(int i=1; i<=pref.getInt("terminNum",0); i++) {
			System.out.println(i+"Termin: "+pref.get("termin:"+String.valueOf(i),""));
			System.out.println(i+"Notiz: "+pref.get("notiz:"+String.valueOf(i),""));
		}
	    System.out.println("Terminnum: "+pref.getInt("terminNum",0));
	}
	public void delete() {
		try {
			pref.clear();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void setButton() {
		ok.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	        	try {
	        		boolean valid=false;
	        	    Date selectedDate = (Date) datePicker.getModel().getValue();
        		    DateFormat dateFormat = new SimpleDateFormat("dd,MM,yyyy");  
        		    String strDate = dateFormat.format(selectedDate);  
        		    termin=minuteList.getSelectedItem()+","+timeList.getSelectedItem()+","+strDate;
        		    Date d=new SimpleDateFormat("mm,HH,dd,MM,yyyy").parse(termin);
        		    Calendar cal = Calendar.getInstance();
	        	    Date today= cal.getTime();
        		    if(d.compareTo(today)>=0) {valid=true;}
        		    if(text.getText()!=null) {notiz=text.getText();}
                    //System.out.println(termin);
                    //System.out.println(notiz);
                    if(checkPref()&&!isempty(termin)&&valid==true) {
                    	//add new termin to pref
                    	int terminnum=pref.getInt("terminNum",0)+1;
                    	pref.put("termin:"+String.valueOf(terminnum), termin);
                    	pref.put("notiz:"+String.valueOf(terminnum),notiz);
                    	pref.putInt("terminNum",terminnum);
                    	sortpref();
                    	dispose();
                    }
                    else {
                    	if(valid==false||isempty(termin)) {JOptionPane.showMessageDialog(ok, "Please enter valid termin date");}
                    	if(!checkPref()) {JOptionPane.showMessageDialog(ok, "Termin List full, please delete");}
                    }
	        	}
	        	catch(Exception ex) {}
	        }
	    });
		cancel.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent e) {dispose();}
		});
	}
	public  void finished() throws ParseException, BackingStoreException {
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
		//setTermin();
    }
	public boolean isempty(String s) {
		String[]cut=s.split(",");
		for(int i=0;i<cut.length;i++) {if(cut[i].isEmpty())return true;}
		return false;
	}
	public static void main(String[]args) throws ParseException, BackingStoreException {
		Schedule s= new Schedule();
		s.printTermin();
		//s.finished();
		//s.printTermin();
		//s.delete();
	}
}
