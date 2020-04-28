package com.example.glorious.warehousemanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;

public class BottomNavbar {

    private static Intent appIntent;

    public BottomNavbar() { }

    public void assignIcon(BottomNavigationView bottomNav, int index) {
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(index);
        menuItem.setChecked(true);
    }

    public void switchToActivity(Context context, int itemId) {
        switch (itemId) {
            case R.id.navigation_home:
                navigateToActivity(context, MainActivity.class);
                break;
            case R.id.navigation_add:
                navigateToActivity(context, AddActivity.class);
                break;
            case R.id.navigation_request:
                navigateToActivity(context, RequestActivity.class);
                break;
            case R.id.navigation_remove:
                navigateToActivity(context, RemoveActivity.class);
                break;
            case R.id.navigation_settings:
                navigateToActivity(context, SettingsActivity.class);
                break;
        }
    }

    // Singleton pattern for appIntent (navigate back to running activity)
    private void navigateToActivity(Context context, Class<?> cls) {
        appIntent = new Intent(context, cls);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // overridePendingAnimation (remove animation
        Activity activity = (Activity) context;
        activity.overridePendingTransition(0, 0);
        context.startActivity(appIntent);
    }
}
