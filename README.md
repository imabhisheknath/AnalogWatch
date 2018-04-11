# AnalogWatch
This library use for creating analog clock from drawable and
secound hand also include in it. 
--------------
#uses

//in xml

```xml


<com.example.administrator.watchapp.CustomAnalogClock
      android:id="@+id/analog_clock"
      android:layout_width="match_parent"
      android:layout_height="match_parent"/>



```


//in java

```java

//add to build app
compile 'com.github.imabhisheknath:AnalogWatch:v1.0-sheild'

//in main activity

CustomAnalogClock customAnalogClock = (CustomAnalogClock) findViewById(R.id.analog_clock);

//set up auto update
customAnalogClock.setAutoUpdate(true);

 //set up drawable for each watch body
                Drawable dial = getDrawable(R.drawable.dial);
                Drawable hour = getDrawable(R.drawable.hour);
                Drawable min = getDrawable(R.drawable.minute);
                Drawable sec = getDrawable(R.drawable.second);
                
                //execute it
                customAnalogClock.setWatchBody(MainActivity.this, dial, hour, min, sec);

```
