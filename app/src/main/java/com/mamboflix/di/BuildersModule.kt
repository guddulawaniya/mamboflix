package com.mamboflix.di


import com.mamboflix.HomeActivity3.HomeActivity3
import com.mamboflix.ui.activity.*
import com.mamboflix.ui.activity.forgotpassword.ForgotPasswordActivity
import com.mamboflix.ui.activity.login.LoginActivity
import com.mamboflix.ui.activity.forgotpassword.OtpVerifyActivity
import com.mamboflix.ui.activity.forgotpassword.UpdatePasswordActivity
import com.mamboflix.ui.activity.initial.LanguageActivity
import com.mamboflix.ui.activity.initial.VideoSplashActivity
import com.mamboflix.ui.activity.managedevices.ManageDevicesActivity
import com.mamboflix.ui.activity.payment.PaymentBillingActivity
import com.mamboflix.ui.activity.payment.PaymentBillingDetailsActivity
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import com.mamboflix.ui.activity.filter.FilterActivity
import com.mamboflix.ui.activity.initial.SubUserActivity
import com.mamboflix.ui.activity.offersection.Offers
import com.mamboflix.ui.activity.payment.MakePaymentActivity
import com.mamboflix.ui.activity.player.PlayerActivity
import com.mamboflix.ui.activity.player.PlayerViewActivity
import com.mamboflix.ui.activity.profile.EditProfileActivity
import com.mamboflix.ui.activity.profile.ProfileActivity
import com.mamboflix.ui.activity.purchsedhistory.PurchasedHistory
import com.mamboflix.ui.activity.recentView.RecentViewActivity
import com.mamboflix.ui.activity.reviews.Reviews
import com.mamboflix.ui.activity.search.SearchActivity
import com.mamboflix.ui.activity.settings.SettingsActivity
import com.mamboflix.ui.activity.signup.AddProfileActivity
import com.mamboflix.ui.activity.signup.InterestActivity
import com.mamboflix.ui.activity.signup.SignUpActivity
import com.mamboflix.ui.activity.signup.WatchTypeActivity
import com.mamboflix.ui.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Binds all sub-components within the app.
 */

@Module
abstract class BuildersModule {
    /*@ContributesAndroidInjector
    abstract fun paymentPayUMoneyActivity(): PaymentPayUMoneyActivity*/

    @ContributesAndroidInjector
    abstract fun homeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun dashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun moviesTabFragment(): MoviesTabFragment

    @ContributesAndroidInjector
    abstract fun splaceScreen(): SplaceScreen

    @ContributesAndroidInjector
    abstract fun videoSplashActivity(): VideoSplashActivity

    @ContributesAndroidInjector
    abstract fun HomeActivity3(): HomeActivity3


    @ContributesAndroidInjector
    abstract fun tvShowsFragment(): TvShowsFragment

    @ContributesAndroidInjector
    abstract fun paymentBillingActivity(): PaymentBillingActivity

    @ContributesAndroidInjector
    abstract fun paymentBillingDetailsActivity(): PaymentBillingDetailsActivity

    @ContributesAndroidInjector
    abstract fun manageDevicesActivity(): ManageDevicesActivity




    @ContributesAndroidInjector
    abstract fun settingsActivity(): SettingsActivity


    @ContributesAndroidInjector
    abstract fun playerViewActivity(): PlayerViewActivity

    @ContributesAndroidInjector
    abstract fun recentViewActivity(): RecentViewActivity

    @ContributesAndroidInjector
    abstract fun languageActivity(): LanguageActivity

    @ContributesAndroidInjector
    abstract fun loginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun signUpActivity(): SignUpActivity

    @ContributesAndroidInjector
    abstract fun interestActivity(): InterestActivity

    @ContributesAndroidInjector
    abstract fun playerDetailsActivity(): ContentDetailsActivity


    @ContributesAndroidInjector
    abstract fun editProfileActivity(): EditProfileActivity


    @ContributesAndroidInjector
    abstract fun profileActivity(): ProfileActivity

    @ContributesAndroidInjector
    abstract fun contacUsActivity(): ContacUsActivity

    @ContributesAndroidInjector
    abstract fun faqActivity(): FaqActivity

    @ContributesAndroidInjector
    abstract fun myListActivity(): MyListActivity

    @ContributesAndroidInjector
    abstract fun myDownloadActivity(): MyDownloadActivity

    @ContributesAndroidInjector
    abstract fun notificationsActivity(): NotificationsActivity



    @ContributesAndroidInjector
    abstract fun playerMoreLikeFragment(): PlayerMoreLikeFragment

    @ContributesAndroidInjector
    abstract fun watchTypeActivity(): WatchTypeActivity

    @ContributesAndroidInjector
    abstract fun addProfileActivity(): AddProfileActivity


    @ContributesAndroidInjector
    abstract fun forgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector
    abstract fun myListFragment(): MyListFragment



    @ContributesAndroidInjector
    abstract fun otpVerifyActivity(): OtpVerifyActivity

    @ContributesAndroidInjector
    abstract fun updatePasswordActivity(): UpdatePasswordActivity


    @ContributesAndroidInjector
    abstract fun searchActivity(): SearchActivity


    @ContributesAndroidInjector
    abstract fun filterActivity(): FilterActivity


    @ContributesAndroidInjector
    abstract fun subUserActivity(): SubUserActivity

    @ContributesAndroidInjector
    abstract fun helpActivity(): HelpActivity

    @ContributesAndroidInjector
    abstract fun makePaymentActivity(): MakePaymentActivity

    @ContributesAndroidInjector
    abstract fun playerActivity(): PlayerActivity

    @ContributesAndroidInjector
    abstract fun offers(): Offers

    @ContributesAndroidInjector
    abstract fun purchasedHistory(): PurchasedHistory

    @ContributesAndroidInjector
    abstract fun testHLSActivity(): TestHLSActivity

    @ContributesAndroidInjector
    abstract fun reviews(): Reviews


}