package com.example.acer.mytimer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityGreeting extends AppCompatActivity {
    /*
        private static final int MILLIS_PER_SECOND = 1000;
        private static final int SECONDS_TO_COUNTDOWN = 30;
        private TextView     countdownDisplay;
        private CountDownTimer timer;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_countdown);

            countdownDisplay = (TextView) findViewById(R.id.time_display_box);
            Button startButton = (Button) findViewById(R.id.startbutton);
            startButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    try {
                        showTimer(SECONDS_TO_COUNTDOWN * MILLIS_PER_SECOND);
                    } catch (NumberFormatException e) {
                        // method ignores invalid (non-integer) input and waits
                        // for something it can use
                    }
                }
            });
        }*/

    // время работы первого активити - 2 секунды
    long millis_count = 2000;
    int millis_in_interval = 7;

    static CountDownTimer my_timer;

    /*
    onStart() – вызывается перед тем, как Activity будет видно пользователю
    onStop() – вызывается когда Activity становится не видно пользователю
    onDestroy() – вызывается перед тем, как Activity будет уничтожено*/

    // onCreate() – вызывается при первом создании Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // создаем greeting activity и подключаем соответствующий xml файл
        setContentView(R.layout.greeting);
    }

    // onResume() – вызывается перед тем как будет доступно для активности пользователя (взаимодействие)
    @Override
    public void onResume() {
        super.onResume();
        // Ожидание реализуем через CountDownTimer.
        //timer = new CountDownTimer(countdownMillis, MILLIS_PER_SECOND)
        my_timer = new CountDownTimer(millis_count, millis_in_interval) {
            // вызывается для каждого кванта времени таймера. Он вызывает методы, которые добавляются с помощью Tick.
            public void onTick(long millis_current) {
                millis_count = millis_current;
            }
            // завершение работы активити
            public void onFinish() {
                // переключаемся на второе активити
                Intent intent = new Intent(ActivityGreeting.this, ActivityTimer.class);
                startActivity(intent);
                // завершаемся
                finish();
            }
        }.start();
    }

    // onPause() – вызывается перед тем, как будет показано другое Activity
    @Override
    public void onPause() {
        super.onPause();
        my_timer.cancel();
    }

    /*
    Когда работа Activity приостанавливается(onPause или onStop), она остается в памяти и хранит все свои объекты и их значения.
    И при возврате в Activity, все остается, как было. Но если приостановленное Activity уничтожается,
    например, при нехватке памяти, то соответственно удаляются и все его объекты. И если к нему снова вернуться,
    то системе надо заново его создавать и восстанавливать данные, которые были утеряны при уничтожении.
    Для этих целей Activity предоставляет нам для реализации пару методов: первый позволяет сохранить данные –
    onSaveInstanceState, а второй – восстановить - onRestoreInstanceState.
     */
    // сохраняем данные (millis_count)
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("save_timer_count", millis_count);
    }

    // восстанавливаем данные
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        millis_count = savedInstanceState.getLong("save_timer_count");
    }

}

