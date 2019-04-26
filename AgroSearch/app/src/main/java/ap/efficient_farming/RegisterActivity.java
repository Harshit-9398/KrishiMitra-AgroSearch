package ap.efficient_farming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private Button RegisterButton;
    private EditText userEmail;
    private EditText userPassword;
    //FirebaseApp firebaseApp;
    private EditText userCPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       // firebaseApp = FirebaseApp.initializeApp(this);
            firebaseAuth = FirebaseAuth.getInstance();



        progressDialog = new ProgressDialog(this);

        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        userCPassword = findViewById(R.id.userCPassword);
        RegisterButton = findViewById(R.id.RegisterButton);


        RegisterButton.setOnClickListener(this);


    }

    private void registerNewUser() {

        final String number = userEmail.getText().toString().trim();
        final String password = userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a preferred password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("In Progress...");

        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(number, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            redirectToLogin();
                        } else {
                            // If sign in fails, display a message to the user
                            progressDialog.dismiss();
                            Log.d("anant", task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

//        firebaseAuth.createUserWithEmailAndPassword(number, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                progressDialog.dismiss();
//                if(task.isSuccessful()){
//                    Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    finish();
//                }else{
//                    Toast.makeText(RegisterActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }


    private void redirectToLogin(){
        finish();

        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onClick(View view) {

        if(view == RegisterButton){
            registerNewUser();
        }


    }
}