package com.example.bursel.databaseapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        ArrayList<Map<String, String>> items= (ArrayList<Map<String, String>>) bundle.getSerializable("result");

        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item,
                new String[]{Dairys.Dairy._ID,Dairys.Dairy.COLUMN_NAME_TITLE, Dairys.Dairy.COLUMN_NAME_TITLE},
                new int[]{R.id.textId,R.id.textViewTitle, R.id.textViewBody});

        ListView list = (ListView) findViewById(R.id.lstSearchResultWords);

        list.setAdapter(adapter);
    }
}
