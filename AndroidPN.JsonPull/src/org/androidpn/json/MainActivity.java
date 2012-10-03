package org.androidpn.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.androidpn.json.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText ipaddress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		Button connect = (Button) findViewById(R.id.connect);
		ipaddress = (EditText) findViewById(R.id.ipaddress);

		connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String ip = ipaddress.getText().toString();
				if (MainActivity.isboolIp(ip)) {
					Intent intent = new Intent(MainActivity.this,
							DemoAppActivity.class);
					intent.putExtra("ip", ip);
					startActivity(intent);
				} else {
					Toast.makeText(MainActivity.this,
							R.string.alert_not_ip_address, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		
		
		Button jsonReq = (Button) findViewById(R.id.jsonReq);
		jsonReq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						JsonRequsetActivity.class);
				startActivity(intent);
			}
		});
	}

	public static boolean isboolIp(String ipAddress) {
		String ip = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}
}
