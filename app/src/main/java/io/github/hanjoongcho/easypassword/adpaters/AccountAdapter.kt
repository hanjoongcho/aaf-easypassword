package io.github.hanjoongcho.easypassword.adpaters

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.activities.AccountAddActivity
import io.github.hanjoongcho.easypassword.databinding.ItemAccountBinding
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.models.Theme

/**
 * Created by Administrator on 2017-11-17.
 */

class AccountAdapter(
        private val activity: Activity,
        private val onItemClickListener: AdapterView.OnItemClickListener
) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    private val resources = activity.resources
    private val layoutInflater = LayoutInflater.from(activity)
    private var accounts = mutableListOf<Account>()

    fun selectAccounts() {
        accounts.clear()
        accounts.addAll(activity.database().selectAccountAll())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            account = accounts[position]
            executePendingBindings()
            setCategoryIcon(account, categoryIcon)
            AccountAddActivity.setPasswordStrengthLevel(activity, account.passwordStrengthLevel, included.level1, included.level2, included.level3, included.level4, included.level5)
            with(accountTitle) {
                setTextColor(AccountAddActivity.getColor(R.color.blackText, activity))
            }
            with(accountSummary) {
                setTextColor(AccountAddActivity.getColor(Theme.white.textPrimaryColor, activity))
                setBackgroundColor(AccountAddActivity.getColor(Theme.white.primaryColor, activity))
            }
        }
        with(holder.itemView) {
            setBackgroundColor(AccountAddActivity.getColor(Theme.white.windowBackgroundColor, activity))
            setOnClickListener {
                onItemClickListener.onItemClick(null, it, holder.adapterPosition, holder.itemId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(DataBindingUtil.inflate<ItemAccountBinding>(layoutInflater,
                    R.layout.item_account,
                    parent,
                    false))

    override fun getItemCount() = accounts.size

    fun getItem(position: Int): Account = accounts[position]

    private fun setCategoryIcon(account: Account, icon: ImageView) {
        val imageRes = resources.getIdentifier("ic_${account.category?.resourceName}", "drawable", activity.packageName)
        icon.setImageResource(imageRes)
    }

    class ViewHolder(val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root)
}