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
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.simplemobiletools.commons.dialogs.StoragePickerDialog
import com.simplemobiletools.commons.models.FileDirItem
import com.simplemobiletools.commons.views.Breadcrumbs
import com.simplemobiletools.commons.views.MyRecyclerView
import io.github.hanjoongcho.commons.helpers.TransitionHelper
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.activities.SecurityAddActivity
import io.github.hanjoongcho.easypassword.activities.SecurityDetailActivity
import io.github.hanjoongcho.easypassword.adpaters.SecurityAdapter
import io.github.hanjoongcho.easypassword.helper.beforeDrawing
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Security
import io.github.hanjoongcho.easypassword.widget.OffsetDecoration
import kotlinx.android.synthetic.main.fragment_securities.*

/**
 * Created by CHO HANJOONG on 2017-11-17.
 */

class SecuritySelectionFragment : Fragment(), SecurityAdapter.ItemOperationsListener, Breadcrumbs.BreadcrumbsListener {
    private var mKeyword: String? = ""

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

        items_fastscroller.setViews(securities, items_swipe_refresh) {
//            val item = storedItems.getOrNull(it)
//            items_fastscroller.updateBubbleText(item?.getBubbleText() ?: "")
        }

        items_swipe_refresh.setOnRefreshListener { refreshItems() }
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
                    adapter?.selectAccounts(mKeyword ?: "")
                    adapter?.notifyDataSetChanged()
                    securities.visibility = View.VISIBLE
                    loadingProgress.visibility = View.INVISIBLE
                    loadingProgressMessage.visibility = View.INVISIBLE
                }
            }).start()
        }
    }

    override fun refreshItems() {
//        openPath(currentPath)
        items_swipe_refresh?.isRefreshing = false
    }

    override fun breadcrumbClicked(id: Int) {
        if (id == 0) {
//            StoragePickerDialog(activity!!, currentPath) {
//                openPath(it)
//            }
        } else {
//            val item = breadcrumbs.getChildAt(id).tag as FileDirItem
//            openPath(item.path)
        }
    }
    
    fun filteringItems(keyword: String) {
        mKeyword = keyword
        adapter?.selectAccounts(keyword)
        adapter?.notifyDataSetChanged()
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
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        if (requestCode == REQUEST_CATEGORY && resultCode == R.id.solved) {
//            adapter?.notifyItemChanged(data.getStringExtra(JsonAttributes.ID))
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }

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