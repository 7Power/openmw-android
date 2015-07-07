package fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.widget.Toast;

import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.lb.material_preferences_library.custom_preferences.CheckBoxPreference;
import com.lb.material_preferences_library.custom_preferences.ListPreference;
import com.libopenmw.openmw.R;

import constants.Constants;
import ui.files.Writer;

public class FragmentSettings extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        
        CheckBoxPreference subtitlescheckBoxPreference = (CheckBoxPreference) findPreference(Constants.SUBTITLES);
        subtitlescheckBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                boolean showSubtitles = (boolean) newValue;
                saveSubtitlesSettings(showSubtitles);

                return true;
            }
        });

        ListPreference encodingList = (ListPreference) findPreference(Constants.LANGUAGE);

        encodingList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                String encoding = newValue.toString();
                try {
                    Writer.write(
                            encoding,
                            Constants.configsPath + "/config/openmw/openmw.cfg",
                            "encoding");

                } catch (Exception e) {

                }
                return true;
            }
        });
        ListPreference mipmappingList = (ListPreference) findPreference(Constants.MIPMAPPING);

        mipmappingList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                String mipmapping = newValue.toString();
                saveMipMappingOptions(mipmapping);
                return true;
            }
        });

        ListPreference resolutionList = (ListPreference) findPreference(Constants.RESOLUTION);

        resolutionList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                String currentResolution = newValue.toString();
                ScreenResolutionHelper resolutionHelper = new ScreenResolutionHelper(FragmentSettings.this.getActivity());
                resolutionHelper.writeScreenResolution(currentResolution);
                return true;
            }
        });

    }

    private void saveSubtitlesSettings(boolean showSubtitles) {

        try {
            Writer.write(String.valueOf(showSubtitles), Constants.configsPath
                    + "/config/openmw/settings.cfg", "subtitles");

        } catch (Exception e) {

            Toast toast = Toast.makeText(FragmentSettings.this
                            .getActivity().getApplicationContext(),
                    "configs files not found", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void saveMipMappingOptions(String mipmapping) {
        try {
            Writer.write(mipmapping, Constants.configsPath
                            + "/config/openmw/settings.cfg",
                    "texture filtering");

        } catch (Exception e) {
            Toast toast = Toast.makeText(FragmentSettings.this
                            .getActivity().getApplicationContext(),
                    "configs files not found",
                    Toast.LENGTH_LONG);
            toast.show();
        }

    }


}
