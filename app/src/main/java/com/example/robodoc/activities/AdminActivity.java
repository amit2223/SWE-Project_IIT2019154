package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.auth.SignOut;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

public class AdminActivity extends AppCompatActivity implements SignOut.SignOutInterface {

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar=findViewById(R.id.adminToolbar);

        updateInterface();

        toolbar.setOnMenuItemClickListener(item -> {
            Menu menu=toolbar.getMenu();
            if(item==menu.findItem(R.id.aMenuDoctor))
                startDoctorActivity();
            else if(item==menu.findItem(R.id.aMenuUser))
                startMainActivity();
            else if(item==menu.findItem(R.id.aMenuLogout))
                signOut();
            return false;
        });
    }

    private void updateInterface(){
        if(Globals.isUserDoctor())
            toolbar.getMenu().findItem(R.id.aMenuDoctor).setVisible(true);
    }

    private void startDoctorActivity(){
        startActivity(new Intent(this,DoctorActivity.class));
        this.finish();
    }

    private void startMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private void signOut(){
        new SignOut(this,this,getSupportFragmentManager());
    }

    @Override
    public void onSignOut(boolean result) {
        if(result){
            Toast.makeText(AdminActivity.this,"Sign Out Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminActivity.this,LoginActivity.class));
            AdminActivity.this.finish();
        }
        else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"Sign Out Failed, Please try Again",2000).show();
        }
    }
}