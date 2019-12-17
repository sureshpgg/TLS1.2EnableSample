package com.erp.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Eswar on 23/07/16.
 */
public class LoginActivity extends Activity {


    private EditText userNameET;
    private EditText passwordET;
    private Context context;
    private TransparentProgress tp;
    private static String loginType;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new AndroidExceptionHandler(this));
        setContentView(R.layout.activity_login);


        context = this;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        TextView loginTitle = (TextView) findViewById(R.id.login_title_txt);
        TextView userNameTitle = (TextView) findViewById(R.id.username_title);
        TextView passwordTitle = (TextView) findViewById(R.id.password_title);

        TextView counter_txt = (TextView) findViewById(R.id.counter_txt);
        userNameET = (EditText) findViewById(R.id.txtUsername);
        passwordET = (EditText) findViewById(R.id.txtPassword);

        Button loginButton = (Button) findViewById(R.id.btnLogin);


        ImageView userSettingsButton = (ImageView) findViewById(R.id.user_settings_button);


        tp = new TransparentProgress(context);


        loginButton.setOnClickListener(loginClickListener);


        h = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {


                switch (msg.what) {

                    case 1:
                        tp.show();
                        break;
                    case 2:
                        tp.dismiss();

                        String txt = "Login Successfull";


                        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();


                        clear();
                        break;

                    case 3:
                        tp.dismiss();
                        txt = "Login Failed";

                        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();

                        break;

                    case 4:
                        tp.dismiss();

                        txt = "Login Failed\nServer returned null response";


                        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();

                        break;
                    case 5:
                        tp.dismiss();

                        txt = "Login Failed\nServer down";


                        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();

                        break;

                    default:
                        tp.dismiss();
                        break;

                }
                return false;
            }
        });


        CheckConnectivity.init(this);

    }


    View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (userNameET.getText().toString().isEmpty() || passwordET.getText().toString().isEmpty()) {

                // Toast.makeText(context, "Please fill all fields !", Toast.LENGTH_SHORT).show();


                String txt = "Please fill all fields !";


                Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();

                return;


            } else {
                try {
                    if (CheckConnectivity.isConnectingToInternet()) {
                        System.err.println("calling login task");
                        Thread th = new Thread(new VerfiyLogedInUserTask(userNameET.getText().toString(), passwordET.getText().toString(), 61 + ""));
                        th.start();

                    } else {
                        String txt = "No Internet Connection";
                        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };


    private void clear() {
//        userNameET.setText("");
//        passwordET.setText("");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onResume() {

        super.onResume();
        clear();


    }

    private Handler h;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    class VerfiyLogedInUserTask implements Runnable {

        HttpServiceImpl httpService;
        String user;
        String pwd;
        String apmcId;

        public VerfiyLogedInUserTask(String user, String pwd, String apmcId) {

            this.user = user;
            this.pwd = pwd;
            this.apmcId = apmcId;
        }

        @Override
        public void run() {

            h.sendEmptyMessage(1);
            if (httpService == null) {
                httpService = new HttpServiceImpl();
            }


            try {
                System.out.println("User " + user + "\n ampmcId " + apmcId);

                final String login = httpService.verifyLogin(user, pwd, apmcId,getApplicationContext());

                if (login != null) {
                    h.sendEmptyMessage(2);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(context, login, Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    h.sendEmptyMessage(4);
                }

            } catch (Exception e) {
                e.printStackTrace();
                h.sendEmptyMessage(5);
            }

        }


    }
}


