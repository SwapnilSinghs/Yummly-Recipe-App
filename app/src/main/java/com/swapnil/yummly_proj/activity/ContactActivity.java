package com.swapnil.yummly_proj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
import com.swapnil.yummly_proj.R;

import java.util.List;

public class ContactActivity extends AppCompatActivity {
    CircleMenu circleMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#f2f098"), R.drawable.ic_menu1, R.drawable.ic_cancel);
        circleMenu.addSubMenu(Color.parseColor("#f2cf76"), R.drawable.ic_facebook)
                .addSubMenu(Color.parseColor("#f2cf76"), R.drawable.ic_twitter)
                .addSubMenu(Color.parseColor("#f2cf76"), R.drawable.youtube);

        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener()
        {
            @Override
            public void onMenuSelected(int index){
                switch (index){
                    case 0:
                        Intent fb = new Intent(Intent.ACTION_VIEW);
                        fb.setData(Uri.parse("https://www.facebook.com/yummly/"));
                        startActivity(fb);
                        break;

                    case 1:
                        Intent tw = new Intent(Intent.ACTION_VIEW);
                        tw.setData(Uri.parse("https://twitter.com/yummly"));
                        startActivity(tw);
                        break;
                    case 2:
                        Intent yt = new Intent(Intent.ACTION_VIEW);
                        yt.setData(Uri.parse("https://www.youtube.com/watch?v=XIiM0tHVwH0"));
                        startActivity(yt);
                        break;
                }
            }
        });
        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener()
        {
            @Override
            public void onMenuOpened() {
                Toast.makeText(ContactActivity.this,"Contact Yummly Here",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onMenuClosed(){
                Toast.makeText(ContactActivity.this,"Closed Contact Sources",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent_contact = new Intent(ContactActivity.this, MainActivity.class);
        startActivity(intent_contact);
    }


}
