package com.example.administrator.watchapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends Activity {
    CustomAnalogClock customAnalogClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        customAnalogClock = (CustomAnalogClock) findViewById(R.id.analog_clock);

        customAnalogClock.setAutoUpdate(true);


        Button btn = (Button) findViewById(R.id.chang);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Drawable dial = getDrawable(R.drawable.dial);
                Drawable hour = getDrawable(R.drawable.hour);
                Drawable min = getDrawable(R.drawable.minute);
                Drawable sec = getDrawable(R.drawable.second);
*/

               /* customAnalogClock.setWatchBody(MainActivity.this, dial, hour, min, sec);
*/
            }
        });




    }
}
