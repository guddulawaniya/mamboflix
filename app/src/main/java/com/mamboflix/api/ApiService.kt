package com.mamboflix.api

import com.google.gson.JsonObject
import com.mamboflix.model.CommonDataResponse
import com.mamboflix.model.ContactUsModel
import com.mamboflix.model.CreateWatchUserModel
import com.mamboflix.model.FaqMainModel
import com.mamboflix.model.FilterMovies.MoviesByCategoryModel
import com.mamboflix.model.ForgotPasswordModel
import com.mamboflix.model.NotificationModel
import com.mamboflix.model.ResumeVideoModel
import com.mamboflix.model.SubsrciptionModel
import com.mamboflix.model.SwitchProfileData
import com.mamboflix.model.UserDataProfileData
import com.mamboflix.model.Userdata
import com.mamboflix.model.VideoSplashModel
import com.mamboflix.model.contentdetails.MoreLikeThisClassData
import com.mamboflix.model.contentdetails.MoreLikeThisData
import com.mamboflix.model.contentdetails.MovieContentDetailsModel
import com.mamboflix.model.contentdetails.RelatedDetailsBaseModel
import com.mamboflix.model.hometab.HomeTabModel
import com.mamboflix.model.interest.InterestModel
import com.mamboflix.model.managedevice.ManageDeviceModel
import com.mamboflix.model.movietab.MovieTabModel
import com.mamboflix.model.mylist.MyListModel
import com.mamboflix.model.recentView.RecentViewModel
import com.mamboflix.model.searchContent.SearchContentModel
import com.mamboflix.model.sendOTPModel.SendOTPData
import com.mamboflix.model.shows.ShowsTabModel
import com.mamboflix.model.verify_otp.VerifyOTPmodelClass
import com.mamboflix.ui.activity.offersection.offers.OffersApplied
import com.mamboflix.ui.activity.offersection.offers.OffersList
import com.mamboflix.ui.activity.payment.model.CommonPaymentResponse
import com.mamboflix.ui.activity.payment.model.MobilePaymentModel
import com.mamboflix.ui.activity.payment.model.PackageListModel
import com.mamboflix.ui.activity.payment.model.PaymentStatus
import com.mamboflix.ui.activity.payment.model.PaymentUrlModel
import com.mamboflix.ui.activity.payment.model.UnsubscribResponse
import com.mamboflix.ui.activity.payment.model.newPayment_module
import com.mamboflix.ui.activity.purchsedhistory.paymenthistory.history
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url
import rx.Observable

interface ApiService {

    /* @Headers(value = ["Accept: application/json",
         "Content-type:application/json"])*/

    //Login
    @POST("login")
    fun callloginAPI(@Body request: JsonObject): Observable<CommonDataResponse<Userdata>>

    //Signup
    @POST("register")
    fun callSignUpAPI(@Body request: JsonObject): Observable<CommonDataResponse<Userdata>>


    //http://162.214.65.129/~jeevan/mamboflix/api/send_otp
    @POST("send_otp")
    fun callSendOtpAPI(@Body request: JsonObject): Observable<CommonDataResponse<Userdata>>

    //Update
    @Multipart
    @POST("update_profile")
    fun callUpdateUserAPI(
        @Header("Authorization") token: String,
        @Part profile_image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("country_code") country_code: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
    ): Observable<CommonDataResponse<Userdata>>

    //update_password
    @POST("update_password")
    @FormUrlEncoded
    fun callUpdatePasswordApi(
        @Header("Authorization") token: String,
        @Field("password") password: String,
        @Field("new_password") new_password: String,
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_movies
    @POST("get_movies")
    @FormUrlEncoded
    fun callGetMovieApi(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String,
        @Field("country_short_code") country_short_code: String
    ): Observable<CommonDataResponse<MovieTabModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/logout
    @POST("logout")
    @FormUrlEncoded
    fun callLogoutApi(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/views_update
    @POST("views_update")
    @FormUrlEncoded
    fun views_updateApi(
        @Header("Authorization") token: String,
        @Field("content_id") content_id: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/categories
    @POST("categories")
    @FormUrlEncoded
    fun callCategoriesAPI(
        @Header("Authorization") token: String,
        @Field("lang_type") lang_type: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<ArrayList<InterestModel>>>

    //http://162.214.65.129/~jeevan/mamboflix/api/user_details/get_notification/token_o/gfdgdgd
    /*@GET("get_notification/token_o/{token_o}")
    fun callNotificationAPI(@Header("Authorization") token:String,
    @Path("token_o") device_token: String): Observable<CommonDataResponse<ArrayList<NotificationModel>>>*/

    @POST("get_notification")
    @FormUrlEncoded
    fun callNotificationAPI(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<ArrayList<NotificationModel>>>

    //Clear notification
    @POST("clear_notification")
    @FormUrlEncoded
    fun callClearNotificationAPI(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/user_categories
    @POST("user_categories")
    fun callSubmitCategoryAPI(
        @Header("Authorization") token: String,
        @Body request: JsonObject
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/create_watching_user
    @POST("create_watching_user")
    @Multipart
    fun callCreateWatchUserApi(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("device_token") device_token: RequestBody,
        @Part("type") type: RequestBody,
        @Part("lang_type") lang_type: RequestBody
    ): Observable<CommonDataResponse<CreateWatchUserModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_watching_user/token_o/{device_token}
    /* @GET("get_watching_user/token_o/{token_o}")
     fun callGetWatchingUserAPI(@Header("Authorization") token:String,
     @Path("token_o") device_token: String): Observable<CommonDataResponse<ManageDeviceModel>>*/

    @POST("get_watching_user")
    @FormUrlEncoded
    fun callGetWatchingUserAPI(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<ManageDeviceModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_dashboard
    @POST("get_dashboard")
    @FormUrlEncoded
    fun callDashboardApi(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String,
        @Field("country_short_code") country_short_code: String
    ): Observable<CommonDataResponse<HomeTabModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_faq
    @POST("get_faq")
    @FormUrlEncoded
    fun callGetFaqAPI(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<List<FaqMainModel>>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_webcontent
    @POST("get_webcontent")
    @FormUrlEncoded
    fun callGetContentApi(
        @Header("Authorization") token: String,
        @Field("type") type: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<ContactUsModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_movies_by_cat
    @POST("get_movies_by_cat")
    @FormUrlEncoded
    fun callGetMovieByCategoryApi(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("cat_id") cat_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<MoviesByCategoryModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_shows
    @POST("get_shows")
    @FormUrlEncoded
    fun callGetShowApi(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String,
        @Field("country_short_code") country_short_code: String
    ): Observable<CommonDataResponse<ShowsTabModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_shows_by_cat
    @POST("get_shows_by_cat")
    @FormUrlEncoded
    fun callGetShowByCategoryApi(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("cat_id") cat_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<MoviesByCategoryModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/make_watch_history
    @POST("make_watch_history")
    @FormUrlEncoded
    fun callMakeWatchHistoryApi(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("content_id") content_id: String,
        @Field("watch_duration") watch_duration: String,
        @Field("device_token") device_token: String,
        @Field("type") type: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/edit_sub_user
    @POST("edit_sub_user")
    @Multipart
    fun callEditSubUserApi(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("sub_user_id") sub_user_id: RequestBody,
        @Part("account_type") account_type: RequestBody,
        @Part("name") name: RequestBody,
        @Part("device_token") device_token: RequestBody,
        @Part("lang_type") lang_type: RequestBody
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/delete_sub_user
    @POST("delete_sub_user")
    @FormUrlEncoded
    fun callDeleteSubUserApi(
        @Header("Authorization") token: String,
        @Field("sub_user_id") sub_user_id: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    //edit_particular_device
    @POST("edit_particular_device")
    @FormUrlEncoded
    fun callEditDeviceApi(
        @Header("Authorization") token: String,
        @Field("device_name") device_name: String,
        @Field("device_id") device_id: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<ManageDeviceModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/user_details/token_o/gfdgdgd
    @GET("user_details/token_o/{token_o}")
    fun callUserDetailsAPI(@Path("token_o") device_token: String): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_particular_watching_user
    @POST("get_particular_watching_user")
    @FormUrlEncoded
    fun callGetParticularUserApi(
        @Header("Authorization") token: String,
        @Field("sub_user_id") sub_user_id: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_particular_device_detail
    @POST("get_particular_device_detail")
    @FormUrlEncoded
    fun callGetParticularDeviceApi(
        @Header("Authorization") token: String,
        @Field("device_id") device_id: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/delete_particular_device
    @POST("delete_particular_device")
    @FormUrlEncoded
    fun callDeleteParticularDeviceApi(
        @Header("Authorization") token: String,
        @Field("device_id") device_id: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/get_watch_history
    @POST("get_watch_history")
    @FormUrlEncoded
    fun callGetWatchHistoryApi(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<List<RecentViewModel>>>

    //http://162.214.65.129/~jeevan/mamboflix/api/search_content
    @POST("search_content")
    @FormUrlEncoded
    fun callSearchContentApi(
        @Header("Authorization") token: String,
        @Field("keyword") keyword: String,
        @Field("watch_id") watch_id: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/rating_content
    @POST("rating_content")
    @FormUrlEncoded
    fun callRatingContentApi(
        @Header("Authorization") token: String,
        @Field("content_id") content_id: String,
        @Field("rating") rating: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/report_content
    @POST("report_content")
    @FormUrlEncoded
    fun callReportContentApi(
        @Header("Authorization") token: String,
        @Field("content_id") content_id: String,
        @Field("reason") reason: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/make_like
    @POST("make_like")
    @FormUrlEncoded
    fun callMakeLikeApi(
        @Header("Authorization") token: String,
        @Field("sub_user_id") sub_user_id: String,
        @Field("cat_id") cat_id: String,
        @Field("content_id") content_id: String,
        @Field("status") status: String,
        @Field("type") type: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    @POST("resume_video")
    @FormUrlEncoded
    fun callResumeVideoApi(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String,
        @Field("content_id") content_id: String,
        @Field("type") type: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<ResumeVideoModel>>

    //callDownloadApi
    @POST("download")
    @FormUrlEncoded
    fun callDownloadApi(
        @Header("Authorization") token: String,
        @Field("sub_user_id") sub_user_id: String,
        @Field("cat_id") cat_id: String,
        @Field("content_id") content_id: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/access_content
    //Details page
    @POST("access_content")
    @FormUrlEncoded
    fun callAccessContentApi(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("content_id") content_id: String,
        @Field("ep_id") episode_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<MovieContentDetailsModel>>

    //http://162.214.65.129/~jeevan/mamboflix/api/make_list
    @POST("make_list")
    @FormUrlEncoded
    fun callMakeListApi(
        @Header("Authorization") token: String,
        @Field("sub_user_id") sub_user_id: String,
        @Field("content_id") content_id: String,
        @Field("cat_id") cat_id: String,
        @Field("status") status: String,
        /*  @Field("type") type : String,*/
        @Field("device_token") device_token: String
//        @Field("episode_id") episode_id: String
    ): Observable<CommonDataResponse<Any?>>

    //http://162.214.65.129/~jeevan/mamboflix/api/my_list
    @FormUrlEncoded
    @POST("my_list")
    fun callMyListAPI(
        @Header("Authorization") token: String,
        @Field("sub_user_id") sub_user_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String,
        @Field("country_short_code") country_short_code: String
    ): Observable<CommonDataResponse<List<MyListModel>>>

    @FormUrlEncoded
    @POST("access_related_content")
    fun callAccessRelatedAPI(
        @Header("Authorization") token: String,
        @Field("related_content_id") related_content_id: String,
        @Field("device_token") device_token: String,
        @Field("content_id") content_id: String,
        @Field("seasion_id") seasion_id: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<RelatedDetailsBaseModel>>

    @FormUrlEncoded
    @POST("more_like_this")
    fun more_like_this(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("content_id") content_id: String,
        @Field("session_id") session_id: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<MoreLikeThisClassData>>

    //logout  user_id
    /*    @POST("logout")
       @FormUrlEncoded
        fun callLogOutapi( @Field("user_id") user_id: String): Observable<CommonDataResponse<Any?>>*/

    @FormUrlEncoded
    @POST("search_data")
    fun callSearchContentAPI(
        @Header("Authorization") token: String,
        @Field("content_type") content_type: String,
        @Field("keyword") keyword: String,
        @Field("watch_id") watch_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<List<SearchContentModel>>>

    @FormUrlEncoded
    @POST("parental_control_update")
    fun callParentalControlAPI(
        @Header("Authorization") token: String,
//        @Header("sub_user_id") sub_user_id: String,
        @Field("type") type: String,
        @Field("who_watch_id") watch_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<Any>>

    //  logout  user_id
    @GET
    @Streaming
    fun callDownLoadFile(@Url url: String): Observable<retrofit2.Response<ResponseBody>>

    //get_packages
    @FormUrlEncoded
    @POST("get_packages")
    fun callGetPackageAPI(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<ArrayList<PackageListModel>>>

    @FormUrlEncoded
    @POST("help")
    fun callHeldAPI(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("username") username: String,
        @Field("subject") subject: String,
        @Field("message") message: String
    ): Observable<CommonDataResponse<Any>>

    @FormUrlEncoded
    @POST("banner_image")
    fun callBannerImageAPI(
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<VideoSplashModel>>

    //forget_password
    @FormUrlEncoded
    @POST("password_update")
    fun callForgotAPI(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<CommonDataResponse<Any>>

    //forget_password
    @FormUrlEncoded
    @POST("forget_password")
    fun callgetotp(
        @Field("type") type: Int,
        @Field("username") username: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<ForgotPasswordModel>>


    @FormUrlEncoded
    @POST("payment_process")
    fun fetchPaymentUrl(
        @Header("Authorization") token: String,
        @Field("package_id") package_id: String
    ): Observable<newPayment_module>

    @FormUrlEncoded
    @POST("payment_callback")
    fun paymentCallback(
        @Header("Authorization") token: String,
        @Field("package_id") package_id: String,
        @Field("order_id") order_id : String
    ): Observable<newPayment_module>

    //payment
    @FormUrlEncoded
    @POST("payment")
    fun callPaymentAPIAPI(
        @Field("user_id") user_id: String,
        @Field("email") email: String,
        @Field("coupen_off") coupen_off: String,
        @Field("packages") packages: String,
        @Field("amount") amount: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("billing_firstname") billing_firstname: String,
        @Field("billing_lastname") billing_lastname: String,
        @Field("billing_address_1") billing_address_1: String,
        @Field("billing_city") billing_city: String,
        @Field("billing_state_or_region") billing_state_or_region: String,
        @Field("billing_postcode_or_pobox") billing_postcode_or_pobox: String,
        @Field("billing_country") billing_country: String,
        @Field("billing_phone") billing_phone: String,
        @Field("shipping_firstname") shipping_firstname: String,
        @Field("shipping_lastname") shipping_lastname: String,
        @Field("shipping_address_2") shipping_address_2: String,
        @Field("shipping_city") shipping_city: String,
        @Field("shipping_state_or_region") shipping_state_or_region: String,
        @Field("shipping_postcode_or_pobox") shipping_postcode_or_pobox: String,
        @Field("shipping_country") shipping_country: String,
        @Field("shipping_phone") shipping_phone: String,
        @Field("no_of_items") no_of_items: String,
        @Field("device_token") device_token: String
    ): Observable<CommonPaymentResponse<ArrayList<PaymentUrlModel>>>

    @FormUrlEncoded
    @POST("mobile_payment")
    fun callmobilepayment(
        @Field("email") email: String,
        @Field("packages") packages: String,
        @Field("amount") amount: String,
        @Field("no_of_items") no_of_items: String,
        @Field("name") name: String,
        @Field("coupen_off") coupen_off: String,
        @Field("user_id") user_id: String,
        @Field("phone") phone: String
    ): Observable<CommonDataResponse<MobilePaymentModel>>


    //update_password
    @POST("check_user_existence")
    @FormUrlEncoded
    fun callValidationApi(
        @Field("email") email: String,
        @Field("mobile") mobile: String
    ): Observable<CommonDataResponse<Any?>>

    @POST("get_offer_list")
    @FormUrlEncoded
    fun callGetOfferList(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<List<OffersList>>>

    // http://162.214.65.129/~jeevan/mamboflix1/api/get_packages_after_coupon_apply
    @POST("get_packages_after_coupon_apply")
    @FormUrlEncoded
    fun callGetPackageListAfterCouponApply(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("coupon_code") coupon_code: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<ArrayList<PackageListModel>>>

    @POST("apply_offer")
    @FormUrlEncoded
    fun callapplyoffer(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String,
        @Field("offer_id") offer_id: String,
        @Field("package_id") package_id: String
    ): Observable<CommonDataResponse<OffersApplied>>

    @POST("get_payment_history")
    @FormUrlEncoded
    fun callGetpaymenthistory(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<List<history>>>

    @POST("unsubscribed_plan")
    @FormUrlEncoded
    fun unsubscribed_plan(
        @Header("Authorization") token: String,
        @Field("purchase_id") purchase_id: String
//        @Field("purchase_his") purchase_his: String
    ): Observable<UnsubscribResponse>

    @POST("rating_content")
    @FormUrlEncoded
    fun callsubmitRatingList(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String,
        @Field("content_id") content_id: String,
        @Field("content_type") content_type: String,
        @Field("rating") rating: String,
        @Field("description") description: String
    ): Observable<CommonDataResponse<com.mamboflix.ui.activity.reviews.reviewsmodel.Reviews>>

    @POST("rating_content_list")
    @FormUrlEncoded
    fun callGetRatingList(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String,
        @Field("content_id") content_id: String,
        @Field("content_type") content_type: String
    ): Observable<CommonDataResponse<com.mamboflix.ui.activity.reviews.reviewsmodel.Reviews>>

    @GET("plan_details")
    fun callplandetails(
        @Header("Authorization") token: String
    ): Observable<CommonDataResponse<SubsrciptionModel>>

    //  https://mamboflix.tv/api/get_payment_status
    @POST("get_payment_status")
    @FormUrlEncoded
    fun callgetpaymentstatus(@Field("user_id") user_id: String): Observable<CommonDataResponse<PaymentStatus>>

    @POST("send_otp")
    @FormUrlEncoded
    fun send_otp(
        @Field("type") type: String,
        @Field("mobile") username: String,
        @Field("country_code") country_code: String,
        @Field("email") email: String,
        @Field("lang_type") lang_type: String
    ): Observable<CommonDataResponse<SendOTPData>>

    @POST("verify_otp")
    @FormUrlEncoded
    fun verify_otp(
        @Field("username") username: String,
        @Field("otp") otp: String
    ): Observable<CommonDataResponse<VerifyOTPmodelClass>>

    @POST("userData")
    @FormUrlEncoded
    fun userData(
        @Header("Authorization") token: String,
        @Field("device_token") device_token: String
    ): Observable<CommonDataResponse<UserDataProfileData>>

    @POST("switch_profile")
    @FormUrlEncoded
    fun switch_profile(
        @Field("id") id: String
    ): Observable<CommonDataResponse<SwitchProfileData>>

    @POST("read_notification")
    @FormUrlEncoded
    fun read_notification(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String,
        @Field("type") type: String
    ): Observable<CommonDataResponse<UserDataProfileData>>

    @POST("more_like_this")
    @FormUrlEncoded
    fun more_like_this(
        @Header("Authorization") token: String,
        @Field("watch_id") watch_id: String,
        @Field("content_id") content_id: String,
        @Field("episode_id") episode_id: String,
        @Field("device_token") device_token: String,
        @Field("lang_type") lang_type: String,
        @Field("seasion_id") seasion_id: String
    ): Observable<CommonDataResponse<MoreLikeThisData>>


//  @POST("send_otp")
//    @FormUrlEncoded
//    suspend fun send_otp(
//        @Field("type") type: String,
//        @Field("username") username: String,
//        @Field("lang_type") lang_type: String
//    ): retrofit2. Response<SendOTPModelClass>

}