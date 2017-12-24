package com.example.acer.mytimer;

import android.os.CountDownTimer;
import android.os.Bundle;

import android.util.SparseArray;
import android.support.v7.app.AppCompatActivity;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Math.pow;


public class ActivityTimer extends AppCompatActivity {
    TextView counting;
    Button switching;
    private SparseArray<String> numbers = new SparseArray<>();

    // состояния кнопки - таймер приостановлен/еще не запускался - "Start/continue counting!"
    // если в данный момент он работает - "Stop counting!"
    enum Button_states {
        onProcess,
        onPause
    }
    // текущее состояние кнопки - onProcess, т.к. хотим начать отсчет, значит, на кнопке должно быть написано "Start/continue..."
    Button_states button_state = Button_states.onProcess;

    // ведем отсчет секунд между сменами чисел на экране
    int seconds_cnt = 0;
    CountDownTimer my_timer;

    // метод для заполнения нашего массива чисел
    public void fill_array(SparseArray<String> array_to_fill) {
        String[] numberStrings = getResources().getStringArray(R.array.number_strings);
        int cnt = -1;
        int[] numbers = getResources().getIntArray(R.array.numbers);
        for (String str : numberStrings) {
            cnt++;
            array_to_fill.append(numbers[cnt], str);
        }
    }
    // активити создалось
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        final String button_on_process = getString(R.string.start);
        final String button_on_pause = getString(R.string.stop);

        // заполняем массив
        fill_array(numbers);

        switching = (Button)findViewById(R.id.button);
        counting = (TextView)findViewById(R.id.text);
        switching.setText(button_on_process);
        button_state = Button_states.onProcess;

        // при нажатии на кнопку меняем состояния
        switching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button_state == Button_states.onProcess) {
                    switching.setText(button_on_pause);
                    button_state = Button_states.onPause;
                    my_timer.start();
                } else if (button_state == Button_states.onPause) {
                    switching.setText(button_on_process);
                    button_state = Button_states.onProcess;
                    my_timer.cancel();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final String buttonStart = getString(R.string.start);
        // все время с момента запуска таймера
        long full_time = (1001 - seconds_cnt) * 1000;

        // интервал между сменой чисел по условию - одна секунда
        my_timer = new CountDownTimer(full_time, 1000) {
            StringBuilder new_str = new StringBuilder();
            @Override
            public void onTick(long millis) {
                seconds_cnt ++;
                if (seconds_cnt > 1001) {
                    this.onFinish();
                    this.cancel();
                }
                new_str.append(numbers.get(seconds_cnt, ""));
                if (new_str.length() == 0) {
                    // генерируем новую строку - отображаемое на экране число
                    int current = seconds_cnt;
                    for (int i = 0; current > 0; i++) {
                        int numeral = current % 10;
                        current /= 10;
                        if (numeral == 0) {
                            continue;
                        }
                        if (i == 0 && current % 10 == 1) {
                            new_str.insert(0, numbers.get(numeral + 10) + " ");
                            current /= 10;
                            i++;
                        } else {
                            new_str.insert(0, numbers.get(numeral*(int)pow(10, i)) + " ");
                        }
                    }
                }

                counting.setText(new_str.toString());
                // очищаем строку
                new_str.delete(0, new_str.length());
            }

            @Override
            public void onFinish() {
                switching.setText(buttonStart);
                button_state = Button_states.onProcess;
                new_str.delete(0, new_str.length());
                seconds_cnt = 0;
            }
        };
        if (button_state == Button_states.onPause) {
            my_timer.start();
        }
    }

    // onPause() – вызывается перед тем, как будет показано другое Activity
    @Override
    public void onPause() {
        super.onPause();
        my_timer.cancel();
    }

    // опять же, сохраняем состояние и восстанавливаем данные, если потребуется
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // запоминаем кол-во секунд, текст на кнопке (должны знать ее состояние) и текущее отображаемое число
        outState.putInt("seconds_count", seconds_cnt);
        outState.putString("text_in_button", switching.getText().toString());
        outState.putString("counting_number", counting.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        seconds_cnt = savedInstanceState.getInt("seconds_count");
        String textString = savedInstanceState.getString("counting_number", "");
        String buttonString = savedInstanceState.getString("text_in_button");

        final String button_on_process = getString(R.string.start);
        // проверяем, какая сейчас надпись на кнопке - в зависимости от этого определяем состояние
        if (buttonString.equals(button_on_process)) {
            button_state = Button_states.onProcess;
        } else {
            button_state = Button_states.onPause;
        }
        counting.setText(textString);
        switching.setText(buttonString);
    }
}
