package pl.edu.agh.iaeste.praktykator;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(4000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent startMain = new Intent("pl.edu.agh.iaeste.praktykator.MENU");
                    startActivity(startMain);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
