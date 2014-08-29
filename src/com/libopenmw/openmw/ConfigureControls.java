package com.libopenmw.openmw;

import org.libsdl.app.SDLActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class ConfigureControls extends Activity {

	public Context context;
	public static boolean contols = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.controls);

		context = this;
		MultiTouchListener touchListener=new MultiTouchListener(this);
		final Controls touch = (Controls) findViewById(R.id.controls1);
		touch.setOnTouchListener(touchListener);
		ImageButton buttonRun = (ImageButton) findViewById(R.id.buttonrun);
		buttonRun.setOnTouchListener(touchListener);
		
		
	}

}
