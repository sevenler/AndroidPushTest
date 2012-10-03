package org.androidpn.demo;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class JsonRequsetService extends Service{
	
	public static final String HOST_SERVICE = "http://service.androidesk.com";
	public static final String PUSH_CHECK = HOST_SERVICE
			+ "/messagedeliver/messagerequest";
	private static final String LOGTAG = "JsonRequsetService";
	
	private JSONObject pushJson = null;
	
	private boolean start = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(LOGTAG, "onCreate()...");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		start = true;
		super.onStart(intent, startId);
		Log.d(LOGTAG, "onStart()...");
		new Thread(){
			@Override
			public void run() {
				super.run();
				int i = 0;
				while(start){
					i++;
					Log.i(LOGTAG, "getPushInfo thread runing "+ i +" times");
					getPushInfo();
					try {
						Thread.sleep(1000 * 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Log.i(LOGTAG, "getPushInfo thread stop");
			}
		}.start();
	}
	
	    // 获取该推送的信息
		private void getPushInfo() {
			try {
				String result = NetworkManager.getInstance().httpGET(this,PUSH_CHECK);
				Log.i(LOGTAG, "httpGet : "+ PUSH_CHECK);
				Log.i(LOGTAG, "result : "+ result);
				pushJson = new JSONObject(result);
				JSONArray jsonArray = pushJson.getJSONArray("resp");
				String respObject = jsonArray.getString(0);
				JSONObject resp = new JSONObject(respObject);

				resp.getString("id");
				resp.getString("title");
				resp.getString("content");
				resp.getString("link");
				resp.getString("datefrom");
				resp.getString("dateto");
				resp.getString("imgid"); 
				
			} catch (Exception e) {//这里需要扑捉解析异常，网络异常，Io异常等等.....
				e.printStackTrace();
			}
		}

		@Override
		public void onDestroy() {
			Log.d(LOGTAG, "onDestroy()...");
			start = false;
			super.onDestroy();
		}
		
}
