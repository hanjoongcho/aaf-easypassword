package io.github.hanjoongcho.easypassword.fragment

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.simplemobiletools.commons.extensions.onGlobalLayout
import com.simplemobiletools.commons.views.MyRecyclerView
import io.github.hanjoongcho.commons.helpers.TransitionHelper
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.activities.SecurityAddActivity
import io.github.hanjoongcho.easypassword.activities.SecurityDetailActivity
import io.github.hanjoongcho.easypassword.adpaters.SecurityAdapter
import io.github.hanjoongcho.easypassword.extensions.config
import io.github.hanjoongcho.easypassword.helper.beforeDrawing
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Security
import io.github.hanjoongcho.easypassword.widget.OffsetDecoration
import kotlinx.android.synthetic.main.fragment_securities.*

/**
 * Created by CHO HANJOONG on 2017-11-17.
 */

class SecuritySelectionFragment : Fragment(), SecurityAdapter.ItemOperationsListener {
    private var mKeyword: String? = ""
    private var storedItems = mutableListOf<Security>()

    private val adapter: SecurityAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        activity?.let { activity ->
            SecurityAdapter(activity,
                    this@SecuritySelectionFragment,
                    AdapterView.OnItemClickListener { _, v, position, _ ->
                        adapter?.getItem(position)?.let {
                            startAccountDetailActivityWithTransition(activity,
                                    v.findViewById(R.id.category_icon), it)
                        }
                    })
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_securities, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        setUpGrid(securities)
        add.setOnClickListener {
            activity?.let {
                TransitionHelper.startActivityWithTransition(it, SecurityAddActivity::class.java)    
            }
        }

       
        
//        items_swipe_refresh.setOnRefreshListener { refreshItems() }
    }

    override fun onResume() {
        loadingProgress.visibility = View.VISIBLE
        super.onResume()
        activity?.let { activity ->
            Thread({
                if (activity.database().countSecurity() == 0) {
                    Handler(Looper.getMainLooper()).post({
                        securities.visibility = View.INVISIBLE
                        loadingProgressMessage.visibility = View.VISIBLE
                    })
                    activity.database().initDatabase()
                }
                Handler(Looper.getMainLooper()).post {
                    filteringItems(mKeyword ?: "")
                    securities.visibility = View.VISIBLE
                    loadingProgress.visibility = View.INVISIBLE
                    loadingProgressMessage.visibility = View.INVISIBLE

                    items_fastscroller.updatePrimaryColor()
                    items_fastscroller.updateBubbleColors()
                    items_fastscroller.allowBubbleDisplay = context!!.config.showInfoBubble
//                    items_fastscroller.setViews(securities, null) {
//                        val item = storedItems.getOrNull(it)
//                        items_fastscroller.updateBubbleText(item?.getBubbleText() ?: "")
//                    }
                    securities.onGlobalLayout {
                        items_fastscroller.setScrollTo(securities.computeVerticalScrollOffset())
                    }
                }
            }).start()
        }
    }

    override fun refreshItems() {
//        openPath(currentPath)
//        items_swipe_refresh?.isRefreshing = false
    }
    
    fun filteringItems(keyword: String) {
        adapter?.let {
            mKeyword = keyword
            storedItems.clear()
            storedItems.addAll(it.selectAccounts(keyword))
            adapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("NewApi")
    private fun setUpGrid(securitiesView: MyRecyclerView) {
        with(securitiesView) {
            addItemDecoration(OffsetDecoration(context.resources
                    .getDimensionPixelSize(R.dimen.spacing_nano)))
            adapter = this@SecuritySelectionFragment.adapter
            activity?.let {
                beforeDrawing { it.supportStartPostponedEnterTransition() }    
            }

            items_fastscroller.allowBubbleDisplay = context!!.config.showInfoBubble
            items_fastscroller.setViews(securitiesView, null) {
                val item = storedItems.getOrNull(it)
                items_fastscroller.updateBubbleText(item?.getBubbleText() ?: "")
            }
            securities.onGlobalLayout {
                items_fastscroller.setScrollTo(securities.computeVerticalScrollOffset())
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startAccountDetailActivityWithTransition(activity: Activity, toolbar: View,
                                                         security: Security) {

        val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                *TransitionHelper.createSafeTransitionParticipants(activity,
                        false,
                        Pair(toolbar, getString(R.string.transition_category)))
        ).toBundle()

        // Start the activity with the participants, animating from one to the other.
        val startIntent = SecurityDetailActivity.getStartIntent(activity, security)
        startIntent.putExtra("sequence", security.sequence)
        ActivityCompat.startActivity(activity, startIntent, animationBundle)
    }
}