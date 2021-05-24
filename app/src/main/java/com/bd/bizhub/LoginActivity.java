package com.bd.bizhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Home";
    TextView Create;
    private Realm realm;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);


        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .build();
        realm = Realm.getInstance(config);
        Log.e("EXAMPLE", "Successfully opened a realm at: " + realm.getPath());


        TextInputLayout emailTV = findViewById(R.id.Email);
        TextInputLayout passwordTV = findViewById(R.id.Password);

        Button mButtonLogin = findViewById(R.id.button_elogin);

        Create = findViewById(R.id.CreateAcc);


        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);

            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText emailET = emailTV.getEditText();
                EditText passwordET = passwordTV.getEditText();

                if (emailET.length() == 0) {
                    showSnackBar("Enter EMAIL");
                    emailET.requestFocus();
                } else if (passwordET.length() == 0) {
                    showSnackBar("Enter password");
                    passwordET.requestFocus();

                }

                if (checkUser(emailET.getText().toString(),passwordET.getText().toString())){
                    showSnackBar("Login Success");
                    Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
                    startActivity(i);
                }else {
                    showSnackBar("Login Failed");
                }

            }

            private boolean checkUser(String email, String password) {
                RealmResults<User> realmObjects = realm.where(User.class).findAll();
                for (User user : realmObjects) {
                    if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                        Log.e(TAG, user.getEmail());
                        return true;
                    }
                }
                Log.e(TAG, String.valueOf(realm.where(User.class).contains("email", email)));
                return false;
            }


            private void showSnackBar(String msg) {
                try {
                    Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }


        });
      //  realm.close();
    }
}