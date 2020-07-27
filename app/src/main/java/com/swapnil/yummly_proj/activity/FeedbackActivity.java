package com.swapnil.yummly_proj.activity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.swapnil.yummly_proj.R;

public class FeedbackActivity extends AppCompatActivity {

    private Toolbar mTopToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar_feedback);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(FeedbackActivity.this, "FeedbackActivity", Toast.LENGTH_SHORT).show();
        Button startBtn = (Button) findViewById(R.id.sendEmail);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });
    }
    protected void sendEmail() {
        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "subject text"+ "&body=" + "body text " + "&to=" + "feedback.yummly@gmail.com");
        mailIntent.setData(data);
        startActivity(Intent.createChooser(mailIntent, "Send Us FeedbackActivity"));
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
        startActivityForResult(myIntent,0);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(FeedbackActivity.this,MainActivity.class));
        finish();

    }

}