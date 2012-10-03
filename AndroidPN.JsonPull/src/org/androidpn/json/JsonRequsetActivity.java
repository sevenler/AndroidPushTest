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
public class JsonRequsetActivity extends Activity {
	
	private Button okButton;
	private TextView status;
	private boolean isStart = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("JsonRequsetActivity", "onCreate()...");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_activity);
		
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(R.string.pull_json_service_activity);
		
		// Settings
		okButton = (Button) findViewById(R.id.btn_opreate);
		status = (TextView) findViewById(R.id.status);
		opreateService(true);
		
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//ServiceManager.viewNotificationSettings(JsonRequsetActivity.this);
				opreateService(!isStart);
			}
		});
	}
	
	private void opreateService(boolean isOpen){
		if(isOpen){
			Intent intent = new Intent(this,JsonRequsetService.class);
			this.startService(intent);
			okButton.setText(R.string.close_pull_service);
			status.setText(R.string.alert_pull_open);
		}else {
			Intent intent = new Intent(this,JsonRequsetService.class);
			this.stopService(intent);
			okButton.setText(R.string.open_pull_service);
			status.setText(R.string.alert_pull_close);
		}
		isStart = isOpen;
	}

}