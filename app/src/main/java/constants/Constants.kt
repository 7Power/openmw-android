/*
    Copyright (C) 2015, 2016 sandstranger
    Copyright (C) 2019 Ilya Zhuravlev

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

package constants

import android.os.Environment
import com.libopenmw.openmw.BuildConfig

object Constants {
    val APP_PREFERENCES = "settings"
    val HIDE_CONTROLS = "pref_hide_controls"

    // TODO: the comment below is outdated
    // Base path: [/sdcard]/Android/data/[com.libopenmw.openmw]/
    // * /sdcard - in theory, can be different, haven't seen any on modern android though
    // * com.libopenmw.openmw - our application id
    //
    // $base/share - savedata, shouldn't touch this
    // $base/resources - resource files from openmw, ok to overwrite
    // $base/openmw - default settings, ok to overwrite
    // $base/config - user settings

    // TODO: this should be omw-nightly for nightly builds
    // e.g. /sdcard/omw
    val USER_FILE_STORAGE = Environment.getExternalStorageDirectory().toString() + "/omw/"

    // e.g. /data/data/com.libopenmw.openmw/files/config/settings-default.cfg
    var SETTINGS_DEFAULT_CFG = ""

    // e.g. /data/data/com.libopenmw.openmw/files/config/openmw.cfg
    var OPENMW_CFG = ""

    // e.g. /data/data/com.libopenmw.openmw/files/config/openmw.base.cfg
    var OPENMW_BASE_CFG = ""

    // e.g. /data/data/com.libopenmw.openmw/files/config/openmw.fallback.cfg
    var OPENMW_FALLBACK_CFG = ""

    // e.g. /data/data/com.libopenmw.openmw/files/resources
    var RESOURCES = ""

    // e.g. /data/data/com.libopenmw.openmw/files/config
    var GLOBAL_CONFIG = ""

    // e.g. /sdcard/Android/data/com.libopenmw.openmw/config
    val USER_CONFIG = "$USER_FILE_STORAGE/config"

    // e.g. /sdcard/Android/data/com.libopenmw.openmw/config/openmw.cfg
    val USER_OPENMW_CFG = "$USER_CONFIG/openmw.cfg"

    // Contains app version code for currently deployed resources; redeployed on mismatch
    // e.g. /sdcard/Android/data/com.libopenmw.openmw/files/stamp
    var VERSION_STAMP = ""
}
