package ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceGroup

import com.codekidlabs.storagechooser.StorageChooser
import com.libopenmw.openmw.R

import ui.activity.ConfigureControls
import ui.activity.ModsActivity

class FragmentSettings : PreferenceFragment(), OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        findPreference("pref_controls").setOnPreferenceClickListener { pref: Preference ->
            val intent = Intent(activity, ConfigureControls::class.java)
            this.startActivity(intent)
            true
        }

        findPreference("pref_mods").setOnPreferenceClickListener { pref: Preference ->
            val intent = Intent(activity, ModsActivity::class.java)
            this.startActivity(intent)
            true
        }

        findPreference("data_files").setOnPreferenceClickListener { pref: Preference ->
            val chooser = StorageChooser.Builder()
                .withActivity(activity)
                .withFragmentManager(fragmentManager)
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build()

            chooser.show()

            chooser.setOnSelectListener { path ->
                val sharedPref = preferenceScreen.sharedPreferences
                val editor = sharedPref.edit()
                editor.putString("data_files", path)
                editor.apply()
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        for (i in 0 until preferenceScreen.preferenceCount) {
            val preference = preferenceScreen.getPreference(i)
            if (preference is PreferenceGroup) {
                for (j in 0 until preference.preferenceCount) {
                    val singlePref = preference.getPreference(j)
                    updatePreference(singlePref, singlePref.key)
                }
            } else {
                updatePreference(preference, preference.key)
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        updatePreference(findPreference(key), key)
    }

    private fun updatePreference(preference: Preference?, key: String) {
        if (preference == null)
            return
        if (preference is EditTextPreference) {
            val editTextPreference = preference as EditTextPreference?
            editTextPreference!!.summary = editTextPreference.text
        }
        // Show selected value as a summary for data_files
        if (key == "data_files") {
            preference.summary = preference.sharedPreferences.getString("data_files", "")
        }
    }

}
