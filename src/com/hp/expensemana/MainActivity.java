package com.hp.expensemana;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TabHost.TabSpec;
import com.hp.expensemanager.R;

public class MainActivity extends Activity {

	SQLiteDatabase db;
	TextView tamount,dat;
	EditText event,amount;
	String last_date;
	Spinner spinner,spinner2,spinner3;
	ArrayAdapter<String> spinnerAdapter;
	String str;
	Cursor c;
	String date;
	int today_amount=0;

	//History
	ArrayAdapter<String> ob,ob1;
	ListView lv;
	String dd[],details[];
	int arr[],count=0;
	int sum=0,f=0,i=0;

	//Reminder
	ListView lv3;
	TextView tv3;
	String details1[],dd1[],arr1[],price1[];
	int pos;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//Making tabs
		TabHost host = (TabHost)findViewById(R.id.tabhost);
		host.setup();

		TabSpec spec = host.newTabSpec("Tab One");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Today");
		host.addTab(spec);

		spec = host.newTabSpec("Tab Two");
		spec.setContent(R.id.tab2);
		spec.setIndicator("History");
		host.addTab(spec);
		
		spec = host.newTabSpec("Tab Three");
		spec.setContent(R.id.tab3);
		spec.setIndicator("Reminder");
		host.addTab(spec);
		
		
		//Getting current time
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
	date = df.format(cal.getTime());
		
		tamount=(TextView)findViewById(R.id.tamount);

		

		
		dat=(TextView)findViewById(R.id.date);
		event=(EditText)findViewById(R.id.event);
		amount=(EditText)findViewById(R.id.amount);
		db=openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);

		
		//creating  table
		try{
		db.execSQL("create table expense (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,date date,event varchar(20),amount varchar(10),mode varchar(15));");
		//Toast.makeText(this, "Table Created", Toast.LENGTH_LONG).show();
		tamount.setText("0");
		}catch(Exception e){
			//Toast.makeText(this, "Table already exist", Toast.LENGTH_LONG).show();
		}
		
		
		//Reminder
		lv3=(ListView)findViewById(R.id.lv3);
	try
		{
			db.execSQL("create table reminder (dd date,event varchar(50),price varchar(10));");
			//Toast.makeText(this, "ReminderTable Created", Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
		//	Toast.makeText(this, "not Created", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		c=db.rawQuery("select * from reminder where dd='"+date+"';",null);
		count=c.getCount();
		//Toast.makeText(this,""+count,Toast.LENGTH_LONG).show();
		while(c.moveToNext())
		{
			AlertDialog.Builder notice=new AlertDialog.Builder(this);
			notice.setTitle("Reminder");
			notice.setMessage("Rs."+c.getString(2)+" should be spent on "+c.getString(1)+".Please delete reminder from Reminder tab if you have already done it.");
			notice.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{}
			});
			notice.show();
			}
	c=db.rawQuery("select * from reminder where dd>='"+date+"';",null);
		count=c.getCount();
	//	Toast.makeText(Reminder.this,""+count,Toast.LENGTH_LONG).show();
		i=0;
		
		if(count>0)
		{
			//tv3.setText("     DETAILS");
			details1= new String[count];
			arr1= new String[count];
			dd1= new String[count];
			price1= new String[count];
			while(c.moveToNext())
			{
				dd1[i]=c.getString(0);
				arr1[i]=c.getString(1);
				price1[i]=c.getString(2);
				details1[i]=dd1[i]+"     "+arr1[i]+"    	Rs."+price1[i] ;
				i++;
			}
		//	Toast.makeText(Reminder.this,details[0],Toast.LENGTH_LONG).show();
			ob=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,details1);
			lv3.setAdapter(ob);
			lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
			{
				pos=arg2;
				AlertDialog.Builder notice1=new AlertDialog.Builder(MainActivity.this);
				notice1.setTitle("Delete");
				notice1.setMessage("This reminder will be deleted.");
				notice1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String da=dd1[pos];
					String ev=arr1[pos];
					try
					{
						db.execSQL("delete from reminder where dd='"+da+"' and event='"+ev+"';");
						Toast.makeText(MainActivity.this,"Reminder deleted.",Toast.LENGTH_LONG).show();
					}	
					catch(Exception e)
					{
						e.printStackTrace();
					}
					Cursor c=db.rawQuery("select * from reminder where dd>='"+date+"';",null);
					count=c.getCount();
					i=0;
					if(count>0)
					{
						
						details1= new String[count];
						arr1= new String[count];
						dd1= new String[count];
						price1= new String[count];
						while(c.moveToNext())
						{
							dd1[i]=c.getString(0);
							arr1[i]=c.getString(1);
							price1[i]=c.getString(2);
							details1[i]=dd1[i]+"     "+arr1[i]+"    	Rs."+price1[i] ;
							i++;
						}
					}	
					else
					{
						//tv3.setText("NO REMINDER SET");
						details1[i]="";
					}
					ob=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,details1);
					lv3.setAdapter(ob);
				}
				}); 
				notice1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
				
				Toast.makeText(MainActivity.this,"Deletion cancelled.",Toast.LENGTH_LONG).show();
				}
				});
				notice1.show();
				
			}
			});
		}
		else
		{
			//tv3.setText("NO REMINDER SET");
		}	

		
		
		//saving the last date of the database so that we can get the expenses of present day(last entry)
		c=db.rawQuery("SELECT date FROM expense order by ID desc limit 1 ", null);

		if(c.moveToNext())
		{
			last_date=c.getString(0);
		}

	
		//making spinner to list the expenses of present day
		spinner = (Spinner)findViewById(R.id.spinner);
		spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerAdapter);
	
//
		spinner2 = (Spinner)findViewById(R.id.spinner2);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        // your code here
		    	if(id==0)
		    	{
		    	}else
		    	{
		    		event.setText(spinner2.getSelectedItem().toString());
		    	}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		spinner3 = (Spinner)findViewById(R.id.spinner3);
		

		//adding data to spinner for the present day
		c=db.rawQuery("SELECT event,amount,mode FROM expense where date='"+last_date+"'", null);
		spinnerAdapter.add("Details");
		if(date.equals(last_date))
		{
			while(c.moveToNext())
			{try{
					today_amount = today_amount + Integer.parseInt(c.getString(1));
					str="Rs."+c.getString(1)+"("+c.getString(2)+ ")"+ "spent on "+c.getString(0);
					spinnerAdapter.add(str);
					spinnerAdapter.notifyDataSetChanged();
				}catch(Exception ex)
				{}
			}
		}

		//displaying current date and amount spent of that day when the app starts
			tamount.setText(today_amount+"");
			dat.setText(date);
		
		//history
		lv=(ListView)findViewById(R.id.lv);
		 RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg);   
		 
	//Displaying history list of last 7 days
 		c=db.rawQuery("select sum(amount),date from expense group by date having date<'"+date+"';",null);
 		count=c.getCount();
 		//Toast.makeText(History.this,""+count,Toast.LENGTH_LONG).show();
 		i=0;
 		f=0;
 	if(count>0)
 	{	
 	
 		if(count>7)
 		{
 			arr= new int[7];
 			dd= new String[7];
 			details= new String[7];
 			while(c.moveToNext())
 			{
 				f++;
 				if(f>(count-7))
 				{
 					arr[i]=c.getInt(0);
 					dd[i]=c.getString(1);
 					details[i]=dd[i]+"	Amount Spent = Rs."+arr[i] ;
 					i++;
 				}
 			}
 		}
 		else
 		{
 			arr= new int[count];
 			dd= new String[count];
 			details= new String[count];
 			//Toast.makeText(History.this,"yes",Toast.LENGTH_LONG).show();
 			while(c.moveToNext())
 			{
 				arr[i]=c.getInt(0);
 				dd[i]=c.getString(1);
 				details[i]=dd[i]+"	Amount Spent=Rs"+arr[i] ;
 				i++;
 			}
 		}
 		ArrayAdapter<String> ob=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,details);
 		lv.setAdapter(ob);
 		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
 			@Override
 			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
 			{
 				String date=dd[arg2];
 				Intent i=new Intent(MainActivity.this,Hist_details_date.class);
 				i.putExtra("m",date);
 				startActivity(i);
 			}
 		});
 	}
 	else
 	{
 		
 	}
		 
		 
		 radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		    {
			
		        public void onCheckedChanged(RadioGroup group, int checkedId) {
		          
		        	switch (checkedId)
		        	{
		        	//History for date
		        	case R.id.r1:
		        		c=db.rawQuery("select sum(amount),date from expense group by date having date<'"+date+"';",null);
		        		count=c.getCount();
		        		//Toast.makeText(History.this,""+count,Toast.LENGTH_LONG).show();
		        		i=0;
		        		f=0;
		        	if(count>0)
		        	{	
		        	
		        		if(count>7)
		        		{
		        			arr= new int[7];
		        			dd= new String[7];
		        			details= new String[7];
		        			while(c.moveToNext())
		        			{
		        				f++;
		        				if(f>(count-7))
		        				{
		        					arr[i]=c.getInt(0);
		        					dd[i]=c.getString(1);
		        					details[i]="Rs."+arr[i]+" spent on "+dd[i];
		        					i++;
		        				}
		        			}
		        		}
		        		else
		        		{
		        			arr= new int[count];
		        			dd= new String[count];
		        			details= new String[count];
		        			//Toast.makeText(History.this,"yes",Toast.LENGTH_LONG).show();
		        			while(c.moveToNext())
		        			{
		        				arr[i]=c.getInt(0);
		        				dd[i]=c.getString(1);
		        				details[i]="Rs."+arr[i]+" spent on "+dd[i] ;
		        				i++;
		        			}
		        		}
		        		ArrayAdapter<String> ob=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,details);
		        		lv.setAdapter(ob);
		        		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		        			@Override
		        			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
		        			{
		        				String date=dd[arg2];
		        				Intent i=new Intent(MainActivity.this,Hist_details_date.class);
		        				i.putExtra("m",date);
		        				startActivity(i);
		        			}
		        		});
		        	}
		        	else
		        	{
		        		
		        	}
		        		
		        	

		        		break;
		        		//for category or event history
		        	case R.id.r2:
		        		 //copied
		        		c=db.rawQuery("select sum(amount),event from expense group by event having date<'"+date+"';",null);
		        		count=c.getCount();
		        		//Toast.makeText(History.this,""+count,Toast.LENGTH_LONG).show();
		        		i=0;
		        		f=0;
		        	if(count>0)
		        	{	
		        	
		        		if(count>7)
		        		{
		        			arr= new int[7];
		        			dd= new String[7];
		        			details= new String[7];
		        			while(c.moveToNext())
		        			{
		        				f++;
		        				if(f>(count-7))
		        				{
		        					arr[i]=c.getInt(0);
		        					dd[i]=c.getString(1);
		        					details[i]="Rs."+arr[i]+" spent on "+dd[i];
		        					i++;
		        				}
		        			}
		        		}
		        		else
		        		{
		        			arr= new int[count];
		        			dd= new String[count];
		        			details= new String[count];
		        			//Toast.makeText(History.this,"yes",Toast.LENGTH_LONG).show();
		        			while(c.moveToNext())
		        			{
		        				arr[i]=c.getInt(0);
		        				dd[i]=c.getString(1);
		        				details[i]="Rs."+arr[i]+" spent on"+ dd[i];
		        				i++;
		        			}
		        		}
		        		ArrayAdapter<String> ob=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,details);
		        		lv.setAdapter(ob);
		        		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		        			@Override
		        			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
		        			{
		        				String event=dd[arg2];
		        				Intent i=new Intent(MainActivity.this,Hist_details_event.class);
		        				i.putExtra("m",event);
		        				startActivity(i);
		        			}
		        		});
		        	}
		        	else
		        	{
		        		
		        	}
		        		
		        	

		        		break;
		        		
		        		
		        	}
		        	
		        }
		    });
		
		

	}

		
		
	
	
	
	//onclick for save button
	public void save(View v)
	{
		String ta=tamount.getText().toString().trim();
		String d=dat.getText().toString().trim();
		String e=event.getText().toString().trim();
		String a=amount.getText().toString().trim();
		
		int flag=1;
		
		 String m=spinner3.getSelectedItem().toString();
		//Saving the last date of the table
		c=db.rawQuery("SELECT date FROM expense order by ID desc limit 1 ", null);
		if(c.moveToNext())
		{
			last_date=c.getString(0);
		}
		
		//inserting new entry into the table
		try{
			db.execSQL("insert into expense (date,event,amount,mode) values ('"+d+"','"+e+"','"+a+"','"+m+"');");
			Toast.makeText(this, "Entered", Toast.LENGTH_LONG).show();
			
		}catch(Exception ex)
		{	
			flag=0;
			Toast.makeText(this, "Invalid Entry", Toast.LENGTH_LONG).show();
		}
		
		//if date is 
		int nta=0;
		if(d.equals(last_date))
		{
			
			try{
			nta = Integer.parseInt(a)+Integer.parseInt(ta);
			}catch(Exception ex){
				flag=0;
				Toast.makeText(this, "Invalid Amount", Toast.LENGTH_LONG).show();
			}
			
			tamount.setText(nta + "");	
		}else
		{
			spinnerAdapter.clear();
			spinnerAdapter.add("Details");
	
			try{
				nta = Integer.parseInt(a);
				}catch(Exception ex){
					flag=0;
					Toast.makeText(this, "Invalid Amount", Toast.LENGTH_LONG).show();
				}
			
			tamount.setText(nta + "");
			
			
		}
		
		event.setText("");
		amount.setText("");
		spinner2.setSelection(0);
		
		if(flag==1){
		str="Rs."+a+"("+m+ ") spent on "+e;
		spinnerAdapter.add(str);
		spinnerAdapter.notifyDataSetChanged();
		}
		
		
	
}

	
	public void add(View v)
	{
		Intent i=new Intent(MainActivity.this,AddReminder.class);
		startActivity(i);
		finish();
	}
		
}
