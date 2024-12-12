package com.mamboflix.ui;


public class Constants {

    //For Learn tab
    public static int CLASS_ID = -1;

    public static final String ACTIVITY_TYPE = "activityType";
    public static final String MODULE_TYPE = "moduleType";
    public static final String Timetable_INTENT = "timetableOf";
    public static final String SELECTED_TAB = "selectedTab";
    public static final String CLASSES = "classList";
    public static final String DEFAULT_CLASS = "Select Class";
    public static final String DEFAULT_GENDER = "Select Gender";
    public static final String WEB_URL = "webUrl";
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
    public static final String STUDENT_ID = "studentId";
    public static final String DEFAULT_STATUS = "Select Status";
    public static final String DEFAULT_PAYMENT_MODE = "Select Payment Mode";

    //Date formats
    public static final String DATE_FORMAT_DMY = "dd MMM, yyyy";
    public static final String DATE_FORMAT_DM = "dd MM";
    public static final String DATE_FORMAT_MDY = "MM/dd/yyyy";
    public static final String DATE_FORMAT_DMY2 = "dd-MM-yyyy";
    public static final String DATE_FORMAT_DMY3 = "dd/MM/yyyy";

    public class LearnTabSections {
        public static final String SUBJECT = "subject";
        public static final String SHARE = "share";
        public static final String CYBER_SAFETY = "cybersafety";

    }

    public static String FIREBASEDB = "spt";
    // Array of strings storing country names
    public static String USERID = "userId";
    public static String TEMP_TOKEN_ID = "tempTokenId";
    public static String SESSION_TOKEN_ID = "sessionTokenId";
    public static String DEVICE_TYPE = "1";

    public static String IMAGE_BASE_URL = "http://mobile.scientificstudy.in";
    public static String BUILD_TYPE = "Production";
    public static String FILE_AUTHORITY = "com.jeannypr.scientificstudy.file";
    public static String ACTIVITY_UPLOAD_URL = IMAGE_BASE_URL + "/api/class/activity/upload";
    public static String HTTPS = "https://";
    public static String SUBDOMAIN = ".scientificstudy.in";

    public static int TOTAL_ATTACHMENTS_IN_NEWS_NOTICE_CREATION = 1;
    public static float ATTACHMENT_SIZE_IN_NEWS_NOTICE_CREATION = (float) 10.0;

    //Urls
    public static String UNABLE_TO_LOGIN_HELP_URL = "https://www.scientificstudy.in/help/d/login.html";
    public static String ENTER_KEY_HELP_URL = "https://www.scientificstudy.in/help/d/key.html";
    public static String CHAT_HELP_URL = "https://www.scientificstudy.in/help/Chattmp.html";
    /*public static String SIGN_UP_URL = "http://www.scientificstudy.in/demo/sales-demo-Get-buy-ERPthroughApp.php";*/
    public static String ABOUT_US_URL = "https://www.scientificstudy.in/";
    public static String TERMS_CONDITIONS_URL = "https://www.scientificstudy.in/Spt_confirmation/termAndCondition.php";
    public static String SHARE_EARN_URL = "https://www.scientificstudy.in/Spt_confirmation/shareAndEarn.php";
    public static String PARENT_HELP_URL = "https://www.scientificstudy.in/help/d/p.html";
    public static String ADMIN_TEACHER_HELP_URL = "https://www.scientificstudy.in/help/d/t.html";
    public static String PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=";
    public static String SIGN_UP_URL = "https://www.scientificstudy.in/create-account.php";

    public static String APP_PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=com.jeannypr.scientificstudy";
    public static long CACHE_EXPIRATION_FCM_CONFIG = 3600;/*in seconds*/
    public static long TRACK_LOCATION_INTERVAL = 120000;  //120000 - in milliseconds
    /*  public static long TRACK_LOCATION_INTERVAL = 60000;*//*in milliseconds*/
    public static String DATE_CHANGE_INTENT = "dateChangeCustomIntent";
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    public static final int AUTOMATICALLY_CLOSE_JOURNEY_INTERVAL = 12;

    //Analytics
    public static final int DEVICE_LOG_INTERVAL = 7;
    public static final int DEVICE_ACCESS_LOG_INTERVAL = 2;

    public static final int THRESHOLD_MARKS_ENTRY = 40;

    public static final String DEFAULT_MOBILE = "0000000000";

    public class PermissionRequestCode {
        public static final int LOCATION = 300;
        public static final int RREAD_WRITE_STORAGE = 300;
    }

    public class Directory {
        public static final String Base = "SPT";
    }

    public class Rcode {
        public static final int OK = 100;
        public static final int NORECORDS = 502;
        public static final int ERROR = 101;
    }

    public class SuccessCode {
        public static final int SUCCESS = 200;
        public static final int FAILED = 400;
    }

    public class Role {
        public static final String ADMIN = "Admin";
        public static final String PARENT = "Parent";
        public static final String TEACHER = "Teacher";
        //TODO : set role of driver as per api
        public static final String DRIVER = "Driver";
    }

    public class Gender {
        public static final String MALE = "Male";
        public static final String FEMALE = "Female";
        public static final String OTHER = "Other";
    }

    public class ChatRoomType {
        public static final String INDIVIDUAL = "1-1";
        public static final String GROUP = ">2";
    }

    public class Chat {
        public static final String CHATROOM = "ChatRoom";
        public static final String USERS = "ChatUsers";
        public static final String MESSAGES = "ChatMessages";
    }

    public class ChatMessageStatus {
        public static final int PENDING = 0;
        public static final int SENT = 1;
        public static final int READ = 2;
    }

    public class ChatGroupTab {
        public static final String STAFF = "teacher";
        public static final String CLASS = "classes";
    }

    public class MODE {
        public static final String ALL = "all";
        public static final String ASSIGNED = "assigned";
    }

    public class PostType {
        public static final String NEWS = "news";
        public static final String NOTICE = "notice";
        public static final String EVENT = "event";
        public static final String GENERAL = "general";
    }


    public class PaymentStatus {
        public static final String PAID = "Paid";
        public static final String DUE = "Due";
        public static final String OVERDUE = "OverDue";
    }

    public class AttachmentType {
        public static final String IMAGE = "image";
        public static final String DOCUMENT = "document";
    }

    public class Attendance {
        public static final int ABSENT = 0;
        public static final int PRESENT = 1;
        public static final int HALFDAY = 2;
    }

    public class DiaryType {
        public static final int Homework = 1;
        public static final int Classwork = 2;

    }

    public class DiaryTypeName {
        public static final String Homework = "Homework";
        public static final String Classwork = "Classwork";

    }

    public class ActivityAttachmentType {
        public static final int TEXT = 0;
        public static final int LINK = 1;
        public static final int MEDIA = 2;
        public static final int FILE = 3;
    }

    public class FileType {
        //IMAGE
        public static final String PNG = "png";
        public static final String JPG = "jpg";
        public static final String JPEG = "jpeg";
        public static final String BMP = "bmp";

        public static final String COD = "cod";
        public static final String GIF = "gif";
        public static final String IEF = "ief";
        public static final String JPE = "jpe";
        public static final String JFIF = "jfif";
        public static final String SVG = "svg";
        public static final String TIF = "tif";
        public static final String TIFF = "tiff";
        public static final String RAS = "ras";
        public static final String CMX = "cmx";
        public static final String ICO = "ico";
        public static final String PNM = "pnm";
        public static final String PBM = "pbm";
        public static final String PGM = "pgm";
        public static final String RGB = "rgb";
        public static final String XBM = "xbm";
        public static final String XPM = "xpm";
        public static final String XWD = "xwd";

        //AUDIO
        public static final String M4A = "m4a";
        public static final String AMR = "amr";
        public static final String GPP = "3gpp";
        public static final String MP3 = "mp3";

        public static final String AU = "au";
        public static final String SND = "snd";
        public static final String RMI = "rmi";
        public static final String MID = "mid";
        public static final String AIF = "aif";
        public static final String AIFC = "aifc";
        public static final String AIFF = "aiff";
        public static final String M3U = "m3u";
        public static final String RA = "ra";
        public static final String RAM = "ram";
        public static final String WAV = "wav";


        //TEXT
        public static final String PDF = "pdf";
        public static final String DOC = "doc";
        public static final String DOCX = "docx";
        public static final String TXT = "txt";

        //VIDEO
        public static final String MP4 = "mp4";
        public static final String AVI = "avi";
        public static final String FLV = "flv";
        public static final String M4V = "m4v";

        public static final String MP2 = "mp2";
        public static final String MPA = "mpa";
        public static final String MPE = "mpe";
        public static final String MPG = "mpg";
        public static final String MPEG = "mpeg";
        public static final String MPV2 = "mpv2";
        public static final String MOV = "mov";
        public static final String QT = "qt";
        public static final String LSF = "lsf";
        public static final String LSX = "lsx";
        public static final String ASR = "asr";
        public static final String ASF = "asf";
        public static final String ASX = "asx";

    }

    public class DayOfWeek {
        //IMAGE
        public static final String MONDAY = "Monday";
        public static final String TUESDAY = "Tuesday";

    }

    public class TypeOfCreatedPost {
        public static final int NEWS = 1;
        public static final int NOTICE = 2;
    }

    public class PostAudienceNames {
        public static final String SCHOOL = "School";
        public static final String PARENT = "Parent";
        public static final String TEACHER = "Teacher";
        public static final String STUDENT = "Student";
        public static final String ALUMNI = "Alumni";
    }

    public class PostAudienceValues {
        public static final int SCHOOL = 4;
        public static final int PARENT = 3;
        public static final int TEACHER = 2;
        public static final int STUDENT = 1;
        public static final int ALUMNI = 5;
    }

    public class UploadAttachmentRcode {
        public static final int OK = 100;
        public static final int SERVER_ERROR = 101;
        public static final int NULL = 102;
        public static final int EXCEPTION = 103;
    }

    public class InventoryReportType {
        public static final String PURCHASE = "Purchase";
        public static final String SALE = "Sale";
        public static final String PAYMENT_RECEIPT = "payment_receipt";
        public static final String PAYMENT = "Payment";
        public static final String RECEIPT = "Receipt";
        public static final String REPORT = "Report";
    }

    public class InventoryTabs {
        public static final String PAYMENT = "Payment";
        public static final String RECEIPT = "Receipt";
        public static final String PURCHASE = "Purchase";
        public static final String SALE = "Sale";
        public static final String REPORT = "Report";
    }

    public class ImageTakenFrom {
        public static final String CAMERA = "camera";
        public static final String GALLERY = "gallery";
    }

    public class UploadImageContext {
        public static final String STUDENT = "StudentProfile";
        public static final String TEACHER = "TeacherProfile";
    }

    public class DashboardBanners {
        public static final String INVENTORY = "inventory";
        public static final String WALL = "wall";
        public static final String TIMETABLE = "timetable";
        public static final String TRACK_LOCATION = "track_location";
        public static final String UPLOAD_PROFILE_PIC = "upload_profile_pic";
        public static final String REPORT_CARD = "report card";
    }

    public class UserStatus {
        public static final int ACTIVE = 1;
        public static final int INACTIVE = 0;
    }

    public class ConnectivityType {
        public static final String WIFI = "wifi";
        public static final String MOBILE_DATA = "mobile_data";
    }

    public class NotificationActionsString {
        public static final String REPLY = "Reply";
        public static final String CANCEL = "Cancel";
    }

    public class NotificationFor {
        public static final String CHAT = "chat";
        public static final String TRANSPORT = "transport";
        public static final String CLASSWORK = "classwork";
        public static final String HOMEWORK = "homework";
        public static final String LEAVE_REQUESTER = "leave_requester";
        public static final String LEAVE_APPROVER = "leave_approver";
        public static final String BIOMETERICS = "Biometerics";

    }

    public class Platform {
        public static final String ANDROID = "A";
        public static final String IOS = "I";
        public static final String WEB = "W";
    }

    //  0 - logout| 1 - login
    public class LogType {
        public static final int LOGIN = 1;
        public static final int LOGOUT = 0;
    }

    public class LeaveDayTypeAlias {
        public static final String FULLDAY = "F";
        public static final String HALFDAY = "H";
    }

    public class LeaveDayType {
        public static final String FULLDAY = "FullDay";
        public static final String HALFDAY = "HalfDay";
    }

    public class LeaveRequestStatus {
        public static final String APPROVED = "Approved";
        public static final String REJECTED = "Rejected";
        public static final String PENDING = "Pending";
    }

    public class LeaveRequestStatusVal {
        public static final int APPROVED = 1;
        public static final int REJECTED = 2;
        public static final int PENDING = 3;
    }

    public class LeavePageFor {
        public static final String ADMIN = "manager";
        public static final String OTHERUSER = "";
    }

    public class LeaveFor {
        public static final String TEACHER = "teacher";
        public static final String STUDENT = "student";
    }

    public class TimetableOf {
        public static final String STAFF = "staff";
        public static final String STUDENT = "student";
        public static final String SELF = "self";

    }

    public class VoucharType {
        public static final String PURCHASE = "Purchase";
        public static final String SALE = "Sale";
        public static final String PAYMENT = "Payment";
        public static final String RECEIPT = "Receipt";

    }

    public class TransactionMode {
        public static final String PAYMENT = "Payment";
        public static final String RECEIPT = "Receipt";

    }

    public class InventoryItemsContextualMenu {
        public static final int DELETE = 1;
        public static final int EDIT = 2;
    }

    public class InventoryDiscountTypes {
        public static final String PERCENTAGE = "Parcentage";
        public static final String FLAT = "Flat";
    }

    public class DueReportTypes {
        public static final String CLASSWISE = "Class Wise";
        public static final String STUDENTWISE = "Student Wise";
    }

    public class Module {
        public static final String ACCOUNTS = "Accounts";
        public static final String INVENTORY = "Inventory";
    }

    public class ContextTypeForPermissionResult {
        public static final String DRIVER_DASHBOARD = "driver_dashboard";
        public static final String CW_HW = "cw_hw";
    }

    public class AdminDashboardBottomNavTab {
        public static final int TODAY = 200;
        public static final int HOME = 203;
        public static final int TOOLS = 204;
    }

    public class CTDashboardBottomNavTab {
        public static final int TODAY = 401;
        public static final int HOME = 400;
        public static final int TOOLS = 402;
    }

    public class STDashboardBottomNavTab {
        public static final int TODAY = 501;
        public static final int HOME = 500;
        public static final int TOOLS = 502;
    }

    public class ParentDashboardBottomNavTab {
        public static final int HOME = 300;
        public static final int TOOLS = 301;
        public static final int LEARN = 302;
        public static final int COMMUNITY = 303;
    }

    public class DriverDashboardBottomNavTab {
        public static final int HOME = 100;
        public static final int MOVIES = 101;
        public static final int TV = 102;
        public static final int LIST = 103;
        public static final int MORE = 104;
    }

    public class TrackLoction {
        public static final String MAP_PACKAGE_NAME = "com.google.android.apps.maps";
        public static final String MAP_PALYSTORE_URL = "market://details?id=com.google.android.apps.maps";
    }

    public class JourneyMode {
        public static final String START = "Start";
        public static final String COMPLETE = "End";
    }

    public static class LanguagesISOCode {
        public static final String HINDI = "hi";
        public static final String ENGLISH = "en";
        public static final String TAMIL = "ta";
        public static final String PUNJABI = "pa";
        public static final String MARATHI = "mr";
        public static final String GUJARATI = "gu";
        public static final String BENGALI = "bn";
        public static final String TELUGU = "te";
        public static final String KANNADA = "kn";
        public static final String MALAYALAM = "ml";
    }

    public static class Languages {
        public static final String HINDI = "हिंदी";
        public static final String ENGLISH = "English";
        public static final String TAMIL = "தமிழ்";
        public static final String PUNJABI = "ਪੰਜਾਬੀ";
        public static final String MARATHI = "मराठी";
        public static final String GUJARATI = "ગુજરાતી";
        public static final String BENGALI = "বাঙালি";
        public static final String TELUGU = "తెలుగు";
        public static final String KANNADA = "ಕನ್ನಡ";
        public static final String MALAYALAM = "മലയാളം";
    }

    public class TrackLocationStaus {
        public static final int SUCCESS = 0;
        public static final int COMPLETED = 1;
        public static final int NO_JOURNEY = 2;
        public static final int STARTED_BUT_NO_LOCATION = 3;
    }

    public class Alarm {
        public static final int START = 1;
        public static final int CANCEL = 2;
    }

    public class ProfilePhoto {
        public static final String FATHER = "father";
        public static final String MOTHER = "mother";
        public static final String STUDENT = "student";
    }

    public class ModuleSetting {
        public static final String TRANSPORT = "Transport";
    }

    public class LeaveRequesterType {
        public static final int TEACHER_LEAVE = 1;
        public static final int STUDENT_LEAVE = 2;
    }

    public class AnalyticsCategoryRegistration {
        public static final String ADMISSION = "Admission";
        public static final String REGISTRATION = "Registration";
    }

    public class ProductFlavors {
        public static final String SCIENTIFICSTUDY = "scientificstudy";
        public static final String LITTLE_ONE_JAIPURIA_PRESCHOOL = "little_one_jaipuria_preschool";
        public static final String JH_PODDAR_BH = "jh_poddar_bh";
    }

    public class SchoolCodes {
        public static final String LITTLE_ONE_JAIPURIA_PRESCHOOL = "littleone";
        public static final String JH_PODDAR_BH = "jhpoddar";
    }

    public class OnlinePaymentFor {
        public static final String ADMISSION = "admission";
    }

    public class AccountExpired {
        public static final String TRAIL = "Trial";
    }

    public class AdminModules {
        public static final String REGISTRATION_MODULE = "Registration";
        public static final String STUDENT_MODULE = "Students";
        public static final String STAFF_MODULE = "Staff";
        public static final String ATTENDANCE_MODULE = "Attendance";

        public static final String CW_MODULE = "Classwork";
        public static final String HW_MODULE = "Homework";
        public static final String EXAM_MODULE = "Exam";
        public static final String FEE_MODULE = "Fee";

        public static final String TRANSPORT_MODULE = "Transport";
        public static final String INVENTORY_MODULE = "Inventory";
        public static final String MY_LEAVE_MODULE = "My Leave";
        public static final String ACCOUNTS_MODULE = "Accounts";

        public static final String MY_TIMETABLE_MODULE = "My Timetable";
        public static final String STAFF_TIMETABLE_MODULE = "Staff Timetable";
        public static final String STUDENT_TIMETABLE_MODULE = "Student Timetable";
        public static final String LEAVE_MODULE = "Leave";

        public static final String SELF_ATTENDANCE_MODULE = "Self Attendance";
        public static final String ALLOW_DISCOUNT = "Allow Discount";

        public static final String H_HOLIDAYS = "Holidays";
        public static final String H_NEWS = "News";
        public static final String H_NOTICE = "Notice";
        public static final String H_EVENT = "Event";
    }

    /**
     * Getting sections from api response.
     */
    public class HomeTabSections {
        public static final String BDAY = "birthday";
        public static final String MESSAGE = "message";
        public static final String PTM = "ptm";
        public static final String HOLIDAY = "holiday";
        public static final String NEWS_NOTICE = "newsnotice";
        public static final String SPECIAL_EVENT = "specialevent";
        public static final String EVENT = "event";
        public static final String SUNDAY = "sunday";
        public static final String SURVEY = "survey";
        public static final String SOCIAL_SHARE = "socialshare";
    }

    public class TodayTabSections {
        public static final String FEE = "fee";
        public static final String REG = "reg";
        public static final String MESSENGER = "messenger";

        public static final String CHAT = "chat";
        //        public static final String STAFF = "";
        public static final String TIMETABLE = "timetable";
        public static final String ACADEMIC = "academic";

        public static final String LEARN = "learn";
        public static final String HEALTH = "health";
        public static final String AUDIT = "audit";
    }

    public class BroadcastAudience {
        public static final String STAFF = "staff";
        public static final String PARENT = "parents";
    }

    public class SmsAudience {
        public static final String STAFF = "staff";
        public static final String PARENT = "parents";
    }

    public class MessageType {
        public static final String BROADCAST = "Broadcast";
        public static final String BULKSMS = "BulkSms";
    }

    public class DotsIndicator {
        public static final int RADIUS = 8;
        public static final int PADDING = 15;
        public static final int HEIGHT = 50;
    }

    public class BarGraphSettings {
        public static final float BAR_BORDER_WIDTH = 0.9f;
        public static final float GRANULARITY = 1f;
        public static final int VISIBLE_X_RANGE_MAX = 4;
        public static final int VIEW_TO_X = 2;
        public static final int DURATION_MILLIS_X = 5000;
    }


    public class BlogTabs {
        public static final String INTERVIEW = "Interview";
        public static final String JOURNALISM = "Journalism";
        public static final String WRITINGS = "Writings";
        public static final String PARENTING = "Parenting";
    }

        public class PaymentModes {
            public static final String CASH = "Cash";
            public static final String CHEQUE = "Cheque";
            public static final String BANKPROCESS = "BankProcess";
            public static final String DEMANDDRAFT = "DemandDraft";
            public static final String PAYTM = "Paytm";
            public static final String NEFT = "Neft";
            public static final String ONLINE = "Online";
            public static final String OTHER = "Other";
            public static final String CARDSWAP = "CardSwap";
        }

        public class PaymentModeId {
            public static final int CASH = 1;
            public static final int CHEQUE = 2;
            public static final int BANKPROCESS = 5;
            public static final int DEMANDDRAFT = 8;
            public static final int PAYTM = 9;
            public static final int NEFT = 10;
            public static final int ONLINE = 11;
            public static final int OTHER = 12;
            public static final int CARDSWAP = 13;

        }

}
