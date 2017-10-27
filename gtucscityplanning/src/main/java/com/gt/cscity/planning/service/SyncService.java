package com.gt.cscity.planning.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.IBinder;
import android.util.Log;

import com.gt.cscity.planning.utils.CangShuPlanning;
import com.gt.cscity.planning.utils.CommValues;
import com.gt.cscity.planning.utils.CommonHelper;
import com.gt.cscity.planning.utils.DBExHelper;
import com.gt.cscity.planning.utils.MyJSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class SyncService extends Service {
    private int type = -1;
    public final static int NONE = 0;
    public final static int WIFI = 1;
    // Wi-Fi
    public final static int MOBILE = 2;

    private String bz_result;
    private String sp_result;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        type = getNetworkState(CangShuPlanning.getContext());
        if (type!=NONE) {
			/*AutomaticUpdateLoginInfo updateLoginInfo = new AutomaticUpdateLoginInfo(CangShuPlanning.getContext());
			updateLoginInfo.updateLoginInfo();*/
            new Thread(new Runnable() {
                public void run() {
                    String recordTime = getSyncCondition(1, "2016-11-09");
//					String recordTime = LoginActivity.sp.getString("recordTime", "2016-11-09");
                    try {
                        bz_result = MyJSONUtil.getJSON(CommValues.interfaceUrl, "sc=DataSynchronous&op=GetResultProjectData_bytime&startday='"+recordTime+"'");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (bz_result!=null) {
                        try {
                            clearBZ(recordTime);
                            JSONObject jsonObject =new JSONObject(bz_result);
                            JSONArray baseinfo = jsonObject.getJSONArray("baseInfo");
                            JSONArray progress = jsonObject.getJSONArray("progress");
                            JSONArray node = jsonObject.getJSONArray("node");
                            JSONArray pay = jsonObject.getJSONArray("pay");
                            JSONArray send = jsonObject.getJSONArray("send");


                            if (baseinfo!=null&&baseinfo.length()>0) {
                                CommonHelper.jsonDate2Table(CangShuPlanning.getContext(),"ONEMAP_PLANNINGDESIGN_PROJECT", baseinfo);
                            }
                            if (progress!=null&&progress.length()>0) {
                                CommonHelper.jsonDate2Table(CangShuPlanning.getContext(),"ONEMAP_PLANNINGDESGIN_PROGRESS", progress);
                            }
                            if (node!=null&&node.length()>0) {
                                CommonHelper.jsonDate2Table(CangShuPlanning.getContext(),"ONEMAP_PLANNINGDESIGN_NODE", node);
                            }
                            if (pay!=null&&pay.length()>0) {
                                CommonHelper.jsonDate2Table(CangShuPlanning.getContext(),"ONEMAP_PLANNINGDESIGN_PAY", pay);
                            }
                            if (send!=null&&send.length()>0) {
                                CommonHelper.jsonDate2Table(CangShuPlanning.getContext(),"ONEMAP_PLANNINGDESIGN_FUNDING", send);
                            }

                            String maxProjectDate = CommonHelper.getMaxProjectDate(CangShuPlanning.getContext());
                            updateSyncCondition(1, maxProjectDate);
//						   LoginActivity.sp.edit().putString("recordTime", maxProjectDate).commit();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    int opId = Integer.parseInt(getSyncCondition(2, "101747"));
                    try {
                        sp_result = MyJSONUtil.getJSON(CommValues.interfaceUrl, "sc=DataSynchronous&op=GetApprovalProjectData_new&startopid="+opId);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block clearSP
                        e.printStackTrace();
                    }
                    JSONArray array = new JSONArray();
                    if(sp_result!=null){
                        clearSP(opId);
                        JSONArray jsonData = getJsonData(sp_result);
                        if(jsonData!=null&&jsonData.length()>0){
                            getData(jsonData,array);
                            CommonHelper.jsonDate2Table(CangShuPlanning.getContext(),"TBOP_01T", jsonData);
                            if(array.length()>0){
//								clearTable("PROJECT_PUBLISH");
                                CommonHelper.jsonDate2Table(CangShuPlanning.getContext(),"PROJECT_PUBLISH", array);
                            }
                            int maxOpId = CommonHelper.getMaxOpId(CangShuPlanning.getContext());
                            updateSyncCondition(2, maxOpId+"");
//							LoginActivity.sp.edit().putInt("opId", maxOpId).commit();
                        }
                    }
                    stopSelf();
                }
            }).start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.e("onStartCommand","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.e("onDestroy","onDestroy");
    }

    private void clearBZ(String time){
        DBExHelper db = new DBExHelper(CangShuPlanning.getContext(),CommValues.dbPath);
        db.open();
        clearBZ(db, time, "ONEMAP_PLANNINGDESGIN_PROGRESS");
        clearBZ(db, time, "ONEMAP_PLANNINGDESIGN_PAY");
        clearBZ(db, time, "ONEMAP_PLANNINGDESIGN_FUNDING");
        String deleteProject = "DELETE FROM ONEMAP_PLANNINGDESIGN_PROJECT WHERE RECORDTIME>='"+time+"' OR trim(RECORDTIME)='' OR RECORDTIME IS NULL";
        db.Excute(deleteProject);
        String deleteNode = "DELETE FROM ONEMAP_PLANNINGDESIGN_NODE";
        db.Excute(deleteNode);
        db.close();
    }

    private void clearBZ(DBExHelper db,String time,String tabName){
        String condition = "(SELECT t.PLANNINGDESIGNID FROM ONEMAP_PLANNINGDESIGN_PROJECT t WHERE t.RECORDTIME>='"+time+"' OR trim(RECORDTIME)='' OR RECORDTIME IS NULL)";

        String sql = "DELETE FROM "+tabName+" WHERE PLANNINGDESIGNID IN "+condition;

        db.Excute(sql);
    }
    private void clearSP(int opId){
        DBExHelper db = new DBExHelper(CangShuPlanning.getContext(),CommValues.dbPath);
        db.open();
        String sql = "DELETE FROM TBOP_01T WHERE OPID>"+opId;
        db.Excute(sql);
        String opnumgatherSql = "SELECT t.OPNUMGATHER FROM TBOP_01T t WHERE OPID>"+opId;
        Cursor query = db.Query(opnumgatherSql);
        while(query.moveToNext()){
            String opnumgather = query.getString(0);
            String deletePublishSql = "DELETE FROM PROJECT_PUBLISH WHERE TITLE LIKE '%"+opnumgather+"%'";
            db.Excute(deletePublishSql);
        }
        query.close();
        db.close();
    }

    private JSONArray getJsonData(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            String success = jsonObject.getString("success");
            if(success==null||!"同步获取审批记录成功".equals(success)){
                return null;
            }
            JSONArray contentJsonArray = jsonObject.getJSONArray("items");
            if(contentJsonArray==null){
                return null;
            }
            return contentJsonArray;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void getData(JSONArray jsonArray,JSONArray array){
        for(int i=0;i<jsonArray.length();i++){
            JSONObject obj = (JSONObject) jsonArray.opt(i);
            JSONObject jsonObject = null;
            try {
                String prepublish = obj.getString("prepublish");
                if(!prepublish.trim().equals("")){
                    jsonObject = new JSONObject(prepublish);
                    jsonObject.put("istype", 0);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(jsonObject!=null){
                array.put(jsonObject);
            }
        }
    }

    private void clearTable(String tabName){
        DBExHelper db = new DBExHelper(CangShuPlanning.getContext(),CommValues.dbPath);
        db.open();
        String clearSql = "DELETE FROM "+tabName;
        db.Excute(clearSql);
        db.close();
    }
    public int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return WIFI;
        }
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return MOBILE;
        }
        return NONE;
    }
    //获取更新参数
    private String getSyncCondition(int id,String defaultCondition){
        String condition = defaultCondition;
        DBExHelper db = new DBExHelper(CangShuPlanning.getContext(),CommValues.dbPath);
        db.open();
        String sql = "select t.[CONDITION_VALUE] from TBSYNC t where t.[ID] = "+id;
        Cursor query = db.Query(sql);
        if(query.moveToFirst()){
            condition = query.getString(0);
        }
        query.close();
        db.close();
        return condition;
    }
    private void updateSyncCondition(int id,String condition){
        DBExHelper db = new DBExHelper(CangShuPlanning.getContext(),CommValues.dbPath);
        db.open();
        String sql = "update TBSYNC set CONDITION_VALUE='"+condition+"' where ID="+id;
        db.Excute(sql);
        db.close();
    }
}
