package io.github.hanjoongcho.easypassword.helper

import android.support.multidex.MultiDexApplication
import io.realm.Realm

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class EasyPasswordApplication : MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}
