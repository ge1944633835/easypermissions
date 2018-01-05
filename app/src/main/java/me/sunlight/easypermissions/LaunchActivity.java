package me.sunlight.easypermissions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.sunligh.easypermissions.R;
import me.sunlight.easypermissions.assist.PermissionsFragment;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        findViewById(R.id.btnMain).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean haveAll = PermissionsFragment.haveAll(this, getSupportFragmentManager());

        if (haveAll){
            HomeActivity.runActivity(this);
            finish();
        }

    }
}
