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
import io.github.hanjoongcho.easypassword.databinding.ItemAccountBinding
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Account

/**
 * Created by Administrator on 2017-11-17.
 */

class AccountAdapter(
        private val activity: Activity,
        private val onItemClickListener: AdapterView.OnItemClickListener
) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    private val resources = activity.resources
    private val layoutInflater = LayoutInflater.from(activity)
    private var accounts = activity.database().getAccounts()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            account = accounts[position]
            executePendingBindings()
            setCategoryIcon(account, categoryIcon)
            setPasswordStrengthLevel(account, level1, level2, level3, level4, level5)
            with(accountTitle) {
                setTextColor(getColor(R.color.blackText))
            }
            with(accountSummary) {
                setTextColor(getColor(account.theme.textPrimaryColor))
                setBackgroundColor(getColor(account.theme.primaryColor))
            }
        }
        with(holder.itemView) {
            setBackgroundColor(getColor(accounts[position].theme.windowBackgroundColor))
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
        val imageRes = resources.getIdentifier("ic_${account.category}", "drawable", activity.packageName)
        icon.setImageResource(imageRes)
    }

    private fun setPasswordStrengthLevel(account: Account, level1: ImageView, level2: ImageView, level3: ImageView, level4: ImageView, level5: ImageView) {
        when (account.passwordStrengthLevel) {
            1 -> {
                setStrengthColor(level1, getColor(R.color.strength_bad))
                setStrengthColor(level2, getColor(R.color.strength_default))
                setStrengthColor(level3, getColor(R.color.strength_default))
                setStrengthColor(level4, getColor(R.color.strength_default))
                setStrengthColor(level5, getColor(R.color.strength_default))
            }
            2 -> {
                setStrengthColor(level1, getColor(R.color.strength_bad))
                setStrengthColor(level2, getColor(R.color.strength_bad))
                setStrengthColor(level3, getColor(R.color.strength_default))
                setStrengthColor(level4, getColor(R.color.strength_default))
                setStrengthColor(level5, getColor(R.color.strength_default))
            }
            3 -> {
                setStrengthColor(level1, getColor(R.color.strength_good))
                setStrengthColor(level2, getColor(R.color.strength_good))
                setStrengthColor(level3, getColor(R.color.strength_good))
                setStrengthColor(level4, getColor(R.color.strength_default))
                setStrengthColor(level5, getColor(R.color.strength_default))
            }
            4 -> {
                setStrengthColor(level1, getColor(R.color.strength_good))
                setStrengthColor(level2, getColor(R.color.strength_good))
                setStrengthColor(level3, getColor(R.color.strength_good))
                setStrengthColor(level4, getColor(R.color.strength_good))
                setStrengthColor(level5, getColor(R.color.strength_default))
            }
            5 -> {
                setStrengthColor(level1, getColor(R.color.strength_good))
                setStrengthColor(level2, getColor(R.color.strength_good))
                setStrengthColor(level3, getColor(R.color.strength_good))
                setStrengthColor(level4, getColor(R.color.strength_good))
                setStrengthColor(level5, getColor(R.color.strength_good))
            }
        }
    }

    private fun setStrengthColor(view: ImageView, colorId: Int) {
        view.setBackgroundColor(colorId)
    }

    /**
     * Convenience method for color loading.

     * @param colorRes The resource id of the color to load.
     *
     * @return The loaded color.
     */
    private fun getColor(@ColorRes colorRes: Int) = ContextCompat.getColor(activity, colorRes)

    class ViewHolder(val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root)
}