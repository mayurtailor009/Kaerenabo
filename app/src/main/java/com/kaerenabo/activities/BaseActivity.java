package com.kaerenabo.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaerenabo.R;
import com.kaerenabo.utilities.LocaleManager;
import com.kaerenabo.utilities.MyContextWrapper;
import com.kaerenabo.utilities.TouchEffect;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

	public static final TouchEffect TOUCH = new TouchEffect();
	public Toolbar toolbar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//LocaleManager.updateResources(this);
		//Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(BaseFragment.this));
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		Locale languageType = LocaleManager.getLocale(newBase);
		super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
	}
	public View setTouchNClick(int id) {

		View v = findViewById(id);
		v.setOnClickListener(this);
		v.setOnTouchListener(TOUCH);
		return v;
	}
	
	public View setClick(int id) {

		View v = findViewById(id);
		v.setOnClickListener(this);
		return v;
	}
	
	public void setViewEnable(int id, boolean flag){
		View v = findViewById(id);
		v.setEnabled(flag);
	}
	
	public void setViewVisibility(int id, int flag){
		View v = findViewById(id);
		v.setVisibility(flag);
	}
	
	public void setTextViewText( int id, String text){
		((TextView)findViewById(id)).setText(text);
	}
	
	
	public void setTextViewHtmlText( int id, String text){
		TextView tv = ((TextView)findViewById(id));
		tv.setText(Html.fromHtml(text));
	}
	
	public void setTextViewTextColor(int id, int color){
		((TextView)findViewById(id)).setTextColor(color);
	}
	
	public void setEditText( int id, String text){
		((EditText)findViewById(id)).setText(text);
	}
	
	public String getEditTextText(int id){
		return ((EditText)findViewById(id)).getText().toString();
	}
	
	public String getTextViewText(int id){
		return ((TextView)findViewById(id)).getText().toString();
	}
	
	public String getButtonText(int id){
		return ((Button)findViewById(id)).getText().toString();
	}
	
	public void setButtonText( int id, String text){
		((Button)findViewById(id)).setText(text);
	}

	public void replaceButtoImageWith(int replaceId, int drawable){
		((Button)findViewById(replaceId)).setBackgroundResource(drawable);
	}
	
	public void setButtonSelected(int id, boolean flag){
		((Button)findViewById(id)).setSelected(flag);
	}
	public boolean isButtonSelected(int id){
		return ((Button)findViewById(id)).isSelected();
	}
	
	/**
	 * Method use to set view selected
	 * @param id resource id of view.
	 * @param flag true if view want to selected else false
	 */
	public void setViewSelected(int id, boolean flag){
		View v = findViewById(id);
		v.setSelected(flag);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		}
	}
	
	/**
	 * Method use to set text on TextView, EditText, Button.
	 * @param id resource of TextView, EditText, Button.
	 * @param text string you want to set on TextView, EditText, Button
	 */
	public void setViewText(int id, String text){
		View v = ((View)findViewById(id));
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			tv.setText(text);
		}
		if(v instanceof EditText){
			EditText et = (EditText)v;
			et.setText(text);
		}
		if(v instanceof Button){
			Button btn = (Button)v;
			btn.setText(text);
		}
		
	}
	
	public void setViewText(View view, int id, String text){
		View v = ((View)view.findViewById(id));
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			tv.setText(text);
		}
		if(v instanceof EditText){
			EditText et = (EditText)v;
			et.setText(text);
		}
		if(v instanceof Button){
			Button btn = (Button)v;
			btn.setText(text);
		}
		
	}
	
	/**
	 * Method use to get Text from TextView, EditText, Button.
	 * @param id resource id TextView, EditText, Button
	 * @return string text from view
	 */
	public String getViewText(int id){
		String text="";
		View v = ((View)findViewById(id));
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			text = tv.getText().toString().trim();
		}
		if(v instanceof EditText){
			EditText et = (EditText)v;
			text = et.getText().toString().trim();
		}
		if(v instanceof Button){
			Button btn = (Button)v;
			text = btn.getText().toString().trim();
		}
		return text;
	}
	
	public boolean isCheckboxChecked(int id){
		CheckBox cb = (CheckBox) findViewById(id);
		return cb.isChecked();
	}
	
	public void setImageResourseBackground(int id, int resId){
		ImageView iv = (ImageView) findViewById(id);
		iv.setImageResource(resId);
	}



	public Toolbar setupToolbar(String title){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			toolbar.setTitle(title);
			setSupportActionBar(toolbar);
		}
		return toolbar;
	}

/*	public void setBackButton(){
		toolbar.setNavigationIcon(R.drawable.back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//What to do on back clicked
				finish();
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_cart) {
			startActivity(new Intent(this, CartActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}*/
}
