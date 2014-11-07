package com.hp.expensemana;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import com.hp.expensemanager.R;

public class Starting extends Activity implements AnimationListener{

	Animation an;
	ImageView im;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starting);
		  im=(ImageView)findViewById(R.id.im);
	        an=AnimationUtils.loadAnimation(this,R.anim.animation);
	     	im.setAnimation(an);
	        im.startAnimation(an);
	        an.setAnimationListener(this);
		
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		Intent i=new Intent(this,MainActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}
