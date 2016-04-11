package com.ndtv.ndtvdoc.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ndtv.ndtvdoc.R;
import com.ndtv.ndtvdoc.utils.AppConstants;
import com.ndtv.ndtvdoc.utils.NDTVSharedPreferencesManager;

import java.util.HashMap;

public class UserEntryActivity extends Activity {

    EditText name;
    Button signup;
    private SliderLayout mSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NDTVSharedPreferencesManager.getBoolean(this, AppConstants.APPOPENED) == true) {
            //If Farmer registered go to Farmer home page
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            this.finish();
        }

        setContentView(R.layout.activity_user_entry);
        name = (EditText) findViewById(R.id.signIn_btn);
        signup = (Button) findViewById(R.id.signUp_btn);
        mSlider = (SliderLayout) findViewById(R.id.homeSlider);

        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.a);
        file_maps.put("2", R.drawable.c);
        file_maps.put("3", R.drawable.d);
        file_maps.put("3", R.drawable.e);
        //file_maps.put("", R.drawable.game_of_thrones);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("", name);

            mSlider.addSlider(textSliderView);
        }
        mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(7000);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("")) {
                    NDTVSharedPreferencesManager.getPreferencesEditor(UserEntryActivity.this).putBoolean(AppConstants.APPOPENED, true).commit();
                    NDTVSharedPreferencesManager.getPreferencesEditor(UserEntryActivity.this).putString(AppConstants.USER_NAME, name.getText().toString()).commit();
                    startActivity(new Intent(UserEntryActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(UserEntryActivity.this, "Please enter your name", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
