package com.arkapp.carparknaviagation.ui.setting;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.arkapp.carparknaviagation.BuildConfig;
import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.VoicePackageDetails;
import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.arkapp.carparknaviagation.ui.main.MainActivity;
import com.arkapp.carparknaviagation.ui.splash.SplashFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.arkapp.carparknaviagation.utility.Constants.DEFAULT_CARPARK_COUNT;
import static com.arkapp.carparknaviagation.utility.Constants.DEFAULT_SIMULATION_SPEED;
import static com.arkapp.carparknaviagation.utility.Constants.MAXIMUM_CARPARK_COUNT;
import static com.arkapp.carparknaviagation.utility.Constants.MAXIMUM_SIMULATION_SPEED;
import static com.arkapp.carparknaviagation.utility.Constants.MINIMUM_CARPARK_COUNT;
import static com.arkapp.carparknaviagation.utility.Constants.MINIMUM_SIMULATION_SPEED;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_CARPARK_LOT_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_CAR_PARK_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_LANGUAGE_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_RED_LIGHT_CAMERA_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_SIMULATION_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_SIMULATION_SPEED_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_SPEED_LIMIT_CAMERA_KEY;
import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;

/**
 * Created by Abdul Rehman on 09-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This fragment is used to show setting screen. It will contain all the
 * setting option of the app.
 */
public class SettingFragment extends PreferenceFragmentCompat {

    private Context context;
    private PreferenceScreen screen;
    private Preference emptyTitleSpace;
    private PreferenceCategory divider;
    private SwitchPreferenceCompat settingRedLightCamera;
    private SwitchPreferenceCompat speedCamera;
    private SwitchPreferenceCompat carPark;
    private SeekBarPreference carLotsCount;
    private PreferenceCategory divider2;
    private SwitchPreferenceCompat simulation;
    private SeekBarPreference simulationSpeed;
    private Preference appVersion;

    @Override
    public void onResume() {
        super.onResume();

        printLog("On Resume called");

        //Refresing the language the setting option. When the screen is opened
        PrefRepository prefRepository = new PrefRepository(requireContext());
        //11 is the total no. of setting options. IF the setting options are less it will refresh and add the voice setting option.
        if (prefRepository.getVoicePackages() != null && screen.getPreferenceCount() != 11) {
            List<VoicePackageDetails> packages = prefRepository.getVoicePackages();
            Collections.sort(packages, (v1, v2) -> v1.getLocalizedLanguage().compareTo(v2.getLocalizedLanguage()));

            List<String> languages = new ArrayList<>();
            List<String> languagesValue = new ArrayList<>();
            String defaultLanguage = "";

            //Creating the list for the language selection.
            for (VoicePackageDetails vPackage : packages) {

                languages.add(vPackage.getLocalizedLanguage() + " (" + vPackage.getGender().toLowerCase() + ")");
                languagesValue.add(vPackage.getMarcCode() + "~" + vPackage.getGender());
                if (vPackage.getMarcCode().equalsIgnoreCase("ENG") && vPackage.getGender().equalsIgnoreCase("female"))
                    defaultLanguage = vPackage.getMarcCode() + "~" + vPackage.getGender();
            }

            final CharSequence[] languageCharSequence = languages.toArray(new CharSequence[languages.size()]);
            final CharSequence[] languageValueCharSequence = languagesValue.toArray(new CharSequence[languagesValue.size()]);

            ListPreference languagePref = new ListPreference(context);
            languagePref.setTitle(getString(R.string.voice_assistant_language));
            languagePref.setSummary(getString(R.string.voice_assistant_language_summary));
            languagePref.setKey(SETTING_LANGUAGE_KEY);
            languagePref.setIcon(R.drawable.ic_voice);
            languagePref.setDefaultValue(defaultLanguage);
            languagePref.setEntries(languageCharSequence);
            languagePref.setEntryValues(languageValueCharSequence);

            languagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                SplashFragment frag = ((MainActivity) requireActivity()).splashFrag;
                frag.viewModel.downloadLanguage(frag.mapFragment);
                return true;
            });

            //Adding all the setting options in the screen UI.
            screen.addPreference(emptyTitleSpace);
            screen.addPreference(languagePref);
            screen.addPreference(divider);
            screen.addPreference(settingRedLightCamera);
            screen.addPreference(speedCamera);
            screen.addPreference(carPark);
            screen.addPreference(carLotsCount);
            screen.addPreference(divider2);
            screen.addPreference(simulation);//this
            screen.addPreference(simulationSpeed);//this
            screen.addPreference(appVersion);

            setPreferenceScreen(screen);
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        context = getPreferenceManager().getContext();
        screen = getPreferenceManager().createPreferenceScreen(getContext());

        emptyTitleSpace = new Preference(context);
        emptyTitleSpace.setTitle("");
        emptyTitleSpace.setSummary("");

        divider = new PreferenceCategory(context);
        divider.setTitle("Map settings");

        settingRedLightCamera = new SwitchPreferenceCompat(context);
        settingRedLightCamera.setTitle(getString(R.string.red_light_camera));
        settingRedLightCamera.setSummary(getString(R.string.red_light_camera_summary));
        settingRedLightCamera.setKey(SETTING_RED_LIGHT_CAMERA_KEY);
        settingRedLightCamera.setIcon(R.drawable.ic_red_camera);
        settingRedLightCamera.setSwitchTextOn("On");
        settingRedLightCamera.setSwitchTextOff("Off");
        settingRedLightCamera.setDefaultValue(true);

        speedCamera = new SwitchPreferenceCompat(context);
        speedCamera.setTitle(getString(R.string.speed_camera));
        speedCamera.setSummary(getString(R.string.speed_limit_summary));
        speedCamera.setKey(SETTING_SPEED_LIMIT_CAMERA_KEY);
        speedCamera.setIcon(R.drawable.ic_speed_camera_ring);
        speedCamera.setSwitchTextOn("On");
        speedCamera.setSwitchTextOff("Off");
        speedCamera.setDefaultValue(true);

        carPark = new SwitchPreferenceCompat(context);
        carPark.setTitle(getString(R.string.car_park));
        carPark.setSummary(getString(R.string.car_park_summary));
        carPark.setKey(SETTING_CAR_PARK_KEY);
        carPark.setIcon(R.drawable.ic_parking_marker_small);
        carPark.setSwitchTextOn("On");
        carPark.setSwitchTextOff("Off");
        carPark.setDefaultValue(true);

        carLotsCount = new SeekBarPreference(context);
        carLotsCount.setTitle(getString(R.string.carpark_lots));
        carLotsCount.setSummary(getString(R.string.carpark_lots_summary));
        carLotsCount.setKey(SETTING_CARPARK_LOT_KEY);
        carLotsCount.setMin(MINIMUM_CARPARK_COUNT);
        carLotsCount.setShowSeekBarValue(true);
        carLotsCount.setMax(MAXIMUM_CARPARK_COUNT);
        carLotsCount.setDefaultValue(DEFAULT_CARPARK_COUNT);

        /**
         *
         * The below are the setting option for simulation.
         * Remove the below option to hide the simulation option from the settings.
         *
         * To remove the simulation setting option follow this steps.
         * 1. Make the Default Value of simulation setting to false in line no. 193
         * 2. Remove or comment "simulation" , "simulationSpeed" setting option from the line no. '117' and '118' of this file.
         * 3. Make the total setting count from '11' to '9' in line no. '73' as we have removed 2 setting options.
         *
         *
         *  This will remove the simulation functionality from the app.
         * */
        divider2 = new PreferenceCategory(context);
        divider2.setTitle("Others");

        simulation = new SwitchPreferenceCompat(context);
        simulation.setTitle(getString(R.string.simulation));
        simulation.setSummary(getString(R.string.simulation_summary));
        simulation.setKey(SETTING_SIMULATION_KEY);
        simulation.setSwitchTextOn("On");
        simulation.setSwitchTextOff("Off");
        simulation.setDefaultValue(true);

        simulationSpeed = new SeekBarPreference(context);
        simulationSpeed.setTitle(getString(R.string.simulation_speed));
        simulationSpeed.setSummary(getString(R.string.simulation_speed_summary));
        simulationSpeed.setKey(SETTING_SIMULATION_SPEED_KEY);
        simulationSpeed.setMin(MINIMUM_SIMULATION_SPEED);
        simulationSpeed.setShowSeekBarValue(true);
        simulationSpeed.setMax(MAXIMUM_SIMULATION_SPEED);
        simulationSpeed.setDefaultValue(DEFAULT_SIMULATION_SPEED);

        appVersion = new Preference(context);
        appVersion.setTitle(getString(R.string.version));
        appVersion.setSummary(BuildConfig.VERSION_NAME);

        setPreferenceScreen(null);
    }
}
