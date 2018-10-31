package com.example.bursel.databaseapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

public class EditDairyActivity extends AppCompatActivity {
    DairysDBHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dairy);
        final EditText title=(EditText)findViewById(R.id.title);
        final EditText body=(EditText)findViewById(R.id.body);
        Button btnConfirm=(Button)findViewById(R.id.confirm);
        Bundle bundle=getIntent().getBundleExtra("dairy");
        if(bundle!=null){
            String titleFromUpdate=bundle.getString("title");
            String bodyFromUpdate=bundle.getString("body");
            final String id=bundle.getString("id");
            title.setText(titleFromUpdate);
            body.setText(bodyFromUpdate);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String titleStr=title.getText().toString();
                    String bodyStr=body.getText().toString();
                    MainActivity.Update(id,titleStr, bodyStr);
                    Intent intent=new Intent(EditDairyActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        else{
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String titleStr=title.getText().toString();
                    String bodyStr=body.getText().toString();
                    MainActivity.Insert(titleStr, bodyStr);
                    Intent intent=new Intent(EditDairyActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });
        }

    }




}
