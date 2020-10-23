package com.arkapp.carparknaviagation.viewModels;

import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.arkapp.carparknaviagation.data.models.VoicePackageDetails;
import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.arkapp.carparknaviagation.utility.Constants;
import com.arkapp.carparknaviagation.utility.listeners.SplashListener;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.guidance.VoiceCatalog;
import com.here.android.mpa.guidance.VoicePackage;
import com.here.android.mpa.mapping.AndroidXMapFragment;

import java.util.ArrayList;
import java.util.List;

import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;

/**
 * Created by Abdul Rehman on 12-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class SplashViewModel extends ViewModel {

    public PrefRepository prefRepository;

    public SplashListener listener;

    private SharedPreferences preferences;

    public SplashViewModel(PrefRepository prefRepository, SharedPreferences preferences) {
        this.prefRepository = prefRepository;
        this.preferences = preferences;
    }

    public boolean isFetchLanguagesAvailable() {
        return prefRepository.getVoicePackages() != null && !prefRepository.getVoicePackages().isEmpty();
    }

    //This method will fetch the laguage list to show in the settings screen.
    public void fetchLanguages(AndroidXMapFragment mapFragment) {
        mapFragment.init(error -> {
            if (error == OnEngineInitListener.Error.NONE) {
                VoiceCatalog voiceCatalog = VoiceCatalog.getInstance();
                voiceCatalog.downloadCatalog(errorCode -> {
                    if (errorCode == VoiceCatalog.Error.NONE) {
                        // catalog download successful

                        List<VoicePackageDetails> packageDetails = new ArrayList<>();
                        // Get the list of voice packages from the voice catalog list
                        for (VoicePackage voicePackage : VoiceCatalog.getInstance().getCatalogList()) {
                            VoicePackageDetails package1 = new VoicePackageDetails(
                                    voicePackage.getLocalizedLanguage(),
                                    voicePackage.getMarcCode(),
                                    voicePackage.getDownloadSize(),
                                    voicePackage.getName(),
                                    voicePackage.getGender().name());

                            int count = 0;
                            for (VoicePackageDetails data : packageDetails) {
                                count++;
                                if (data.localizedLanguage.equals(voicePackage.getLocalizedLanguage()) &&
                                        data.getGender().equals(voicePackage.getGender().name()))
                                    break;
                            }
                            if (count == packageDetails.size())
                                packageDetails.add(package1);
                        }
                        prefRepository.setVoicePackages(packageDetails);
                    }
                });
            }
        });
    }

    //This method is used to download the language in background in the setting screen.
    //With the help of this method we can predownload the voice assistant data.
    public void downloadLanguage(AndroidXMapFragment mapFragment) {
        listener.showProgress();

        mapFragment.init(error -> {
            if (error == OnEngineInitListener.Error.NONE) {
                VoiceCatalog voiceCatalog = VoiceCatalog.getInstance();
                voiceCatalog.downloadCatalog(errorCode -> {
                    if (errorCode == VoiceCatalog.Error.NONE) {
                        // catalog download successful

                        // Get the list of voice packages from the voice catalog list
                        List<VoicePackage> voicePackages = VoiceCatalog.getInstance().getCatalogList();
                        long id;
                        // select
                        String selectedLanguageKey = preferences.getString(Constants.SETTING_LANGUAGE_KEY, "ENG");
                        printLog("selectedLanguageKey " + selectedLanguageKey);
                        String selectedLanguageId = selectedLanguageKey.split("~")[0];
                        String selectedLanguageGender = selectedLanguageKey.split("~")[1];

                        for (VoicePackage vPackage : voicePackages) {

                            printLog("vPackage.getMarcCode() " + vPackage.getMarcCode());
                            printLog("vPackage.getLocalizedLanguage() " + vPackage.getLocalizedLanguage());

                            if (vPackage.getMarcCode().equalsIgnoreCase(selectedLanguageId) &&
                                    vPackage.getGender().name().equalsIgnoreCase(selectedLanguageGender)) {
                                id = vPackage.getId();

                                printLog("id" + id);
                                printLog("voiceCatalog.isLocalVoiceSkin(id)" + voiceCatalog.isLocalVoiceSkin(id));
                                if (!voiceCatalog.isLocalVoiceSkin(id)) {
                                    voiceCatalog.downloadVoice(id, errorCode1 -> {
                                        if (errorCode1 == VoiceCatalog.Error.NONE) {
                                            printLog("download completed");
                                            listener.hideProgress();

                                        } else {
                                            listener.hideProgress();
                                            printLog("download error " + errorCode1.name());
                                        }
                                    });
                                } else
                                    listener.hideProgress();
                                break;
                            }
                        }
                    }
                });
            }
        });
    }
}
