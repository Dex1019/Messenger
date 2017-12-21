package com.apkglobal.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText et_username, et_password;
    Button registerButton;
    String user, pass;
    TextView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerButton);
        login = (TextView) findViewById(R.id.login);

        Firebase.setAndroidContext(this);

//        clicklistener for login text view
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));


            }

        });


//        clicklistener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getting user inputs into string
                user = et_username.getText().toString();
                pass = et_password.getText().toString();

//              security features for user input
                if (user.equals("")) {
                    et_username.setError("can't be blank");
                } else if (pass.equals("")) {
                    et_password.setError("can't be blank");
                }


                else if (!user.matches("[A-Za-z0-9]+")) {
                    et_username.setError("only alphabet or number allowed");
                }

                else if (user.length() < 5) {
                            et_username.setError("at least 5 characters long");
                }



                else if (pass.length() < 5) {
                    et_password.setError("at least 5 characters long");
                }




                else {
                    final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();




//                    Volley initialization
                    String url = "https://chatapp-2429a.firebaseio.com/users.json";

//                    Stringrequest method to send data to the api
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override

//                        Response handling method in volley
                        public void onResponse(String s) {

                            Firebase reference = new Firebase("https://chatapp-2429a.firebaseio.com/users");

                            /*
                            * checks if
                            * */
                            if (s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                            }





                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);
//
//                                    if user is not present then create new user
                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();





//                                        check if user is already present then make toast
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "username already exists", Toast.LENGTH_LONG).show();
                                    }





                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            pd.dismiss();

                        }
//                        Error handling method in volley
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("some error occured" + error);
                            pd.dismiss();

                        }
                    });



//                    creating request queue for execution
                    RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
                    rQueue.add(stringRequest);


                }
            }
        });


    }
}
