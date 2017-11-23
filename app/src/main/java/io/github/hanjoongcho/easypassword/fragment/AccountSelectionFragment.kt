package io.github.hanjoongcho.easypassword.fragment

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.activities.AccountAddActivity
import io.github.hanjoongcho.easypassword.activities.AccountDetailActivity
import io.github.hanjoongcho.easypassword.adpaters.AccountAdapter
import io.github.hanjoongcho.easypassword.helper.TransitionHelper
import io.github.hanjoongcho.easypassword.helper.beforeDrawing
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.widget.OffsetDecoration

/**
 * Created by CHO HANJOONG on 2017-11-17.
 */

class AccountSelectionFragment : Fragment() {

    private var mRecyclerView: RecyclerView? = null

    private val adapter: AccountAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        AccountAdapter(activity,
                AdapterView.OnItemClickListener { _, v, position, _ ->
                    adapter?.getItem(position)?.let {
                        startAccountDetailActivityWithTransition(activity,
                                v.findViewById(R.id.category_icon), it)
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_categories, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view.findViewById<RecyclerView>(R.id.categories)
        mRecyclerView?.let { setUpGrid(it) }
        view.findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
//            startActivity(AccountAddActivity.getStartIntent(context))
            TransitionHelper.startSettingActivityWithTransition(activity, AccountAddActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        Thread({
            activity.database().initDatabase()
            Handler(Looper.getMainLooper()).post {
                adapter?.selectAccounts()
                adapter?.notifyDataSetChanged()
                view?.findViewById<ProgressBar>(R.id.loading_progress)?.visibility = View.INVISIBLE
            }
        }).start()
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
                                                         account: Account) {

        val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                *TransitionHelper.createSafeTransitionParticipants(activity,
                        false,
                        Pair(toolbar, getString(R.string.transition_category))))
                .toBundle()

        // Start the activity with the participants, animating from one to the other.
        val startIntent = AccountDetailActivity.getStartIntent(activity, account)
        startIntent.putExtra("sequence", account.sequence)
        ActivityCompat.startActivity(activity, startIntent, animationBundle)
    }

    companion object {

        private const val REQUEST_CATEGORY = 0x2300
    }

}