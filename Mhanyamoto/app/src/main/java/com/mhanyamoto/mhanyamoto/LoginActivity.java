package com.mhanyamoto.mhanyamoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mhanyamoto.mhanyamoto.entity.Login;
import com.mhanyamoto.mhanyamoto.service.Service;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by stark on 4/19/2015.
 */
public class LoginActivity extends Activity {

    private boolean doubleBackToExitPressedOnce;
    Service mService;
    EditText et_username, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ImageButton logBtn = (ImageButton) findViewById(R.id.loginBtn);
        Button regBtn = (Button) findViewById(R.id.registerBtn);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

        //Get From Cache
        hawkInit();
        Hawk.remove("idUser");
        String result = Hawk.get("idUser","0");

        if (!result.contains("0")) {
            Intent loginMenu = new Intent(LoginActivity.this, MenuActivity.class);
            finish();
            startActivity(loginMenu);
        }

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Progress Bar
                final ProgressDialog mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Authentication...");
                mProgressDialog.show();

                initAPI();
                mService.login(et_username.getText().toString(), et_password.getText().toString(), new Callback<Login>() {
                    @Override
                    public void success(Login login, Response response) {
                        if (login.isSuccess()) {

                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                            //----Save To cache
                            hawkInit();
                            Hawk.put("idUser", login.getId());
                            Hawk.put("nameUser", login.getName());
                            Hawk.put("emailUser", login.getEmail());

                            mProgressDialog.dismiss();
                            finish();
                            startActivity(intent);




                        } else {

                            mProgressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Kombinasi email dan password tidak tepat", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        mProgressDialog.dismiss();
                    }
                });


            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void initAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(getResources().getString(R.string.host))
                .build();

        mService = restAdapter.create(Service.class);
    }

    private void hawkInit() {
        Hawk.init(LoginActivity.this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(LoginActivity.this))
                .setLogLevel(LogLevel.FULL)
                .build();
    }
}
