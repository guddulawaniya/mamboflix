package com.mamboflix.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.mamboflix.R
import com.mamboflix.model.DashboardHeaderMenuModel
import com.mamboflix.prefs.UserPref
import com.mamboflix.ui.activity.*
import com.mamboflix.ui.activity.login.LoginActivity
import com.mamboflix.ui.activity.managedevices.ManageDevicesActivity
import com.mamboflix.ui.activity.payment.PaymentBillingActivity
import com.mamboflix.ui.activity.profile.ProfileActivity
import com.mamboflix.ui.activity.settings.SettingsActivity
import com.mamboflix.ui.adapter.NavExpandableListAdapter
import com.mamboflix.databinding.FragmentSideMenuBinding

class SideMenuFragment : Fragment() {
    private lateinit var binding: FragmentSideMenuBinding
    private var headerList: MutableList<DashboardHeaderMenuModel>? = null
    private var listDataChild: HashMap<String, List<String>>? = null
    lateinit var userPref1: UserPref
    private lateinit var adapter: NavExpandableListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSideMenuBinding.inflate(inflater, container, false)

        userPref1 = UserPref(requireContext())
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        openDrawer()
        setNavigationData()

        return binding.root
    }

    private fun setNavigationData() {
        prepareNavMenuList()
        navMenuClickListener()
    }

    private fun prepareNavMenuList() {
        headerList = ArrayList()
        headerList!!.add(DashboardHeaderMenuModel(getString(R.string.notifications), false))
        binding.header.tvName0.text = "Welcome " + userPref1.getSubUserName()

        headerList!!.add(DashboardHeaderMenuModel(getString(R.string.myDownloads), false))
        headerList!!.add(DashboardHeaderMenuModel(getString(R.string.paymentandbilling), false))
        headerList!!.add(DashboardHeaderMenuModel(getString(R.string.manageDevices), false))
        headerList!!.add(DashboardHeaderMenuModel(getString(R.string.settings), false))
        headerList!!.add(DashboardHeaderMenuModel("FAQ", false))
        headerList!!.add(DashboardHeaderMenuModel(getString(R.string.help), false))
        headerList!!.add(DashboardHeaderMenuModel(getString(R.string.contactUs), false))
        headerList!!.add(DashboardHeaderMenuModel(getString(R.string.logout), false))

        listDataChild = HashMap()
        for (header in headerList!!) {
            listDataChild!![header.title] = ArrayList()
        }

        adapter = NavExpandableListAdapter(
            requireContext(),
            headerList as ArrayList<DashboardHeaderMenuModel>,
            listDataChild!!, userPref1.getNotificationdot()
        )
        binding.elvMenu.setAdapter(adapter)
    }

    private fun navMenuClickListener() {
        binding.header.ivUser.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        binding.elvMenu.setOnGroupClickListener { _, _, clickOnPosition, _ ->
            if (listDataChild!![headerList!![clickOnPosition].title]!!.isEmpty()) {
                when (clickOnPosition) {
                    0 -> startActivity(Intent(requireContext(), NotificationsActivity::class.java))
                    1 -> startActivity(Intent(requireContext(), MyDownloadActivity::class.java))
                    2 -> startActivity(Intent(requireContext(), PaymentBillingActivity::class.java))
                    3 -> startActivity(Intent(requireContext(), ManageDevicesActivity::class.java))
                    4 -> startActivity(Intent(requireContext(), SettingsActivity::class.java))
                    5 -> startActivity(Intent(requireContext(), FaqActivity::class.java))
                    6 -> startActivity(Intent(requireContext(), HelpActivity::class.java))
                    7 -> startActivity(Intent(requireContext(), ContacUsActivity::class.java))
                    8 -> {
                        userPref1.clearPref()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                    }
                }
            }
            false
        }

        binding.elvMenu.setOnChildClickListener { _, _, _, _, _ -> false }
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }
}
