package com.alex.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener{
    String myLog = "myLog";
    EditText etFrom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.d(myLog,"onCreat");

        etFrom = (EditText) findViewById(R.id.editText);
        etFrom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Log.d(myLog,"onClick()");

        switch (v.getId()){
            case R.id.editText:

                Log.d(myLog,"swith");

                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
        }
    }

}
