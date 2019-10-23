package org.ilri.eweigh.utils;

import org.ilri.eweigh.BuildConfig;

public class URL {

    private static String BASE_URL = BuildConfig.DEBUG ?
            "http://192.168.1.107/Code/ilri/eweigh-api/" :
            "https://app.eweighapp.com/";

    private static final String BASE_API_URL = BASE_URL + "api/v1/";
    private static final String RESOURCE_URL = BASE_URL + "content/uploads/";

    // Resource URL
    public static final String AvatarPhotoUrl = RESOURCE_URL + "avatars/";

    // App Bundle
    public static final String Bundle = BASE_API_URL + "bundle";

    // Auth
    public static final String Register = BASE_API_URL + "register";
    public static final String Login = BASE_API_URL + "login";
    public static final String UpdateAccount = BASE_API_URL + "user/update";
    public static final String Verify = BASE_API_URL + "account/verify";

    // HG/LW
    public static final String GetLiveWeight = BASE_API_URL + "lw";
    public static final String Cattle = BASE_API_URL + "cattle";
    public static final String RegisterCattle = BASE_API_URL + "cattle/register";

    // Feeds
    public static final String FeedsList = BASE_API_URL + "feeds";
    public static final String GetFeedRation = BASE_API_URL + "feeds/ration";

    // Reports
    public static final String Submissions = BASE_API_URL + "report/submissions";
}
