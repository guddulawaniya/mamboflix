package com.mamboflix.ui.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.ui.activity.payment.PaymentBillingDetailsActivity
import com.mamboflix.ui.activity.payment.model.PackageListModel
import java.util.*

class PaymentBillingAdapter(
    val context: Context,
    private var packageList: ArrayList<PackageListModel>, val country_code: String, val country_name: String,var is_subscribed:String
) : RecyclerView.Adapter<PaymentBillingAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    private var charStringFinal: String = ""

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var binding: LayoutPurchasingBinding = DataBindingUtil.bind(view)!!
        init {

            //view.setOnClickListener(this)
            binding.rlBottomCvView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            if (is_subscribed == "0"){
                context.startActivity(
                    Intent(context, PaymentBillingDetailsActivity::class.java)
                        .putExtra("model", packageList[position])
                        .putExtra("country_code", country_code)
                        .putExtra("country_name", country_name)
                )
            }else{
                detailAlert("You have already purchased a plan.")
            }

            /*  if (listener != null)
                  listener!!.onItemClick(adapterPosition, v,packageList.get(adapterPosition).package_type)*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_purchasing, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = packageList[position]

        /* if(position%2==0){
             holder.binding.viewLine.setBackgroundResource(R.color.paymentgrey)
             holder.binding.llRootItemProduct.setBackgroundResource(R.color.paymentgrey)
             holder.binding.rlBottomCvView.setBackgroundResource(R.color.paymentgrey)
         }
         else{
             holder.binding.viewLine.setBackgroundResource(R.color.paymentorange)
             holder.binding.llRootItemProduct.setBackgroundResource(R.color.paymentorange)
             holder.binding.rlBottomCvView.setBackgroundResource(R.color.paymentorange)
         }

         model.apply {
             holder.binding.apply {

                     tvTitle.text = share_limit+ " USER"
                 tvPrice.text = amount
                 tvValidity.text = packages
                 tvSubPrice.text = description

             }


         }*/
        if (country_code == "91") {
            holder.binding.tvCountryName.setText(country_name)
        } else  {
            holder.binding.tvCountryName.setText(country_name)
        }

        if (model.is_purchase == "1") {
            holder.binding.purchsed.visibility = VISIBLE
        } else {
            holder.binding.purchsed.visibility = GONE
        }
        model.apply {
            holder.binding.apply {
                packagetype.text = packages
                price.text = amount
            }
        }

        holder.binding.details.setOnClickListener(View.OnClickListener {
            detailAlert(model.details)
        })


//        notifyDataSetChanged()

    }


    override fun getItemCount(): Int {
        return packageList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View, type: String)
    }

    private fun detailAlert(text: String) {

        var cDialog = Dialog(context, R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogPaymentdetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_paymentdetails,
            null,
            false
        )
        cDialog!!.setContentView(bindingDialog.root)
        cDialog!!.setCancelable(false)
        cDialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        cDialog!!.show()
        bindingDialog.tvTitle.text = text

        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }

    }


}