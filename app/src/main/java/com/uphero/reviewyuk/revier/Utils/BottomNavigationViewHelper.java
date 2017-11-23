package com.uphero.reviewyuk.revier.Utils;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.uphero.reviewyuk.revier.AddInfo.AddInfoActivity;
import com.uphero.reviewyuk.revier.Camera.CameraActivity;
import com.uphero.reviewyuk.revier.Home.HomeActivity;
import com.uphero.reviewyuk.revier.Maps.MapActivity;
import com.uphero.reviewyuk.revier.Profile.ProfileActivity;
import com.uphero.reviewyuk.revier.R;

/**
 * Created by Adi on 09/10/2017.
 */

public class BottomNavigationViewHelper {
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        //bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_item1:
                        Intent intent1 = new Intent(context, CameraActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.action_item2:
                        Intent intent2 = new Intent(context, AddInfoActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.action_item3:
                        Intent intent3 = new Intent(context, HomeActivity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.action_item4:
                        Intent intent4 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent4);
                        break;
                    case R.id.action_item5:
                        Intent intent5 = new Intent(context, MapActivity.class);
                        context.startActivity(intent5);
                        break;
                }

                return false;
            }
        });
    }
}
