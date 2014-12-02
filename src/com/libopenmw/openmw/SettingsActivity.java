package com.libopenmw.openmw;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private SharedPreferences Settings;
	private EditText configsText;
	private EditText dataText;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		configsText = (EditText) findViewById(R.id.configsPath);
		dataText = (EditText) findViewById(R.id.editText1);
		Settings = getSharedPreferences(Constants.APP_PREFERENCES,
				Context.MODE_MULTI_PROCESS);

		MainActivity.configsPath = Settings.getString(Constants.CONFIGS_PATH, "");
		MainActivity.dataPath = Settings.getString(Constants.DATA_PATH, "");
		configsText.setText(MainActivity.configsPath);
		dataText.setText(MainActivity.dataPath);

		configsText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				saveToSharedPreferences(Constants.CONFIGS_PATH, s.toString());

			}
		});
		
		dataText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				saveToSharedPreferences(Constants.DATA_PATH, s.toString());

			}
		});
		

	}

	public void saveToSharedPreferences(String value, String buffer) {
		Editor editor = Settings.edit();
		editor.putString(value, buffer);
		editor.apply();
	}

}
