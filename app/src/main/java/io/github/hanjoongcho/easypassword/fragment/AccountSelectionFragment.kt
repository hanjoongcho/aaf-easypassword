package io.github.hanjoongcho.easypassword.fragment

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.activities.AccountDetailActivity
import io.github.hanjoongcho.easypassword.adpaters.AccountAdapter
import io.github.hanjoongcho.easypassword.helper.TransitionHelper
import io.github.hanjoongcho.easypassword.helper.beforeDrawing
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.widget.OffsetDecoration

/**
 * Created by CHO HANJOONG on 2017-11-17.
 */

class AccountSelectionFragment : Fragment() {

    private val adapter: AccountAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        AccountAdapter(activity,
                AdapterView.OnItemClickListener { _, v, position, _ ->
                    adapter?.getItem(position)?.let {
                        startAccountDetailActivityWithTransition(activity,
                                v.findViewById(R.id.account_title), it)
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_categories, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpGrid(view.findViewById<RecyclerView>(R.id.categories))
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("NewApi")
    private fun setUpGrid(categoriesView: RecyclerView) {
        with(categoriesView) {
            addItemDecoration(OffsetDecoration(context.resources
                    .getDimensionPixelSize(R.dimen.spacing_nano)))
            adapter = this@AccountSelectionFragment.adapter
            beforeDrawing { activity.supportStartPostponedEnterTransition() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        if (requestCode == REQUEST_CATEGORY && resultCode == R.id.solved) {
//            adapter?.notifyItemChanged(data.getStringExtra(JsonAttributes.ID))
//        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startAccountDetailActivityWithTransition(activity: Activity, toolbar: View,
                                                category: Account) {

        val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                *TransitionHelper.createSafeTransitionParticipants(activity,
                        false,
                        Pair(toolbar, activity.getString(R.string.transition_toolbar))))
                .toBundle()

        // Start the activity with the participants, animating from one to the other.
        val startIntent = AccountDetailActivity.getStartIntent(activity, category)
        ActivityCompat.startActivityForResult(activity,
                startIntent,
                REQUEST_CATEGORY,
                animationBundle)
    }

    companion object {

        private const val REQUEST_CATEGORY = 0x2300
    }

}