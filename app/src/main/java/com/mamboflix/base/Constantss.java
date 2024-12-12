package com.mamboflix.base;


public class Constantss {



    //Date formats
    public static final String DATE_FORMAT_DMY = "dd MMM, yyyy";
    public static final String DATE_FORMAT_DM = "dd MM";
    public static final String DATE_FORMAT_MDY = "MM/dd/yyyy";
    public static String IMAGE_BASE_URL = "http://mobilein";
    public static String BUILD_TYPE = "Production";
    public static String FILE_AUTHORITY = "com.vidyagrowth.file";
    public static String ACTIVITY_UPLOAD_URL = IMAGE_BASE_URL + "/api/class/activity/upload";
    public static String HTTPS = "https://";
    public static String SUBDOMAIN=".scientificstudy.in";

    public static int TOTAL_ATTACHMENTS_IN_NEWS_NOTICE_CREATION = 1;
    public static float IMAGE_SIZE_IN_NEWS_NOTICE_CREATION = (float) 5.0;

    //Urls

    public static String PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=";
    public static String SIGN_UP_URL = "https://www.scientificstudy.in/create-account.php";

    public static String APP_PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=com.jeannypr.scientificstudy";






    public class PermissionRequestCode {
        public static final int LOCATION = 300;
        public static final int RREAD_WRITE_STORAGE = 300;
    }

    public class Directory {
        /*public static final String Base = "SPT";*/
        public static final String Base = "VidyaGrowth";
    }

    public class ContextTypeForPermissionResult {
        public static final String DRIVER_DASHBOARD = "driver_dashboard";
        public static final String CW_HW = "vidya";
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



    public class FileType {
        //IMAGE
        public static final String PNG = "png";
        public static final String JPG = "jpg";
        public static final String JPEG = "jpeg";
        public static final String BMP = "bmp";
        //AUDIO
        public static final String M4A = "m4a";
        public static final String AMR = "amr";
        public static final String GPP = "3gpp";
        public static final String MP3 = "mp3";
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
    }








    public class ConnectivityType {
        public static final String WIFI = "wifi";
        public static final String MOBILE_DATA = "mobile_data";
    }







    public class Module {
        public static final String ACCOUNTS = "Accounts";
        public static final String INVENTORY = "Inventory";
    }



    public class ParentDashboardBottomNavTab {
        public static final int HOME = 300;
        public static final int TOOLS = 301;
        public static final int LEARN = 302;
        public static final int COMMUNITY = 303;
    }

    public class DriverDashboardBottomNavTab {
        public static final int HOME = 100;
        public static final int LOGOUT = 101;
        public static final int NOTIFY = 102;
        public static final int EMERGENCY_CONTACT = 103;
        public static final int STUDENT = 104;
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



    public class Alarm {
        public static final int START = 1;
        public static final int CANCEL = 2;
    }


}
