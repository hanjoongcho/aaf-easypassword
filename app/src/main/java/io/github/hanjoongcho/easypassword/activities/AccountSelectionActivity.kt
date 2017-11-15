package io.github.hanjoongcho.easypassword.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.databinding.DataBindingUtil
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountSelectionBinding

/**
 * Created by Administrator on 2017-11-15.
 */

class AccountSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
                .setContentView<ActivityCategorySelectionBinding>(this,
                        R.layout.activity_category_selection)

//        var player = intent.getParcelableExtra<Player>(EXTRA_PLAYER)
//        if (!isSignedIn()) {
//            if (player == null) {
//                player = getPlayer()
//            } else {
//                savePlayer(player)
//            }
//        }
//        binding.player = player
//        setUpToolbar()
//        if (savedInstanceState == null) {
//            attachCategoryGridFragment()
//        } else {
//            setProgressBarVisibility(View.GONE)
//        }
//        supportPostponeEnterTransition()
    }

}