package com.arkapp.carparknaviagation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.ui.home.HomeFragment;
import com.arkapp.carparknaviagation.ui.setting.SettingFragment;
import com.arkapp.carparknaviagation.ui.splash.SplashFragment;
import com.arkapp.carparknaviagation.utility.listeners.GPSListener;

import static com.arkapp.carparknaviagation.utility.Constants.HOME_FRAGMENT;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_FRAGMENT;
import static com.arkapp.carparknaviagation.utility.Constants.SPLASH_FRAGMENT;
import static com.arkapp.carparknaviagation.utility.ViewUtils.hide;
import static com.arkapp.carparknaviagation.utility.ViewUtils.show;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.REQUEST_CHECK_SETTINGS;

public class MainActivity extends AppCompatActivity {

    public static GPSListener gpsListener;
    private HomeFragment homeFrag;
    private SettingFragment settingFrag;
    public SplashFragment splashFrag;
    private FragmentManager fm;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initReplaceFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            gpsListener.onGPSPermissionChecked();
        }
    }

    public void openHomeScreen() {
        fm.beginTransaction().hide(activeFragment).show(homeFrag).commit();
        activeFragment = homeFrag;
        homeFrag.checkGpsSetting();
    }

    public void openSetting() {
        settingFrag.onResume();
        fm.beginTransaction().hide(activeFragment).show(settingFrag).addToBackStack(HOME_FRAGMENT).commit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.setting);
        show(toolbar);
    }

    private void initReplaceFragment() {
        splashFrag = new SplashFragment();
        homeFrag = new HomeFragment();
        settingFrag = new SettingFragment();

        activeFragment = splashFrag;

        fm = getSupportFragmentManager();

        fm.beginTransaction().add(R.id.fragment, settingFrag, SETTING_FRAGMENT).hide(settingFrag).commit();
        fm.beginTransaction().add(R.id.fragment, homeFrag, HOME_FRAGMENT).hide(homeFrag).commit();
        fm.beginTransaction().add(R.id.fragment, splashFrag, SPLASH_FRAGMENT).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (findViewById(R.id.toolbar) != null)
            hide(findViewById(R.id.toolbar));
    }
}