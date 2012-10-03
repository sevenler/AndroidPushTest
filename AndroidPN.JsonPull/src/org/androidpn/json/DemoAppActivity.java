/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.json;

import org.androidpn.client.ServiceManager;
import org.androidpn.json.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is an androidpn client demo application.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class DemoAppActivity extends Activity {
	
	private ServiceManager serviceManager ;
	private Button okButton;
	private TextView status;
	private String apiKey = "1234567890";
	private String xmppHost = "192.168.0.102";
	private String xmppPort = "5222";
	private boolean isStart = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("DemoAppActivity", "onCreate()...");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_activity);
		
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(R.string.push_socket_service_activity);
		
		Intent intent = getIntent();
		xmppHost = intent.getStringExtra("ip");

		// Settings
		okButton = (Button) findViewById(R.id.btn_opreate);
		status = (TextView) findViewById(R.id.status);
		
		opreateService(true);
		
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//ServiceManager.viewNotificationSettings(DemoAppActivity.this);
				opreateService(!isStart);
			}
		});
	}
	
	private void opreateService(boolean isOpen){
		if(isOpen){
			// Start the service
			serviceManager = new ServiceManager(this, apiKey,
					xmppHost, xmppPort);
			serviceManager.setNotificationIcon(R.drawable.notification);
			serviceManager.startService();
			okButton.setText(R.string.close_push_service);
			status.setText(R.string.alert_push_open);
		}else{
			serviceManager.stopService();
			okButton.setText(R.string.open_push_service);
			status.setText(R.string.alert_push_close);
		}
		isStart = isOpen;
	}

}