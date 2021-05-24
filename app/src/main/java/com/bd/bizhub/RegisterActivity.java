package com.bd.bizhub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register";
    Button mButtonSignUp;
    Realm realm;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder()
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .build();
        realm = Realm.getInstance(config);
        Log.e("EXAMPLE", "Successfully opened a realm at: " + realm.getPath());


        TextInputLayout emailTV = findViewById(R.id.Email);
        TextInputLayout passwordTV = findViewById(R.id.Password);
        TextInputLayout nameTV = findViewById(R.id.Name);

        mButtonSignUp = findViewById(R.id.button_ereg);




        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        realm.beginTransaction();

                        user = realm.createObject(User.class);
                        user.setId("2");
                        user.setEmail(emailET.getText().toString());
                        user.setPassword(passwordET.getText().toString());
                        user.setName(nameET.getText().toString());

                        realm.commitTransaction();

                        showSnackBar("Save Success");


                    } catch (RealmPrimaryKeyConstraintException e){
                        e.printStackTrace();
                        showSnackBar("User found on db.");
                    }

                }
            }
        });

        realm.close();

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