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

import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Home";
    TextView Create;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        realm = Realm.getDefaultInstance();
        Log.e("EXAMPLE", "Successfully opened a realm at: " + realm.getPath());


        TextInputLayout emailTV = findViewById(R.id.Email);
        TextInputLayout passwordTV = findViewById(R.id.Password);

        Button mButtonLogin = findViewById(R.id.button_elogin);

        Create = findViewById(R.id.CreateAcc);


        Create.setOnClickListener(v -> {

            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
            LoginActivity.this.finish();

        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText emailET = emailTV.getEditText();
                EditText passwordET = passwordTV.getEditText();

               // assert emailET != null;
                if (emailET.length() == 0) {
                    showSnackBar("Enter EMAIL");
                    emailET.requestFocus();
                } else {
               //     assert passwordET != null;
                    if (passwordET.length() == 0) {
                        showSnackBar("Enter password");
                        passwordET.requestFocus();

                    }
                }
 /*
                assert passwordET != null;
                if (checkUser(emailET.getText().toString(),passwordET.getText().toString())){
                    showSnackBar("Login Success");
                    Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
                    startActivity(i);
                }else {
                    showSnackBar("Login Failed");
                }
*/


                App app = new App(new AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                        .build());
                Credentials emailPasswordCredentials = Credentials.emailPassword(emailET.getText().toString(),passwordET.getText().toString());
                AtomicReference<User> user = new AtomicReference<>();
                app.loginAsync(emailPasswordCredentials, it -> {
                    if (it.isSuccess()) {
                        Log.v("AUTH", "Successfully authenticated using an email and password.");
                        showSnackBar("Login Success");
                        user.set(app.currentUser());
                        Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
                        startActivity(i);

                    } else {
                        Log.e("AUTH", it.getError().toString());
                        showSnackBar("Login Failed: "+it.getError().toString());
                    }
                });

            }

  /*          private boolean checkUser(String email, String password) {
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
*/

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