package org.ilri.eweigh.utils;

import org.ilri.eweigh.BuildConfig;

public class URL {

    private static String getBaseUrl(){
        String BASE_URL = "https://ilri-test.co.ke/";

        // If debug mode, set local address
        if(BuildConfig.DEBUG){
            BASE_URL = "http://192.168.0.27/Code/ilri/";
        }

        return BASE_URL;
    }

    private String BASE_URL = getBaseUrl();

    private String BASE_API_URL = BASE_URL + "api/v1/";
    private String RESOURCE_URL = BASE_URL + "content/uploads/";

    // Resource URL
    public final String AvatarPhotoUrl = RESOURCE_URL + "avatars/";
    public final String HRMPhotoUrl = RESOURCE_URL + "hrm/";

    // Auth
    public final String Login = BASE_API_URL + "login";
    public final String UpdateAccount = BASE_API_URL + "user/update";
    public final String RegisterDevice = BASE_API_URL + "register-device";

    // Members
    public final String Members = BASE_API_URL + "members";
    public final String MemberInfo = BASE_API_URL + "member_info";
    public final String UpdateMemberInfo = BASE_API_URL + "update_member_info";
    public final String RegisterMember = BASE_API_URL + "register_member";

    // Reports
    public final String Agents = BASE_API_URL + "report/agents";
    public final String Collection = BASE_API_URL + "report/collection";
}
