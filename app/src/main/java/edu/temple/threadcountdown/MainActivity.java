package edu.temple.threadcountdown;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    TextView countdown;
    TextView prompt;
    EditText entry;
    Button start;
    Integer state;
    Integer toCount;
    Thread thread;
    Object stateWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdown = (TextView) findViewById(R.id.textView);
        prompt = (TextView) findViewById(R.id.textView2);
        entry = (EditText) findViewById(R.id.editText);
        start = (Button) findViewById(R.id.button);
        state = 0;
        toCount = 0;

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (state){
                    case 0:
                        toCount = Integer.parseInt(entry.getText().toString());
                        if(toCount <= 0)
                            break;

                        thread = new Thread(){

                            @Override
                            public void run(){
                                for(int i=toCount;i>=0;i--){

                                    while (state != 1) {
                                        try {
                                            Thread.sleep(10);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Message msg = timerHandler.obtainMessage();
                                    msg.what = i;
                                    timerHandler.sendMessage(msg);

                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                        };


                        start.setText(getResources().getString(R.string.pause));
                        state = 1;

                        thread.start();
                        break;
                    case 1:
                        start.setText(getResources().getString(R.string.resume));
                        state = 2;
                        break;
                    case 2:
                        start.setText(getResources().getString(R.string.pause));
                        state = 1;
                        break;
                }
            }
        });

    }

    // Handler that will received and process messages in the UI thread
    Handler timerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            // Retrieve the message and update the textview
            countdown.setText(String.valueOf(message.what));

            if(message.what == 0){
                state = 0;
                start.setText(getResources().getString(R.string.start));
                entry.setText(getResources().getString(R.string.number));

                Toast.makeText(getBaseContext(),getResources().getString(R.string.countdown_complete),Toast.LENGTH_SHORT).show();

            }

            return false;
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
