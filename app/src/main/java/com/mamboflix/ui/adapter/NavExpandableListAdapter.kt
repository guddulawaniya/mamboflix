package com.mamboflix.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import com.mamboflix.R
import com.mamboflix.databinding.MenuListItemBinding
import com.mamboflix.databinding.NavListSubMenuBinding
import com.mamboflix.model.DashboardHeaderMenuModel
import java.util.*

class NavExpandableListAdapter(
    private val context: Context,
    private val headerList: List<DashboardHeaderMenuModel>,
    private val listDataChild: HashMap<String, List<String>>,
  var  notificationdot: Boolean
) : BaseExpandableListAdapter() {

    private var headerBinding: MenuListItemBinding? = null
    private var childBinding: NavListSubMenuBinding? = null

    private val icons = intArrayOf(
        R.drawable.nav1,
        /*R.drawable.nav2,*/
        R.drawable.nav3,
        R.drawable.nav3,
        R.drawable.nav4,
        R.drawable.nav5,
      //  R.drawable.ic_outline_local_offer_24,
        R.drawable.nav6,
        R.drawable.nav7,
        R.drawable.nav8,
        R.drawable.nav9,
        R.drawable.nav10
    )

    override fun getGroupCount(): Int {
        return headerList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listDataChild[headerList[groupPosition].title]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return headerList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return this.listDataChild[headerList[groupPosition].title]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        b: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val inflater = this.context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Perform the binding
        headerBinding = convertView?.let { DataBindingUtil.getBinding(it) }
        if (headerBinding == null) {
            headerBinding =
                DataBindingUtil.inflate(inflater, R.layout.menu_list_item, parent, false)

        }

        if(groupPosition==0){
            if(notificationdot){
                headerBinding!!.dot.visibility=VISIBLE
            }else{
                headerBinding!!.dot.visibility= GONE
            }
        }
        Log.d("TAG", "getGroupView: ff"+notificationdot)
            if(notificationdot){
                headerBinding!!.whiteDot .visibility=VISIBLE
            }else{
                headerBinding!!.whiteDot.visibility= GONE
            }

        //
        headerBinding!!.tvHeaderName.text = headerList[groupPosition].title
        headerBinding!!.ivIcon.setImageResource(icons[groupPosition])

        if (groupPosition != 11
        ) headerBinding!!.ivPlus.visibility = View.VISIBLE else headerBinding!!.ivPlus.visibility =
            View.GONE

       /* if (b) headerBinding!!.ivPlus.setImageResource(R.drawable.arrow_down_black_24dp) else headerBinding!!.ivPlus.setImageResource(
            R.drawable.arrow_right_black_24dp
        )*/

        // Return the bound view
        return headerBinding!!.root
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        b: Boolean,
        convertView: View?,
        viewGroup: ViewGroup
    ): View? {


        if (listDataChild[headerList[groupPosition].title]!!.isNotEmpty()) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //childBinding = DataBindingUtil.getBinding(convertView)
            childBinding = convertView?.let { DataBindingUtil.getBinding(it) }

            if (childBinding == null)
                childBinding = DataBindingUtil.inflate(inflater, R.layout.nav_list_sub_menu, viewGroup, false)
            childBinding!!.tvSubMenu.text = getChild(groupPosition, childPosition) as String
            return childBinding!!.root
        }
        return null
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}