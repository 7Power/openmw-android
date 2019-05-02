/*
    Copyright (C) 2016 sandstranger
    Copyright (C) 2018, 2019 Ilya Zhuravlev

    This file is part of OpenMW-Android.

    OpenMW-Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenMW-Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenMW-Android.  If not, see <https://www.gnu.org/licenses/>.
*/

package ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
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
import file.GameInstaller

import ui.activity.ConfigureControls
import ui.activity.ModsActivity

class FragmentSettings : PreferenceFragment(), OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        findPreference("pref_controls").setOnPreferenceClickListener {
            val intent = Intent(activity, ConfigureControls::class.java)
            this.startActivity(intent)
            true
        }

        findPreference("pref_mods").setOnPreferenceClickListener {
            val intent = Intent(activity, ModsActivity::class.java)
            this.startActivity(intent)
            true
        }

        findPreference("data_files").setOnPreferenceClickListener {
            val chooser = StorageChooser.Builder()
                .withActivity(activity)
                .withFragmentManager(fragmentManager)
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build()

            chooser.show()

            chooser.setOnSelectListener { path -> setupData(path) }
            true
        }
    }

    /**
     * Checks the specified path for a valid morrowind installation, generates config files
     * and saves the path to shared prefs if it's valid.
     * If it isn't, an error is displayed to the user.
     */
    private fun setupData(path: String) {
        val sharedPref = preferenceScreen.sharedPreferences

        val inst = GameInstaller(path)
        if (inst.check()) {
            inst.setNomedia()
            inst.convertIni(sharedPref.getString("pref_encoding",
                GameInstaller.DEFAULT_CHARSET_PREF)!!)
            with (sharedPref.edit()) {
                putString("data_files", inst.findDataFiles())
                apply()
            }
        } else {
            AlertDialog.Builder(activity)
                .setTitle(R.string.data_error_title)
                .setMessage(R.string.data_error_message)
                .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int -> }
                .show()
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
