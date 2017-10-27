package com.gt.cscity.planning.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.gt.cscity.planning.R;
import com.gt.cscity.planning.utils.CommValues;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



public class ServerSettingsActivity extends Activity {

	private RadioGroup rg;
	private RadioButton r1;
	private RadioButton r2;
	private EditText newdataurl;
	private EditText dzdtip;
	private EditText dzdtname;
	private EditText shiliangip;
	private EditText shiliangname;
	private EditText yingxiangip;
	private EditText yingxiangname;
	private Button comfbtn;
	private Button cancbtn;
	private ImageButton toggle;
	private Editor edit = LoginActivity.sp.edit();
	private String previous = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_settings);
		init();
		edit.putBoolean("MAPISCHECK", true).commit();
	}
	//初始化控件信息
	private int loginType = -1;
	private void init(){
		rg = (RadioGroup) findViewById(R.id.rg);
		r1 = (RadioButton) findViewById(R.id.r1);
		r2 = (RadioButton) findViewById(R.id.r2);
		newdataurl=(EditText) findViewById(R.id.newdataurl);
		dzdtip=(EditText) findViewById(R.id.dzdtip);
		dzdtname=(EditText) findViewById(R.id.dzdtname);
		shiliangip=(EditText) findViewById(R.id.shiliangip);
		shiliangname=(EditText) findViewById(R.id.shiliangname);
		yingxiangip=(EditText) findViewById(R.id.yingxiangip);
		yingxiangname=(EditText) findViewById(R.id.yingxiangname);
		newdataurl.setOnFocusChangeListener(focusChangeListener);
		dzdtip.setOnFocusChangeListener(focusChangeListener);
		dzdtname.setOnFocusChangeListener(focusChangeListener);
		shiliangip.setOnFocusChangeListener(focusChangeListener);
		shiliangname.setOnFocusChangeListener(focusChangeListener);
		yingxiangip.setOnFocusChangeListener(focusChangeListener);
		yingxiangname.setOnFocusChangeListener(focusChangeListener);
		loginType=LoginActivity.sp.getInt("loginType", -1);
		String fwqurl=LoginActivity.sp.getString("fwqurl", "");
		String dzdtipValue=LoginActivity.sp.getString("dzdtipValue", "");
		String dzdtnameValue=LoginActivity.sp.getString("dzdtnameValue", "");
		String shiliangipValue=LoginActivity.sp.getString("shiliangipValue", "");
		String shiliangnameValue=LoginActivity.sp.getString("shiliangnameValue", "");
		String yingxiangipValue=LoginActivity.sp.getString("yingxiangipValue", "");
		String yingxiangnameValue=LoginActivity.sp.getString("yingxiangnameValue", "");
		if(loginType!=-1){
			if(loginType==1){
				r2.setChecked(true);
			}else if(loginType==0){
				r1.setChecked(true);
			}
		}else{
			switch(rg.getCheckedRadioButtonId()){
				case R.id.r1:
					loginType = 0;
					break;
				case R.id.r2:
					loginType = 1;
					break;
			}
		}
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
					case R.id.r1:
						loginType = 0;
						break;
					case R.id.r2:
						loginType = 1;
						break;
				}
			}
		});
		if(fwqurl!=null&&!fwqurl.equals(""))
			newdataurl.setText(LoginActivity.sp.getString("fwqurl", ""));
		if(dzdtipValue!=null&&!dzdtipValue.equals(""))
			dzdtip.setText(LoginActivity.sp.getString("dzdtipValue", ""));
		if(dzdtnameValue!=null&&!dzdtnameValue.equals(""))
			dzdtname.setText(LoginActivity.sp.getString("dzdtnameValue", ""));
		if(shiliangipValue!=null&&!shiliangipValue.equals(""))
			shiliangip.setText(LoginActivity.sp.getString("shiliangipValue", ""));
		if(shiliangnameValue!=null&&!shiliangnameValue.equals(""))
			shiliangname.setText(LoginActivity.sp.getString("shiliangnameValue", ""));
		if(yingxiangipValue!=null&&!yingxiangipValue.equals(""))
			yingxiangip.setText(LoginActivity.sp.getString("yingxiangipValue", ""));
		if(yingxiangnameValue!=null&&!yingxiangnameValue.equals(""))
			yingxiangname.setText(LoginActivity.sp.getString("yingxiangnameValue", ""));
		comfbtn=(Button)findViewById(R.id.comfbtn);
		comfbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveUrl();
			}
		});
		cancbtn=(Button) findViewById(R.id.cancbtn);
		cancbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ServerSettingsActivity.this.finish();
			}
		});
		toggle=(ImageButton) findViewById(R.id.toggle);
		toggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ServerSettingsActivity.this.finish();
			}
		});
	}
	//获取焦点和失去焦点处理
	OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			EditText editText = (EditText)v;
			if(hasFocus){
				Editable text = editText.getText();
				if(text!=null&&!"".equals(text.toString().trim()))
					previous = text.toString();
			}else{
				if(previous!=null&&editText.getText().toString().equals(""))
					editText.setText(previous);
			}
		}
	};
	//保存
	private void saveUrl(){
		String url = newdataurl.getText().toString().trim();
		String dzdtipValue = dzdtip.getText().toString().trim();
		String dzdtnameValue = dzdtname.getText().toString().trim();
		String shiliangipValue = shiliangip.getText().toString().trim();
		String shiliangnameValue = shiliangname.getText().toString().trim();
		String yingxiangipValue = yingxiangip.getText().toString().trim();
		String yingxiangnameValue = yingxiangname.getText().toString().trim();
		if(url.equals("")||dzdtipValue.equals("")||dzdtnameValue.equals("")||shiliangipValue.equals("")||shiliangnameValue.equals("")||yingxiangipValue.equals("")||yingxiangnameValue.equals("")){
			Toast.makeText(this, "新地址不可以为空", Toast.LENGTH_LONG).show();
		}else{
			edit.putInt("loginType", loginType).commit();
			edit.putString("fwqurl", url).commit();
			CommValues.bsUrl = url;
			edit.putString("dzdtipValue", dzdtipValue).commit();
			edit.putString("dzdtnameValue", dzdtnameValue).commit();
			edit.putString("shiliangipValue", shiliangipValue).commit();
			edit.putString("shiliangnameValue", shiliangnameValue).commit();
			edit.putString("yingxiangipValue", yingxiangipValue).commit();
			edit.putString("yingxiangnameValue", yingxiangnameValue).commit();
			CommValues.Inite(ServerSettingsActivity.this);
			ServerSettingsActivity.this.finish();
		}
	}
//	public int urlCode(String url){
//		HttpGet httpget = new HttpGet(url);
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpResponse httpResponse;
//		try {
//			httpResponse = client.execute(httpget);
//			int statusCode = httpResponse.getStatusLine().getStatusCode();
//
//			return statusCode;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return 0;
//		}
//	}

}
