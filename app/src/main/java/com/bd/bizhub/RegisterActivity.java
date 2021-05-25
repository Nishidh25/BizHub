package com.bd.bizhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bd.bizhub.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register";
    Button mButtonSignUp;
    Realm realm;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);


        realm = Realm.getDefaultInstance();
        Log.e("EXAMPLE", "Successfully opened a realm at: " + realm.getPath());


        TextInputLayout emailTV = findViewById(R.id.Email);
        TextInputLayout passwordTV = findViewById(R.id.Password);
        TextInputLayout nameTV = findViewById(R.id.Name);

        mButtonSignUp = findViewById(R.id.button_ereg);




        mButtonSignUp.setOnClickListener(v -> {

            EditText emailET = emailTV.getEditText();
            EditText passwordET = passwordTV.getEditText();
            EditText nameET = nameTV.getEditText();

            if (nameET.length() == 0) {
                showSnackBar("Enter Name");
                nameET.requestFocus();
            } else if (emailET.length() ==0) {
                showSnackBar("Enter a valid email");
                emailET.requestFocus();
            }else if (passwordET.length() ==0) {
                showSnackBar("Enter a valid password");
                passwordET.requestFocus();
            }//else if (mEditTextConfirmPassword.length() ==0) {
              //  showSnackBar("Enter a valid Password");
              //  mEditTextPassword.requestFocus();
            //}
            else {

                try{

    /*                realm.beginTransaction();

                    user = realm.createObject(User.class);
                    user.set_id("2");
                   // user.setEmail(emailET.getText().toString());
                   // user.setPassword(passwordET.getText().toString());
                    user.setName(nameET.getText().toString());

                    realm.commitTransaction();


*/

                    App app = new App(new AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                            .build());

                  //  Credentials emailPasswordCredentials = Credentials.emailPassword(emailET.getText().toString(), passwordET.getText().toString());

                    AtomicReference<io.realm.mongodb.User> user = new AtomicReference<>();
                    app.getEmailPassword().registerUserAsync(emailET.getText().toString(), passwordET.getText().toString(), it -> { //loginAsync
                        if (it.isSuccess()) {
                            Log.v("AUTH", "Successfully created User");
                            showSnackBar("Successfully created user");
                            user.set(app.currentUser());
                            Intent i = new Intent(RegisterActivity.this, NavigationActivity.class);
                            startActivity(i);
                            RegisterActivity.this.finish();
                        } else {
                            Log.e("AUTH", it.getError().toString());
                        }
                    });


                } catch (RealmPrimaryKeyConstraintException e){
                    e.printStackTrace();
                    showSnackBar("User found on db.");
                }

            }
        });

      //  realm.close();

    }

    private void showSnackBar(String msg) {
        try {
            Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }



}