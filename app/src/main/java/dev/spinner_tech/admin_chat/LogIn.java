package dev.spinner_tech.admin_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {
    EditText mail , pass ;
    FloatingActionButton signInBtn  ;
    FirebaseAuth mauth  ;
    ProgressBar pabr ;
    TextView signUptxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setBackgroundDrawableResource(R.drawable.background_image_one_signin);
        setContentView(R.layout.activity_log_in);
        mauth = FirebaseAuth.getInstance() ;

        mail = findViewById(R.id.emailEt);
        pass = findViewById(R.id.passEt);
        signInBtn = findViewById(R.id.signInBtn) ;
        pabr = findViewById(R.id.pbar) ;

        pabr.setVisibility(View.INVISIBLE);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MAIL = mail.getText().toString() ;
                String PASS = pass.getText().toString();




                if( PASS.isEmpty() || MAIL.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Fill The Data Properly!! " ,
                            Toast.LENGTH_LONG).show();

                }
                else

                {
                    signUser(MAIL , PASS) ;

                }



            }
        });


    }

    private void signUser(String mail, String pass) {


        pabr.setVisibility(View.VISIBLE);
        mauth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            pabr.setVisibility(View.INVISIBLE);
                            sentUserTOHome() ;

                        }
                        else {
                            pabr.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Authentication failed. "+task.getException() ,
                                    Toast.LENGTH_LONG).show();
                        }



                    }
                }) ;
    }
    private void sentUserTOHome() {


        Intent i = new Intent(getApplicationContext() , MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser muser = mauth.getCurrentUser();

        if(muser != null){
            sentUserTOHome();
        }

        else {
            // do nothing
        }

    }
}