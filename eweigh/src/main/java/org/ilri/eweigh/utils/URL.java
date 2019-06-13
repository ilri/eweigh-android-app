package org.ilri.eweigh.utils;

import org.ilri.eweigh.BuildConfig;

public class URL {

    private static String BASE_URL = BuildConfig.DEBUG ?
            "http://192.168.1.103/Code/ilri/eweigh-api/" :
            "https://ilri-test.co.ke/";

    // private static final String BASE_API_URL = BASE_URL + "api/v1/";
    private static final String BASE_API_URL = BASE_URL + "";
    private static final String RESOURCE_URL = BASE_URL + "content/uploads/";

    // Resource URL
    public static final String AvatarPhotoUrl = RESOURCE_URL + "avatars/";

    // Auth
    public static final String Register = BASE_API_URL + "register";
    public static final String Login = BASE_API_URL + "login";
    public static final String UpdateAccount = BASE_API_URL + "user/update";

    // Auth
    public static final String GetLiveWeight = BASE_API_URL + "lw.php";

    // Reports
    public static final String History = BASE_API_URL + "report/history";
}
