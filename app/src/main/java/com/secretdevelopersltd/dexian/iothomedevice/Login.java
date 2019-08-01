package com.secretdevelopersltd.dexian.iothomedevice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    String TAG = "XIAN";

    EditText ET_email, ET_password;
    Button btn_login, btn_signup;
    TextView TV_forgetPass;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toast.makeText(getApplicationContext(),"Please Login",Toast.LENGTH_LONG).show();

        ET_email = findViewById(R.id.ET_email);
        ET_password = findViewById(R.id.ET_password);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        TV_forgetPass = findViewById(R.id.TV_forgetPass);



        if(getUID().equalsIgnoreCase("Null")){
            //String login[] = getLogIn();
            //Firebase
            mAuth = FirebaseAuth.getInstance();


            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if(firebaseAuth.getCurrentUser() == null){
                        Toast.makeText(getApplicationContext(),"Please Login",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"User Logged-in", Toast.LENGTH_LONG).show();
                    }
                }
            };


            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn(ET_email.getText().toString(),ET_password.getText().toString());
                }
            });

        }else{
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpForm();
            }
        });



    }


    private void SignUpForm(){



        AlertDialog.Builder myBuilder = new AlertDialog.Builder(Login.this);
        View myView = getLayoutInflater().inflate(R.layout.register_dialog, null);

        final EditText ET_fullName, ET_regEmail, ET_PhoneNumber, ET_Address, ET_DID, ET_pWord1, ET_pWord2;
        Button btn_signUpDone;

        ET_fullName = myView.findViewById(R.id.ET_fullName);
        ET_regEmail = myView.findViewById(R.id.ET_regEmail);
        ET_PhoneNumber = myView.findViewById(R.id.ET_PhoneNumber);
        ET_Address = myView.findViewById(R.id.ET_Address);
        ET_DID = myView.findViewById(R.id.ET_DID);
        ET_pWord1 = myView.findViewById(R.id.ET_pWord1);
        ET_pWord2 = myView.findViewById(R.id.ET_pWord2);
        btn_signUpDone = myView.findViewById(R.id.btn_signUpDone);

        myBuilder.setView(myView);
        final AlertDialog Dialog = myBuilder.create();
        Dialog.show();


        btn_signUpDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ET_regEmail.getText().toString().trim();
                String pass1 = ET_pWord1.getText().toString().trim();
                String pass2 = ET_pWord2.getText().toString().trim();
                String name = ET_fullName.getText().toString().trim();
                String phone = ET_PhoneNumber.getText().toString().trim();
                String address = ET_Address.getText().toString().trim();
                String DID = ET_DID.getText().toString().trim();

                if(pass1.equals(pass2)){
                    signUp(email,pass1,name,phone,address,DID,Dialog);
                }else{
                    Toast.makeText(getApplicationContext(),"Password doesn't match",Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private void signIn(String email, String pass){

        Log.i(TAG,"Firebase SignIn Method");
        final String E = email;
        final String P = pass;

        if(!email.equals("") && !pass.equals("")){

            Log.i(TAG,"Email: "+email+" Password: "+pass);

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i(TAG,"Login Success");
                        Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();

                        //Firebase Auth
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser fu = mAuth.getCurrentUser();
                        String uID = fu.getUid();

                        storeUID(uID);
                        getNameFromFirebase(uID);

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();

                    }else {
                        Log.i(TAG,"Login failed");
                        Toast.makeText(getApplicationContext(),"Wrong Password or Email",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else {
            Toast.makeText(getApplicationContext(),"Please fill up Email and Password!",Toast.LENGTH_LONG).show();
        }

    }

    private void signUp(final String Email, String password, final String Name, final String Phone, final String Address, final String DID, final AlertDialog Dialog){

        //String email = inputEmail.getText().toString().trim();
        //String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);
        //create user
        mAuth.createUserWithEmailAndPassword(Email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(TAG,"createUserWithEmail:onComplete:" + task.isSuccessful());
                        //progressBar.setVisibility(View.GONE);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Registration failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            FirebaseUser fu = task.getResult().getUser();
                            String UID = fu.getUid();

                            User u = new User(Name,Email,Phone,Address,DID);

                            pushToFirebase(UID,u);

                            Toast.makeText(Login.this, "Registration Complete Successfully, Please login now",
                                    Toast.LENGTH_LONG).show();

                            Dialog.cancel();

                        }
                    }
                });
    }

    private void pushToFirebase(String uid, User user) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("USER");
        myRef.child(uid).setValue(user);

    }

    private void getNameFromFirebase(String uid) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("USER");
        Query query = myRef.orderByKey().equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    User uu = ds.getValue(User.class);
                    storeUname(uu.getName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void storeUID(String UID){
        SharedPreferences mSharedPreferences = getSharedPreferences("APP", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("UID",UID);
        mEditor.apply();
    }

    private String getUID(){
        String UID;
        SharedPreferences mSharedPreferences = getSharedPreferences("APP", MODE_PRIVATE);
        UID =  mSharedPreferences.getString("UID","Null");
        return UID;
    }

    private void storeUname(String Uname){
        SharedPreferences mSharedPreferences = getSharedPreferences("APP", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("UNAME",Uname);
        mEditor.apply();
    }

    private String getUname(){
        String UID;
        SharedPreferences mSharedPreferences = getSharedPreferences("APP", MODE_PRIVATE);
        UID =  mSharedPreferences.getString("UNAME","No Name");
        return UID;
    }



}
