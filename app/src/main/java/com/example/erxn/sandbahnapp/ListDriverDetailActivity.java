package com.example.erxn.sandbahnapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ListDriverDetailActivity extends AppCompatActivity {

    ListView listDriverDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_driver_detail);

        initControlls();
    }

    private void initControlls() {
        listDriverDetail = (ListView)findViewById(R.id.LIST_DRIVER_DETAIL);

    }
}
