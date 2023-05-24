package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUp extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText Email,password,signupName,Password2;
    Button SignupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView loginRedirectText;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(),MainActivity2.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth=FirebaseAuth.getInstance();

        signupName=findViewById(R.id.signup_name);
        Email=findViewById(R.id.signup_email);
        password=findViewById(R.id.Password);
        Password2=findViewById(R.id.Password2);
        SignupButton = findViewById(R.id.signup_button);
        progressBar =findViewById(R.id.ProgressBar);
        loginRedirectText =findViewById(R.id.Login_button);



        Button Signup = (Button) findViewById(R.id.signup_button);


//login link
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mAuth = FirebaseAuth.getInstance();




        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = signupName.getText().toString();
                String email = Email.getText().toString();
                String password1 = password.getText().toString();
                String password2 = Password2.getText().toString();
                String MainPassword;
                database=FirebaseDatabase.getInstance();
                reference=database.getReference("users");


                //Validation

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUp.this, "Please Enter EMail", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password1)) {
                    Toast.makeText(SignUp.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password2)) {
                    Toast.makeText(SignUp.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SignUp.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Compare Password
                if (password1.equals(password2)) {
                    MainPassword = password1;
                } else {
                    Toast.makeText(SignUp.this, "Both Password Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }


//Data send to real time Database
                if (!name.isEmpty() && !email.isEmpty() && !MainPassword.isEmpty()) {
                    HelperClass helperClass = new HelperClass(name, email, MainPassword);
                    reference.child(name).setValue(helperClass);
                }



//Authentication

                mAuth.createUserWithEmailAndPassword(email,MainPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    Toast.makeText(SignUp.this, "Authentication successful .",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



            }

    });
    }
}
