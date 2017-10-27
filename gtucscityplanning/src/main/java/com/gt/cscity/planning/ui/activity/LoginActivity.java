package com.gt.cscity.planning.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gt.cscity.planning.R;
import com.gt.cscity.planning.service.SyncService;
import com.gt.cscity.planning.utils.CangShuPlanning;
import com.gt.cscity.planning.utils.CommValues;
import com.gt.cscity.planning.utils.CommonHelper;
import com.gt.cscity.planning.utils.DBExHelper;
import com.gt.cscity.planning.utils.DataBaseUtil;
import com.gt.cscity.planning.utils.EncryptHelper;
import com.gt.cscity.planning.utils.ModuleInfo;
import com.gt.cscity.planning.utils.ModuleInfoDao;
import com.gt.cscity.planning.utils.MyJSONUtil;
import com.gt.cscity.planning.utils.PublicContainerParamUtil;
import com.gt.cscity.planning.utils.RepeatClickUtils;
import com.gt.cscity.planning.utils.User;
import com.gt.cscity.planning.utils.UserDao;
import com.gt.cscity.planning.utils.UserInfo;
import com.gt.cscity.planning.utils.UserInfoDao;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public final static int NONE = 0;
    // 无网络
    public final static int WIFI = 1;
    // Wi-Fi
    public final static int MOBILE = 2;
    // 3G,GPRS
    private Context context;
    private EditText login_number_edit;// 用户名
    private EditText login_password_edit;// 密码
    public static CheckBoxView checkbox1;// 记住密码选择框
    private TextView server_setting;
    private TextView loginCancer;
    // public static CheckBox checkbox2;//自动登陆选择框
    private final String EMPTYSTR = "";
    public static SharedPreferences sp;
    private int type = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        context = getApplicationContext();
        type = getNetworkState(LoginActivity.this);
        copyDataBaseToPhone();
        copyXmlToPhone();
        copyMapToPhone("VectorMap","csxzqh", "csxzqh.geodatabase");
        // 控件初始化
        findView();
        Intent intent = getIntent();
        String extra = intent.getStringExtra("fragment");

        if(extra!=null){
            login();
            LoginActivity.this.finish();
        }
    }

    // 屏幕旋转工具
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void findView() {
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        int radius = sp.getInt("radius", -1);
        if(radius==-1){
            sp.edit().putInt("radius", 1000).commit();
        }
        String isOffLine = sp.getString("isOffLine", null);
        if(isOffLine==null){
            sp.edit().putString("isOffLine", "false").commit();
        }
        String recordTime = sp.getString("recordTime", null);
        if(recordTime==null){
            sp.edit().putString("recordTime", "2016-11-09").commit();
        }
        int opId = sp.getInt("opId", -1);
        if(opId==-1){
            sp.edit().putInt("opId", 101747).commit();
        }
        TextView btnLogin = (TextView) findViewById(R.id.login_login);
        login_number_edit = (EditText) findViewById(R.id.login_number);
        login_password_edit = (EditText) findViewById(R.id.login_password);
        checkbox1 = (CheckBoxView) findViewById(R.id.checkbox1);
        login_password_edit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        login_password_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub

                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    login();
                    //      Toast.makeText(LoginActivity.this, "hahahaahaah", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

        // 判断记住密码多选框的状态
        if (sp.getBoolean("ISCHECK", false)) {
            // 设置默认是记录密码状态
            checkbox1.setChecked(true);
            login_number_edit.setText(sp.getString("USER_NAME", ""));
            login_password_edit.setText(sp.getString("PASSWORD", ""));
        }
        // 监听记住密码多选框按钮事件
//        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (checkbox1.isChecked()) {
//                    Log.i("test", "记住密码已被选中...");
//                    sp.edit().putBoolean("ISCHECK", true).commit();
//                } else {
//                    Log.i("test", "记住密码没有被选中");
//                    sp.edit().putBoolean("ISCHECK", false).commit();
//                }
//                checkbox1.toggle();
//            }
//        });

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbox1.isChecked()){
                sp.edit().putBoolean("ISCHECK",true).commit();
                }else{
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
                    checkbox1.toggle();
            }
        });
        server_setting = (TextView) findViewById(R.id.server_setting);
        server_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(LoginActivity.this,ServerSettingsActivity.class));
            }
        });
        if (CommValues.bsUrl==null||CommValues.bsUrl.equals("")){
            CommValues.bsUrl = "http://192.168.3.5:8005";
            sp.edit().putString("fwqurl", CommValues.bsUrl).commit();
        }
        loginCancer = (TextView) findViewById(R.id.login_cancer);
        loginCancer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initVariable();
                showProgress();
                validateOffline();
                hidePrigress();
            }
        });
        boolean isOk = CommValues.Inite(LoginActivity.this);
        if (isOk == false)
            try {
                finalize();
            } catch (Throwable e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        // 授权码
        try {
//			ArcGISRuntime.setClientId(CommValues.ClientID);
        } catch (Exception e) {
            // TODO: handle exception
        }// TODO: 2017/10/9 ceshi
        final Handler loginHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                int urlCode = msg.what;
                if (type != NONE&&urlCode != 200) {
                    startActivity(new Intent(LoginActivity.this,ServerSettingsActivity.class));

                    hidePrigress();
                }else{

                    login();
                }
            }
        };
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (RepeatClickUtils.isFastClick()) {
                    return ;
                }
                initVariable();
                showProgress();
                onLineLoginType = true;
                login();
                //--------------------------------
//				updateCountType(null);
				/*if (EMPTYSTR.equals(login_number_edit.getText().toString()
						.trim())) {
					Toast.makeText(LoginActivity.this, "用户名不能为空",
							Toast.LENGTH_LONG).show();
				} else if (EMPTYSTR.equals(login_password_edit.getText()
						.toString().trim())) {
					Toast.makeText(LoginActivity.this, "密码不能为空",
							Toast.LENGTH_LONG).show();
				} else {
					validateOffline();
				}
				hidePrigress(); */
                //android_admin
//				urlCode(url);

				/*new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String bs_url = sp.getString("fwqurl", "");
						Message msg = loginHandler.obtainMessage();
						msg.what = urlCode(bs_url);
						System.out.println(bs_url+"-------------"+msg.what+"---------------");
						loginHandler.sendMessage(msg);
					}
				}).start();*/
				/*Log.e("chakan", sp.getString("fwqurl", ""));
				if ("123".equals(login_number_edit.getText().toString().trim())&&"123".equals(login_password_edit.getText()
						.toString().trim())){

					startActivity(new Intent(LoginActivity.this,ServerSettingsActivity.class));

					 hidePrigress();

				}else
				{

					login();
				}*/
            }
        });
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 101) {
                    if (EMPTYSTR.equals(login_number_edit.getText().toString()
                            .trim())) {
                        Toast.makeText(LoginActivity.this, "用户名不能为空",
                                Toast.LENGTH_LONG).show();
//						login_number_edit.setError("用户名不能为空");
                    } else if (EMPTYSTR.equals(login_password_edit.getText()
                            .toString().trim())) {
                        Toast.makeText(LoginActivity.this, "密码不能为空",
                                Toast.LENGTH_LONG).show();
                    } else if (type == MOBILE || type == WIFI) {
                        validateOnline();
                    } else if (type == NONE) {
                        validateOffline();
                    }
                    hidePrigress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };

    private void login() {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Message msg = new Message();
                msg.what = 101;
                handler.handleMessage(msg);
                Looper.loop();
                super.run();
            }
        }.start();
    }

    ProgressDialog dialog = null;

    private void showProgress() {
        dialog = new ProgressDialog(LoginActivity.this);
        if (!dialog.isShowing())
            dialog = ProgressDialog.show(LoginActivity.this, "", "正在登陆...");
    }

    private void hidePrigress() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
    int loginUrlType = 0;
    // 在线登陆
    public static String userName = null;
    public static String passWord = null;
    private String validateOnline() {
        userName = login_number_edit.getText().toString().trim();
        passWord = login_password_edit.getText().toString().trim();
        //获取json字符串
        String result = null;
        try {
//			result = MyJSONUtil.getJSON(CommValues.bsUrl,"sc=SecurityController&op=LoginByUserName&LoginInfo={param1:'Sysadmin',param2:'123'}");http://192.168.1.16:8005/service.do?
            loginUrlType = sp.getInt("loginType", -1);
            String paramType = null;
            boolean isChineseChar = CommonHelper.isChineseChar(userName);
            if(!isChineseChar){
                paramType = "sc=DataSynchronous&op=LoginByUserName&LoginInfo=";
            }else{
                paramType = "sc=DataSynchronous&op=CheckLoginByGGInterface&LoginInfo=";
            }
            killOnLineLogin();
            long currentTime1 = System.currentTimeMillis()/1000;
            int code = urlCode(CommValues.bsUrl);
            long currentTime2 = System.currentTimeMillis()/1000;
            Log.e("currentTiem", (currentTime2-currentTime1)+"");
            if(!onLineLoginType){
                return EMPTYSTR;
            }
            onLineLoginType = false;
            if(code!=200){
                validateOffline();
                return EMPTYSTR;
            }
            Intent intent = new Intent(LoginActivity.this, SyncService.class);
            LoginActivity.this.startService(intent);
            updateMapInfo();
            syncBaseMapVersion();
            result = MyJSONUtil.getJSON(CommValues.interfaceUrl,paramType+"{param1:'"+userName+"',param2:'"+passWord+"'}");
//            Log.e("MyJSONUtil_LoginActivity",CommValues.interfaceUrl+paramType+"{param1:'"+userName+"',param2:'"+passWord+"'}");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            if(!onLineLoginType){
                return EMPTYSTR;
            }
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if(!onLineLoginType){
                return EMPTYSTR;
            }
            e.printStackTrace();
        }
        if(result==null){
            Toast.makeText(context, "对不起，因网络问题登陆失败", Toast.LENGTH_LONG).show();
            return EMPTYSTR;
        }else if("{}".equals(result.replaceAll(" ", ""))){
            Toast.makeText(context, "对不起，用户名或密码不正确", Toast.LENGTH_LONG).show();
            return EMPTYSTR;
        }
//		Log.e("result", ""+result);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(jsonObject!=null){
            JSONObject jsonSuccess = null;
            try {
                jsonSuccess = jsonObject.getJSONObject("success");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//			Log.e("jsonSuccess", ""+jsonSuccess);
            if(jsonSuccess==null){
                Toast.makeText(context, "对不起，用户名或密码不正确", Toast.LENGTH_LONG).show();
                return EMPTYSTR;
            }else{
//				updateUserInfo(userName,passWord);
                JSONArray systemright = null;
                JSONObject usermodel = null;
                JSONArray user_roles = null;
                JSONArray role_modules = null;
                JSONArray modules = null;
                try {
                    //获取user信息
                    usermodel = jsonSuccess.getJSONObject("usermodel");
                    userId = usermodel.getInt("userid");
                    //获取角色信息
                    user_roles = jsonSuccess.getJSONArray("user_roles");
                    role_modules = jsonSuccess.getJSONArray("role_modules");
                    modules = jsonSuccess.getJSONArray("modules");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block systemright
                    e.printStackTrace();
                }
                try {
                    //获取模块信息
                    systemright = jsonSuccess.getJSONArray("systemright");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                updateLoginInfo(usermodel, user_roles, role_modules, modules);
                getModuleList(systemright);
            }
        }
        return EMPTYSTR;
    }
    public static int userId = -1;
    // 离线登陆
    public String validateOffline() {
        userName = login_number_edit.getText().toString().trim();
        passWord = login_password_edit.getText().toString().trim();
        String localName = "";
        String localPswd = "";
        String enPassword = CommonHelper.md5(passWord) + "==";//EncryptHelper.GetMD5(passWord);
        enPassword = enPassword.replaceAll("_", "/").replaceAll("-", "+");
        ArrayList<UserInfo> loginInfoList = getLoginInfo(userName, enPassword);//.toUpperCase());

        if (loginInfoList.size() > 0) {
            userId = loginInfoList.get(0).getUserid();
            localName = userName;
            localPswd = passWord;
        }
        ArrayList<ModuleInfo> parentModuleInfoList = null;
        ArrayList<ModuleInfo> subModuleInfoList = null;
        if(userId!=-1){
            parentModuleInfoList = getModuleInfo(userId, "parent");
            subModuleInfoList = getModuleInfo(userId, "sub");
            if(parentModuleInfoList.size()==0||subModuleInfoList.size()==0){
                Toast.makeText(context, "对不起，您权限不足", Toast.LENGTH_LONG).show();
                return EMPTYSTR;
            }else{
                for(int i=0;i<parentModuleInfoList.size();i++){
                    String menuname = parentModuleInfoList.get(i).getMenuname();
                    String menuid = parentModuleInfoList.get(i).getMenuid();
                    String menucode = parentModuleInfoList.get(i).getMenucode();
                    PublicContainerParamUtil.listDataHeader.add(new String[]{menucode,menuname});
                    ArrayList<String[]> subNameList = new ArrayList<String[]>();
                    ArrayList<Integer> icoNameList = new ArrayList<Integer>();
                    for(int j=0;j<subModuleInfoList.size();j++){
                        ModuleInfo moduleInfo = subModuleInfoList.get(j);
                        if(menuid.equals(moduleInfo.getParentmoduleid())){
                            if (!moduleInfo.getMenuname().equals("综合统计")) {//------------------------------------------------------------------------------zdd

                                subNameList.add(new String[]{moduleInfo.getMenucode(),moduleInfo.getMenuname()});
                            }
                            if(moduleInfo.getMenuico()!=null){
                                String[] split = moduleInfo.getMenuico().split("\\.");
                                icoNameList.add(getResources().getIdentifier(split[0], "drawable", "gt.pocketplan.csgh"));
                            }
                            int intDataType = -1;
                            String datatype = moduleInfo.getDatatype();
                            if(datatype!=null&&!"".equals(datatype)){
                                intDataType = Integer.parseInt(datatype);
                                if(intDataType==2)
                                    intDataType = 0;
                            }
                            PublicContainerParamUtil.types.put(moduleInfo.getMenuname(), intDataType);
                            int intServiceType = -1;
                            String servicetype = moduleInfo.getServicetype();
                            if(servicetype!=null&&!"".equals(servicetype)){
                                intServiceType = Integer.parseInt(servicetype);
                            }
                            PublicContainerParamUtil.parameter.put(menuname+":"+moduleInfo.getMenuname(), intServiceType);
                        }
                    }

                    PublicContainerParamUtil.listDataChild.put(menuname, subNameList);
                    PublicContainerParamUtil.listpivc.put(i+"", icoNameList);
                }

            }
        }else{
            Toast.makeText(context, "对不起，用户名或密码不正确", Toast.LENGTH_LONG).show();
            return EMPTYSTR;
        }
        if (localName.toUpperCase().equals(userName.toUpperCase())
                && localPswd.toUpperCase().equals(passWord.toUpperCase())) {
            if (checkbox1.isChecked()) {
                // 记住用户名、密码、
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_NAME", localName);
                editor.putString("PASSWORD", localPswd);
                editor.commit();
            }
            Intent intent = new Intent();
			/*if (type == MOBILE || type == WIFI) {
				intent.putExtra("state", true);
			} else if (type == NONE) {
				intent.putExtra("state", false);
			}*/
            intent.putExtra("state", false);
            intent.putExtra("userName", localName);
			/*for(int i=0;i<PublicContainerParamUtil.listDataHeader.size();i++){
				PublicContainerParamUtil.listDataChild.put(listDataHeader.get(i),intent.getStringArrayListExtra(listDataHeader.get(i)));
				PublicContainerParamUtil.listpivc.put(i+"", intent.getIntegerArrayListExtra(listDataHeader.get(i)+"1"));

				intent.putStringArrayListExtra(PublicContainerParamUtil.listDataHeader.get(i), listDataChild.get(PublicContainerParamUtil.listDataHeader.get(i)));
				intent.putIntegerArrayListExtra(listDataHeader.get(i)+"1", listpivc.get(listDataHeader.get(i)));
			}*/
            startActivityForResult(
                    intent.setClass(LoginActivity.this, MainActivity.class), 1);
//			Toast.makeText(context, "离线登录成功！", Toast.LENGTH_SHORT).show();
//			this.finish();----------------------------------------2016.1.7

            hidePrigress();
            LoginActivity.this.finish();
        } else {
            Toast.makeText(LoginActivity.this, "用户名和密码输入不匹配，请重新输入！",
                    Toast.LENGTH_LONG).show();
        }
        return EMPTYSTR;
    }

    //获取当前用户信息
    public ArrayList<UserInfo> getLoginInfo(String name,String password){//LOGINNAME
        String userSql = "SELECT * FROM ONEMAP_SYS_USER u WHERE (trim(u.USERNAME)='"+name+"' OR trim(u.LOGINNAME)='"+name+"') AND trim(u.LOGINPASSWORD)='"+password+"';";
        UserInfoDao dao = new UserInfoDao(context);
        ArrayList<UserInfo> searchResult = dao.getSearchResult(userSql);
        return searchResult;
    }
    //获取模块
    public ArrayList<ModuleInfo> getModuleInfo(int userId,String type){
        String sqlPart = null;
        if("parent".equals(type)){
            sqlPart = "b.PARENTMODULEID IS NULL OR trim(b.PARENTMODULEID)=''";
        }else{
            sqlPart = "b.PARENTMODULEID IS NOT NULL AND trim(b.PARENTMODULEID)!=''";
        }

        String parentSql = "SELECT DISTINCT b.* FROM ONEMAP_SYS_ROLE_MODULE a,ONEMAP_SYS_MODULE b"+
                " WHERE a.ROLEID IN(SELECT r.ROLEID FROM ONEMAP_SYS_USER_ROLE r WHERE r.USERID='"+userId+"')"+
                " AND b.MODULETYPE='3' AND ("+sqlPart+") AND a.MODULEID=b.MODULEID ORDER BY b.SHOWORDER;";
        ModuleInfoDao dao = new ModuleInfoDao(context);
        ArrayList<ModuleInfo> searchResult = dao.getSearchResult(parentSql);
        return searchResult;
    }
    // 判断当前是否有网络
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // Wifi网络判断
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            Log.i("test", "已经进入if语句。。。");
//			Toast.makeText(context, "wifi网络已连接！", Toast.LENGTH_SHORT).show();
            return WIFI;
        }
        // 手机网络判断
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
//			Toast.makeText(context, "手机网络已连接！", Toast.LENGTH_SHORT).show();
            return MOBILE;
        }
//		Toast.makeText(context, "网络连接失败！采用离线登陆！", Toast.LENGTH_SHORT).show();
        return NONE;
    }
    //同步数据库文件
    private void copyDataBaseToPhone() {
        DataBaseUtil util = new DataBaseUtil(this);
        // 判断数据库是否存在
        File file = new File(Environment.getExternalStorageDirectory().getPath()
                + "/CSGHPocketPlan/database/cspocketPlan.db");

        if (file.exists()) {
            Log.i("tag", "The database is exist.");
			/*file.delete();
			try {
				util.copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}*/
        } else {// 不存在就把raw里的数据库写入手机
            try {
                util.copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
    //同步xml文件
    private void copyXmlToPhone() {
        DataBaseUtil util = new DataBaseUtil(this);
        // 判断xml文件是否存在
		/*File file = new File(context.getFilesDir().getAbsolutePath()
				+ "/xmlit/csghpocketplan.xml");*/
        File file = new File(Environment.getExternalStorageDirectory().getPath()
                + "/CSGHPocketPlan/csghpocketplan.xml");

        if (file.exists()) {
            Log.i("tag", "The xml is exist.");
            file.delete();
            try {
                util.copyXML();
            } catch (IOException e) {
                throw new Error("Error copying xml");
            }
        } else {// 不存在就把raw里的xml文件写入手机
            try {
                util.copyXML();
            } catch (IOException e) {
                throw new Error("Error copying xml");
            }
        }
    }

    private void copyMapToPhone(String path,String srcName,String fileName) {
        DataBaseUtil util = new DataBaseUtil(this);
        // 判断xml文件是否存在
		/*File file = new File(context.getFilesDir().getAbsolutePath()
				+ "/xmlit/csghpocketplan.xml");*/
        File file = new File(Environment.getExternalStorageDirectory().getPath()
                + "/CSGHPocketPlan/map/"+path+"/"+fileName);

        if (file.exists()) {
            Log.i("tag", "The GeoDataBase is exist.");
            file.delete();
            try {
                util.copyMap(path,srcName, fileName);
            } catch (IOException e) {
                throw new Error("Error copying GeoDataBase");
            }
        } else {// 不存在就把raw里的GeoDataBase文件写入手机
            try {
                util.copyMap(path,srcName, fileName);
            } catch (IOException e) {
                throw new Error("Error copying GeoDataBase");
            }
        }
    }
    //模拟接口数据
    @SuppressWarnings("unchecked")
    private void updateCountType(String tableName){
        tableName = "ONEMAP_STATISTIC_KIND";
        String str = "[{\"STATISTICKINDID\":\"caee4236-79d0-42a5-ae31-824e265e2a6b\",\"STATISTICKINDNAME\":\"各科室选址意见书案卷审批数量\",\"OPTYPEID\":\"1\",\"CREATETIME\":\"2015/5/21\",\"CREATOR\":\"\",\"ISUSED\":\"1\",\"ISSYSDEFINE\":\"1\",\"DEFAULTCHARTTYPEID\":\"1\",\"ISSHAREMOBILE\":\"\"},{\"STATISTICKINDID\":\"3eca5e3b-2b2a-4507-b3c4-fbfabdbf823d\",\"STATISTICKINDNAME\":\"各科室用地规划许可证案卷审批数量\",\"OPTYPEID\":\"1\",\"CREATETIME\":\"2015/5/21\",\"CREATOR\":\"\",\"ISUSED\":\"1\",\"ISSYSDEFINE\":\"1\",\"DEFAULTCHARTTYPEID\":\"1\",\"ISSHAREMOBILE\":\"\"},{\"STATISTICKINDID\":\"b842274d-b67e-40da-bffb-c61e5a0f4cad\",\"STATISTICKINDNAME\":\"各科室工程规划许可证案卷审批数量\",\"OPTYPEID\":\"1\",\"CREATETIME\":\"2015/5/21\",\"CREATOR\":\"\",\"ISUSED\":\"1\",\"ISSYSDEFINE\":\"1\",\"DEFAULTCHARTTYPEID\":\"1\",\"ISSHAREMOBILE\":\"\"},{\"STATISTICKINDID\":\"4e55f941-7b00-4f26-a063-df3c092095e0\",\"STATISTICKINDNAME\":\"各规划类型编制项目预算经费\",\"OPTYPEID\":\"2\",\"CREATETIME\":\"2015/5/21\",\"CREATOR\":\"\",\"ISUSED\":\"1\",\"ISSYSDEFINE\":\"1\",\"DEFAULTCHARTTYPEID\":\"1\",\"ISSHAREMOBILE\":\"\"},{\"STATISTICKINDID\":\"95c61987-d509-4896-92f2-b8a41b848a63\",\"STATISTICKINDNAME\":\"各规划类型编制项目拨付经费\",\"OPTYPEID\":\"2\",\"CREATETIME\":\"2015/5/21\",\"CREATOR\":\"\",\"ISUSED\":\"1\",\"ISSYSDEFINE\":\"1\",\"DEFAULTCHARTTYPEID\":\"1\",\"ISSHAREMOBILE\":\"\"}]";
        ArrayList<String> jsonFieldList = new ArrayList<String>();
        ArrayList<String> dbFieldList = new ArrayList<String>();
        DBExHelper db = new DBExHelper(this, CommValues.dbPath);
        String dbSql = "PRAGMA table_info("+tableName+");";
        db.open();
        Cursor query = db.Query(dbSql);
        while(query.moveToNext()){
            dbFieldList.add(query.getString(query.getColumnIndex("name")));
        }
        query.close();
        String clearSql = "delete from "+tableName+";";
        db.Excute(clearSql);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Iterator<String> keys = null;
        if(jsonArray != null){
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObj = (JSONObject)jsonArray.opt(i);
                if(keys==null){
                    keys = jsonObj.keys();
                    while(keys.hasNext()){
                        jsonFieldList.add(keys.next().toString());
                    }
                    dbFieldList.retainAll(jsonFieldList);
                }
                StringBuilder fieldBuilder = new StringBuilder();
                StringBuilder valueBuilder = new StringBuilder();
                for(int m=0;m<dbFieldList.size();m++){
                    try {
                        if(m==0){
                            fieldBuilder.append(dbFieldList.get(m));
                            valueBuilder.append("'"+jsonObj.getString(dbFieldList.get(m))+"'");
                        }else{
                            fieldBuilder.append(","+dbFieldList.get(m));
                            valueBuilder.append(",'"+jsonObj.getString(dbFieldList.get(m))+"'");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                String instrtSql = "insert INTO "+tableName+" ("+fieldBuilder.toString()+")values("+valueBuilder.toString()+")";
                db.Excute(instrtSql);
            }
        }
        db.close();
    }
    public static int urlCode(String url) {
        HttpGet httpget = new HttpGet(url);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = client.execute(httpget);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            return statusCode;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    //模块权限处理
    public void getModuleList(JSONArray systemright){
        if(systemright==null||systemright.length()<1){
            Toast.makeText(context, "对不起，您权限不足", Toast.LENGTH_LONG).show();
            return;
        }else{
            int subModuleNum = 0;
            JSONObject systemright_yd = null;
			/*listDataChild = new HashMap<String, ArrayList<String>>();
			listpivc = new HashMap<String, ArrayList<Integer>>();
			listDataHeader = new ArrayList<String>();*/
            try {
                for(int i=0;i<systemright.length();i++){
                    JSONObject jsonObject = systemright.getJSONObject(i);
                    if("移动端".equals(jsonObject.getString("sysname"))){
                        systemright_yd = jsonObject;
                        break;
                    }
                }
                if(systemright_yd==null){
                    Toast.makeText(context, "对不起，您权限不足", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    JSONArray menuitems = systemright_yd.getJSONArray("menuitems");
                    if(menuitems==null||menuitems.length()<1){
                        Toast.makeText(context, "对不起，您权限不足", Toast.LENGTH_LONG).show();
                        return;
                    }else{
                        for(int i=0;i<menuitems.length();i++){
                            JSONObject jsonObject = menuitems.getJSONObject(i);
                            String menuname = jsonObject.getString("menuname");
                            String menuid = jsonObject.getString("menuid");
                            String menucode = jsonObject.getString("menucode");
                            PublicContainerParamUtil.listDataHeader.add(new String[]{menucode,menuname});
//							listDataHeader.add(menuname);
                            JSONArray jsonArray = jsonObject.getJSONArray("submenus");
                            if(jsonArray!=null){
                                ArrayList<String[]> submenunames = new ArrayList<String[]>();
                                ArrayList<Integer> submenuicos = new ArrayList<Integer>();
                                subModuleNum = subModuleNum+jsonArray.length();
                                String test = "";
                                for(int j=0;j<jsonArray.length();j++){
                                    JSONObject submenu = jsonArray.getJSONObject(j);
                                    String submenucode = submenu.getString("menucode");
                                    String submenuname = submenu.getString("menuname");
                                    submenunames.add(new String[]{submenucode,submenuname});
                                    String menuico = submenu.getString("menuico");
                                    if(menuico!=null){
                                        String[] split = menuico.split("\\.");
                                        submenuicos.add(getResources().getIdentifier(split[0], "drawable", "gt.pocketplan.csgh"));
                                    }
                                    int intDataType = -1;
                                    String datatype = submenu.getString("datatype");
                                    if(datatype!=null&&!"".equals(datatype)){
                                        intDataType = Integer.parseInt(datatype);
                                        if(intDataType==2)
                                            intDataType = 0;
                                    }
                                    PublicContainerParamUtil.types.put(submenuname, intDataType);
                                    int intServiceType = -1;
                                    String servicetype = submenu.getString("servicetype");
                                    if(servicetype!=null&&!"".equals(servicetype)){
                                        intServiceType = Integer.parseInt(servicetype);
                                    }
                                    PublicContainerParamUtil.parameter.put(menuname+":"+submenuname, intServiceType);
//									test+=submenuname+":"+intServiceType+"+++";
                                }
//								System.out.println(test);
                                PublicContainerParamUtil.listDataChild.put(menuname, submenunames);
                                PublicContainerParamUtil.listpivc.put(i+"", submenuicos);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.putExtra("state", true);
            intent.putExtra("userName", "");//用户名暂时不处理
//			intent.putStringArrayListExtra("listDataHeader", listDataHeader);
			/*for(int i=0;i<PublicContainerParamUtil.listDataHeader.size();i++){
				subModuleNum = subModuleNum+PublicContainerParamUtil.listDataChild.get(PublicContainerParamUtil.listDataHeader.get(i)).size();
				intent.putStringArrayListExtra(listDataHeader.get(i), listDataChild.get(listDataHeader.get(i)));
				intent.putIntegerArrayListExtra(listDataHeader.get(i)+"1", listpivc.get(listDataHeader.get(i)));
			}*/
            if(subModuleNum==0){
                Toast.makeText(context, "对不起，您权限不足", Toast.LENGTH_LONG).show();
                return;
            }
            startActivityForResult(
                    intent.setClass(LoginActivity.this, MainActivity.class),
                    1);
//			Toast.makeText(context, "在线登录成功！", Toast.LENGTH_SHORT).show();
//			finish();// 关闭当前activity

            hidePrigress();
            LoginActivity.this.finish();
        }
    }
    //更新登录数据
    public void updateLoginInfo(JSONObject usermodel,JSONArray user_roles,JSONArray role_modules,JSONArray modules){
        if(usermodel!=null&&user_roles!=null&&role_modules!=null&&modules!=null&&userId!=-1){
            updateUserInfo(usermodel);
            updateUserAndRoleInfo(user_roles);
            updateRoleAndMoudleInfo(user_roles, role_modules);
            updateMoudleInfo(modules);
        }
    }
    //更新模块信息
    public void updateMoudleInfo(JSONArray modules){
        DBExHelper db = new DBExHelper(this, CommValues.dbPath);
        db.open();
        String sql = "delete from ONEMAP_SYS_MODULE";
        db.Excute(sql);
        db.close();
        CommonHelper.jsonDate2Table(this, "ONEMAP_SYS_MODULE", modules);
    }
    //更新角色模块关系
    public void updateRoleAndMoudleInfo(JSONArray user_roles,JSONArray role_modules){
        DBExHelper db = new DBExHelper(this, CommValues.dbPath);
        db.open();
        for(int i=0;i<user_roles.length();i++){
            String sql;
            try {
                sql = "delete from ONEMAP_SYS_ROLE_MODULE where ROLEID='"
                        + user_roles.getJSONObject(i).getString("roleid") + "'";
                db.Excute(sql);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        db.close();
        CommonHelper.jsonDate2Table(this, "ONEMAP_SYS_ROLE_MODULE", role_modules);
    }
    //更新用户角色关系
    public void updateUserAndRoleInfo(JSONArray user_roles){
        DBExHelper db = new DBExHelper(this, CommValues.dbPath);
        db.open();
        String sql = "delete from ONEMAP_SYS_USER_ROLE where USERID='"
                + userId + "'";
        db.Excute(sql);
        db.close();
        CommonHelper.jsonDate2Table(this, "ONEMAP_SYS_USER_ROLE", user_roles);
    }
    //更新用户信息
    public void updateUserInfo(JSONObject usermodel){
        if (checkbox1.isChecked()) {
            // 记住用户名、密码、
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("USER_NAME", userName);
            editor.putString("PASSWORD", passWord);
            editor.commit();
        }

        UserDao user = new UserDao(this);
        String enpassword = EncryptHelper.GetMD5(passWord).toUpperCase();
        String sql = "select * from ONEMAP_SYS_USER where USERID='"
                + userId + "'";
        DBExHelper db = new DBExHelper(this, CommValues.dbPath);
        db.open();
        Cursor query = db.Query(sql);
        String updatesql = null;
        if(query.moveToFirst()){
            try {
                updatesql = "update ONEMAP_SYS_USER set USERNAME='" + usermodel.getString("username")+"',LOGINNAME='" + usermodel.getString("loginname")+"',LOGINPASSWORD='" + usermodel.getString("loginpassword")
                        +"',CREATETIME='" + usermodel.getString("createtime")+"',CANCELLATIONTIME='" + usermodel.getString("cancellationtime")+"',ISUSED='" + usermodel.getInt("isused")
                        +"',USERCODE='" + usermodel.getString("usercode")+"',PASSWORD='" + enpassword
                        + "' where USERID='" + userId + "';";
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            try {
                updatesql = "insert into ONEMAP_SYS_USER (USERNAME,USERID,LOGINNAME,LOGINPASSWORD,CREATETIME,CANCELLATIONTIME,ISUSED,USERCODE,PASSWORD) values ('"
                        +usermodel.getString("username") + "' ,'" + usermodel.getInt("userid") + "' ,'" + usermodel.getString("loginname") + "' ,'"
                        + usermodel.getString("loginpassword") + "' ,'" + usermodel.getString("createtime") + "' ,'" + usermodel.getString("cancellationtime") + "' ,'"
                        + usermodel.getInt("isused") + "' ,'" + usermodel.getString("usercode") + "' ,'" + enpassword + "');";
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        query.close();

        try {
            user.ExcuteSql(updatesql);
        } catch (Exception e) {
            // TODO: handle exception
        }
        db.close();
    }
    public void updateUserInfo(){
        if (checkbox1.isChecked()) {
            // 记住用户名、密码、
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("USER_NAME", userName);
            editor.putString("PASSWORD", passWord);
            editor.commit();
        }

        // 更新离线用户记录
        UserDao user = new UserDao(this);
//		String enpassword = EncryptHelper.GetMD5(passWord).toUpperCase();
        String enpassword = CommonHelper.md5(passWord) + "==";//EncryptHelper.GetMD5(passWord);
        enpassword = enpassword.replaceAll("_", "/").replaceAll("-", "+");
        String sql = "select * from ONEMAP_SYS_USER where LOGINNAME='"
                + userName + "' OR USERNAME='"+ userName + "'";
        ArrayList<User> list = user.getSearchReslut(sql);
        String updatesql = "";
        if (list.size() == 0) {
            updatesql = "insert ONEMAP_SYS_USER (LOGINNAME,LOGINPASSWORD) values ('"
                    + enpassword + "' ,'" + userName + "');";
        } else {
            updatesql = "update ONEMAP_SYS_USER set LOGINPASSWORD='" + enpassword
                    + "' where LOGINNAME='"
                    + userName + "' OR USERNAME='"+ userName + "'";
        }
        try {
            user.ExcuteSql(updatesql);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void initVariable(){
        PublicContainerParamUtil.listDataHeader = new ArrayList<String[]>();
        PublicContainerParamUtil.listDataChild = new HashMap<String, List<String[]>>();
        PublicContainerParamUtil.listpivc = new HashMap<String, List<Integer>>();
        PublicContainerParamUtil.parameter = new HashMap<String, Integer>();
        PublicContainerParamUtil.types = new HashMap<String, Integer>();
    }

    //同步底图历史版本
    private void syncBaseMapVersion(){
        String result = null;
        try {
            result = MyJSONUtil.getJSON(CommValues.interfaceUrl, "sc=DataSynchronous&op=GetTileVersion");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (result!=null) {
            JSONArray array = getJsonData(result);
            if(array!=null){
                clearTable("ONEMAP_MAP_TILEVERSION");
                CommonHelper.jsonDate2Table(CangShuPlanning.getContext(),"ONEMAP_MAP_TILEVERSION", array);
            }
        }
    }
    //同步地图配置信息
    private void updateMapInfo(){
        String result = null;
        try {
            result = MyJSONUtil.getJSON(CommValues.interfaceUrl,"sc=DataSynchronous&op=GetMapResourceData");
            Log.e("MyJSONUtil_MapInfoTask",CommValues.interfaceUrl+"sc=DataSynchronous&op=GetMapResourceData");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(result==null){
            return;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(jsonObject!=null){
            JSONArray mapCatalogs = null;
            JSONArray mapServices = null;
            JSONArray layerFiels = null;
            JSONArray layers = null;
            JSONArray topics = null;
            JSONArray roleLayers = null;
            JSONArray moduleLayers = null;
            try {
                mapCatalogs = jsonObject.getJSONArray("mapCatalogs");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                mapServices = jsonObject.getJSONArray("mapServices");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                layerFiels = jsonObject.getJSONArray("layerFiels");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                layers = jsonObject.getJSONArray("layers");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                topics = jsonObject.getJSONArray("topics");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                roleLayers = jsonObject.getJSONArray("roleLayers");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                moduleLayers = jsonObject.getJSONArray("moduleLayers");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(mapCatalogs!=null){
                clearTable("ONEMAP_MAP_CATALOG");
                CommonHelper.jsonDate2Table(LoginActivity.this, "ONEMAP_MAP_CATALOG", mapCatalogs);
            }
            if(mapServices!=null){
                clearTable("ONEMAP_MAP_MAPSERVICE");
                CommonHelper.jsonDate2Table(LoginActivity.this, "ONEMAP_MAP_MAPSERVICE", mapServices);
            }
            if(layerFiels!=null){
                clearTable("ONEMAP_MAP_LAYERFIELDS");
                CommonHelper.jsonDate2Table(LoginActivity.this, "ONEMAP_MAP_LAYERFIELDS", layerFiels);
            }
            if(layers!=null){
                clearTable("ONEMAP_MAP_LAYERS");
                CommonHelper.jsonDate2Table(LoginActivity.this, "ONEMAP_MAP_LAYERS", layers);
            }
            if(topics!=null){
                clearTable("ONEMAP_MAP_TOPIC");
                CommonHelper.jsonDate2Table(LoginActivity.this, "ONEMAP_MAP_TOPIC", topics);
            }
            if(roleLayers!=null){
                clearTable("ONEMAP_MAP_ROLELAYERS");
                CommonHelper.jsonDate2Table(LoginActivity.this, "ONEMAP_MAP_ROLELAYERS", roleLayers);
            }
            if(moduleLayers!=null){
                clearTable("ONEMAP_MODULE_LAYER");
                CommonHelper.jsonDate2Table(LoginActivity.this, "ONEMAP_MODULE_LAYER", moduleLayers);
            }
        }
    }
    //获取json数据
    private JSONArray getJsonData(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = jsonObject.getJSONArray("data");
            if(array==null){
                return null;
            }
            return array;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    //清除表数据
    private void clearTable(String tabName){
        DBExHelper db = new DBExHelper(CangShuPlanning.getContext(),CommValues.dbPath);
        db.open();
        String clearSql = "DELETE FROM "+tabName;
        db.Excute(clearSql);
        db.close();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        System.out.println("-------------------------LoginActivity.onDestroy----------------------");
    }
    private boolean onLineLoginType = true;
    private boolean isThreadOn = true;
    private final Handler killOnLineLoginHandler = new Handler(){
        public void handleMessage(Message msg) {
            validateOffline();
            hidePrigress();
        }
    };
    //强制结束在线登陆
    public void killOnLineLogin(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(onLineLoginType&&isThreadOn){
                    onLineLoginType = false;
                    Message msg = killOnLineLoginHandler.obtainMessage();
                    killOnLineLoginHandler.sendMessage(msg);
                }
            }
        }).start();
    }
}

