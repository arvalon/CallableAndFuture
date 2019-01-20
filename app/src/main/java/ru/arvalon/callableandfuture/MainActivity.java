package ru.arvalon.callableandfuture;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Runnable

        Runnable r = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                Logs.info(this,"Runnable Thread SystemTime: "+System.currentTimeMillis());
            }
        };

        Logs.info(this,"System Time: "+System.currentTimeMillis());

        Thread t = new Thread(r);
        t.start();

        // Callable

        Callable<Integer> c = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                SystemClock.sleep(2000);
                Logs.info(this,"Callable SystemTime: "+System.currentTimeMillis());
                return 1;
            }
        };

        ExecutorService tpl = Executors.newSingleThreadExecutor();

        Future<Integer> f =  tpl.submit(c);

        try {
            Logs.info(this,"f.get: "+f.get()+", MainThread SystemTime: "+System.currentTimeMillis());

        } catch (ExecutionException | InterruptedException e) {
            Logs.error(this, e.getMessage(),e);
        }

        // FutureTask & Callable

        Handler handler = new Handler();
        handler.postDelayed(r,3000);
        Logs.info(this," MainThread SystemTime: "+System.currentTimeMillis());

        Callable<Integer> c2 = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                SystemClock.sleep(4000);
                Logs.info(this,"Callable c2 SystemTime: "+System.currentTimeMillis());
                return 1;
            }
        };

        FutureTask<Integer> futureTask = new FutureTask<>(c2);

        handler.post(futureTask);

        // FutureTask & Runnable

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                Logs.info(this,"Runnable r2 SystemTime: "+System.currentTimeMillis());
            }
        };

        FutureTask<Integer> futureTask2 = new FutureTask<>(r2, Integer.valueOf(333));

        tpl.submit(futureTask2);

        Logs.info(this,"END");

    }
}