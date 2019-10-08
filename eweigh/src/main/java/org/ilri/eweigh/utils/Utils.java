package org.ilri.eweigh.utils;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.ilri.eweigh.R;
import org.ilri.eweigh.misc.Country;
import org.ilri.eweigh.misc.County;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Utils {

    /**
     *
     * Capitalize all words in a sentence
     *
     * i am joe = I Am Joe
     *
     * */
    public static String capitalizeAll(String string){
        String[] words = string.split("\\s+");
        String newStr = "";

        for (String word: words) {
            newStr = String.format("%s %s", newStr, StringUtils.capitalize(word.toLowerCase()));
        }

        return newStr.trim();
    }

    public static String formatNumber(double number){
        DecimalFormat formatter = new DecimalFormat("#,###,###.##");
        return formatter.format(number);
    }

    public static String formatDecimal(double number){
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(number);
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);

        return df.format(c.getTime());
    }

    public static ProgressDialog getProgressDialog(Context context, String message, boolean cancellable){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.setCancelable(cancellable);

        return dialog;
    }

    public static DatePickerDialog getDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener){
        Calendar now = Calendar.getInstance();

        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(context,
                android.R.style.Theme_Holo_Dialog, listener, year, month, day);
    }

    public static AlertDialog getSimpleDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNeutralButton("Close", null);

        return builder.create();
    }

    public static AlertDialog getSimpleDialog(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("Close", null);

        return builder.create();
    }

    public static AlertDialog getCustomDialog(Context context, View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        return builder.create();
    }

    public static String getPaymentMode(int paymentId){
        String paymentMode = "";

        switch (paymentId) {

            case 1:
                paymentMode = "Cash";
                break;

            case 2:
                paymentMode = "M-Pesa";
                break;

            case 3:
                paymentMode = "Bank";
                break;
        }

        return paymentMode;
    }

    public static View getBlankState(Context context, String message){
        View blankState = LayoutInflater.from(context).inflate(R.layout.blank_state, null);
        TextView blankText = blankState.findViewById(R.id.txt_blank_state);

        blankText.setText(message);

        return blankState;
    }

    public static List<County> getCountiesList(){
        String str = "[{\"id\":\"30\",\"county\":\"Baringo\"},{\"id\":\"36\",\"county\":\"Bomet\"},{\"id\":\"39\",\"county\":\"Bungoma\"},{\"id\":\"40\",\"county\":\"Busia\"},{\"id\":\"28\",\"county\":\"Elgeyo-Marakwet\"},{\"id\":\"14\",\"county\":\"Embu\"},{\"id\":\"7\",\"county\":\"Garissa\"},{\"id\":\"43\",\"county\":\"Homa Bay\"},{\"id\":\"11\",\"county\":\"Isiolo\"},{\"id\":\"34\",\"county\":\"Kajiado\"},{\"id\":\"37\",\"county\":\"Kakamega\"},{\"id\":\"35\",\"county\":\"Kericho\"},{\"id\":\"22\",\"county\":\"Kiambu\"},{\"id\":\"3\",\"county\":\"Kilifi\"},{\"id\":\"20\",\"county\":\"Kirinyaga\"},{\"id\":\"45\",\"county\":\"Kisii\"},{\"id\":\"42\",\"county\":\"Kisumu\"},{\"id\":\"15\",\"county\":\"Kitui\"},{\"id\":\"2\",\"county\":\"Kwale\"},{\"id\":\"31\",\"county\":\"Laikipia\"},{\"id\":\"5\",\"county\":\"Lamu\"},{\"id\":\"16\",\"county\":\"Machakos\"},{\"id\":\"17\",\"county\":\"Makueni\"},{\"id\":\"9\",\"county\":\"Mandera\"},{\"id\":\"10\",\"county\":\"Marsabit\"},{\"id\":\"12\",\"county\":\"Meru\"},{\"id\":\"44\",\"county\":\"Migori\"},{\"id\":\"1\",\"county\":\"Mombasa\"},{\"id\":\"21\",\"county\":\"Murang'a\"},{\"id\":\"47\",\"county\":\"Nairobi City\"},{\"id\":\"32\",\"county\":\"Nakuru\"},{\"id\":\"29\",\"county\":\"Nandi\"},{\"id\":\"33\",\"county\":\"Narok\"},{\"id\":\"46\",\"county\":\"Nyamira\"},{\"id\":\"18\",\"county\":\"Nyandarua\"},{\"id\":\"19\",\"county\":\"Nyeri\"},{\"id\":\"25\",\"county\":\"Samburu\"},{\"id\":\"41\",\"county\":\"Siaya\"},{\"id\":\"6\",\"county\":\"Taita-Taveta\"},{\"id\":\"4\",\"county\":\"Tana River\"},{\"id\":\"13\",\"county\":\"Tharaka-Nithi\"},{\"id\":\"26\",\"county\":\"Trans Nzoia\"},{\"id\":\"23\",\"county\":\"Turkana\"},{\"id\":\"27\",\"county\":\"Uasin Gishu\"},{\"id\":\"38\",\"county\":\"Vihiga\"},{\"id\":\"8\",\"county\":\"wajir\"},{\"id\":\"24\",\"county\":\"West Pokot\"}]";
        List<County> counties = new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(str);

            for(int i=0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                County c = new County(obj.getInt("id"), obj.getString("county"));

                counties.add(c);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return counties;
    }

    public static List<Country> getCountriesList(){
        String str = "[{\"id\":\"1\",\"country\":\"Aruba\"},{\"id\":\"2\",\"country\":\"Afghanistan\"},{\"id\":\"3\",\"country\":\"Angola\"},{\"id\":\"4\",\"country\":\"Anguilla\"},{\"id\":\"5\",\"country\":\"\\u00c5land Islands\"},{\"id\":\"6\",\"country\":\"Albania\"},{\"id\":\"7\",\"country\":\"Andorra\"},{\"id\":\"8\",\"country\":\"United Arab Emirates\"},{\"id\":\"9\",\"country\":\"Argentina\"},{\"id\":\"10\",\"country\":\"Armenia\"},{\"id\":\"11\",\"country\":\"American Samoa\"},{\"id\":\"12\",\"country\":\"Antarctica\"},{\"id\":\"13\",\"country\":\"French Southern and Antarctic Lands\"},{\"id\":\"14\",\"country\":\"Antigua and Barbuda\"},{\"id\":\"15\",\"country\":\"Australia\"},{\"id\":\"16\",\"country\":\"Austria\"},{\"id\":\"17\",\"country\":\"Azerbaijan\"},{\"id\":\"18\",\"country\":\"Burundi\"},{\"id\":\"19\",\"country\":\"Belgium\"},{\"id\":\"20\",\"country\":\"Benin\"},{\"id\":\"21\",\"country\":\"Burkina Faso\"},{\"id\":\"22\",\"country\":\"Bangladesh\"},{\"id\":\"23\",\"country\":\"Bulgaria\"},{\"id\":\"24\",\"country\":\"Bahrain\"},{\"id\":\"25\",\"country\":\"Bahamas\"},{\"id\":\"26\",\"country\":\"Bosnia and Herzegovina\"},{\"id\":\"27\",\"country\":\"Saint Barth\\u00e9lemy\"},{\"id\":\"28\",\"country\":\"Belarus\"},{\"id\":\"29\",\"country\":\"Belize\"},{\"id\":\"30\",\"country\":\"Bermuda\"},{\"id\":\"31\",\"country\":\"Bolivia\"},{\"id\":\"32\",\"country\":\"Brazil\"},{\"id\":\"33\",\"country\":\"Barbados\"},{\"id\":\"34\",\"country\":\"Brunei\"},{\"id\":\"35\",\"country\":\"Bhutan\"},{\"id\":\"36\",\"country\":\"Bouvet Island\"},{\"id\":\"37\",\"country\":\"Botswana\"},{\"id\":\"38\",\"country\":\"Central African Republic\"},{\"id\":\"39\",\"country\":\"Canada\"},{\"id\":\"40\",\"country\":\"Cocos (Keeling) Islands\"},{\"id\":\"41\",\"country\":\"Switzerland\"},{\"id\":\"42\",\"country\":\"Chile\"},{\"id\":\"43\",\"country\":\"China\"},{\"id\":\"44\",\"country\":\"Ivory Coast\"},{\"id\":\"45\",\"country\":\"Cameroon\"},{\"id\":\"46\",\"country\":\"DR Congo\"},{\"id\":\"47\",\"country\":\"Republic of the Congo\"},{\"id\":\"48\",\"country\":\"Cook Islands\"},{\"id\":\"49\",\"country\":\"Colombia\"},{\"id\":\"50\",\"country\":\"Comoros\"},{\"id\":\"51\",\"country\":\"Cape Verde\"},{\"id\":\"52\",\"country\":\"Costa Rica\"},{\"id\":\"53\",\"country\":\"Cuba\"},{\"id\":\"54\",\"country\":\"Cura\\u00e7ao\"},{\"id\":\"55\",\"country\":\"Christmas Island\"},{\"id\":\"56\",\"country\":\"Cayman Islands\"},{\"id\":\"57\",\"country\":\"Cyprus\"},{\"id\":\"58\",\"country\":\"Czechia\"},{\"id\":\"59\",\"country\":\"Germany\"},{\"id\":\"60\",\"country\":\"Djibouti\"},{\"id\":\"61\",\"country\":\"Dominica\"},{\"id\":\"62\",\"country\":\"Denmark\"},{\"id\":\"63\",\"country\":\"Dominican Republic\"},{\"id\":\"64\",\"country\":\"Algeria\"},{\"id\":\"65\",\"country\":\"Ecuador\"},{\"id\":\"66\",\"country\":\"Egypt\"},{\"id\":\"67\",\"country\":\"Eritrea\"},{\"id\":\"68\",\"country\":\"Western Sahara\"},{\"id\":\"69\",\"country\":\"Spain\"},{\"id\":\"70\",\"country\":\"Estonia\"},{\"id\":\"71\",\"country\":\"Ethiopia\"},{\"id\":\"72\",\"country\":\"Finland\"},{\"id\":\"73\",\"country\":\"Fiji\"},{\"id\":\"74\",\"country\":\"Falkland Islands\"},{\"id\":\"75\",\"country\":\"France\"},{\"id\":\"76\",\"country\":\"Faroe Islands\"},{\"id\":\"77\",\"country\":\"Micronesia\"},{\"id\":\"78\",\"country\":\"Gabon\"},{\"id\":\"79\",\"country\":\"United Kingdom\"},{\"id\":\"80\",\"country\":\"Georgia\"},{\"id\":\"81\",\"country\":\"Guernsey\"},{\"id\":\"82\",\"country\":\"Ghana\"},{\"id\":\"83\",\"country\":\"Gibraltar\"},{\"id\":\"84\",\"country\":\"Guinea\"},{\"id\":\"85\",\"country\":\"Guadeloupe\"},{\"id\":\"86\",\"country\":\"Gambia\"},{\"id\":\"87\",\"country\":\"Guinea-Bissau\"},{\"id\":\"88\",\"country\":\"Equatorial Guinea\"},{\"id\":\"89\",\"country\":\"Greece\"},{\"id\":\"90\",\"country\":\"Grenada\"},{\"id\":\"91\",\"country\":\"Greenland\"},{\"id\":\"92\",\"country\":\"Guatemala\"},{\"id\":\"93\",\"country\":\"French Guiana\"},{\"id\":\"94\",\"country\":\"Guam\"},{\"id\":\"95\",\"country\":\"Guyana\"},{\"id\":\"96\",\"country\":\"Hong Kong\"},{\"id\":\"97\",\"country\":\"Heard Island and McDonald Islands\"},{\"id\":\"98\",\"country\":\"Honduras\"},{\"id\":\"99\",\"country\":\"Croatia\"},{\"id\":\"100\",\"country\":\"Haiti\"},{\"id\":\"101\",\"country\":\"Hungary\"},{\"id\":\"102\",\"country\":\"Indonesia\"},{\"id\":\"103\",\"country\":\"Isle of Man\"},{\"id\":\"104\",\"country\":\"India\"},{\"id\":\"105\",\"country\":\"British Indian Ocean Territory\"},{\"id\":\"106\",\"country\":\"Ireland\"},{\"id\":\"107\",\"country\":\"Iran\"},{\"id\":\"108\",\"country\":\"Iraq\"},{\"id\":\"109\",\"country\":\"Iceland\"},{\"id\":\"110\",\"country\":\"Israel\"},{\"id\":\"111\",\"country\":\"Italy\"},{\"id\":\"112\",\"country\":\"Jamaica\"},{\"id\":\"113\",\"country\":\"Jersey\"},{\"id\":\"114\",\"country\":\"Jordan\"},{\"id\":\"115\",\"country\":\"Japan\"},{\"id\":\"116\",\"country\":\"Kazakhstan\"},{\"id\":\"117\",\"country\":\"Kenya\"},{\"id\":\"118\",\"country\":\"Kyrgyzstan\"},{\"id\":\"119\",\"country\":\"Cambodia\"},{\"id\":\"120\",\"country\":\"Kiribati\"},{\"id\":\"121\",\"country\":\"Saint Kitts and Nevis\"},{\"id\":\"122\",\"country\":\"South Korea\"},{\"id\":\"123\",\"country\":\"Kosovo\"},{\"id\":\"124\",\"country\":\"Kuwait\"},{\"id\":\"125\",\"country\":\"Laos\"},{\"id\":\"126\",\"country\":\"Lebanon\"},{\"id\":\"127\",\"country\":\"Liberia\"},{\"id\":\"128\",\"country\":\"Libya\"},{\"id\":\"129\",\"country\":\"Saint Lucia\"},{\"id\":\"130\",\"country\":\"Liechtenstein\"},{\"id\":\"131\",\"country\":\"Sri Lanka\"},{\"id\":\"132\",\"country\":\"Lesotho\"},{\"id\":\"133\",\"country\":\"Lithuania\"},{\"id\":\"134\",\"country\":\"Luxembourg\"},{\"id\":\"135\",\"country\":\"Latvia\"},{\"id\":\"136\",\"country\":\"Macau\"},{\"id\":\"137\",\"country\":\"Saint Martin\"},{\"id\":\"138\",\"country\":\"Morocco\"},{\"id\":\"139\",\"country\":\"Monaco\"},{\"id\":\"140\",\"country\":\"Moldova\"},{\"id\":\"141\",\"country\":\"Madagascar\"},{\"id\":\"142\",\"country\":\"Maldives\"},{\"id\":\"143\",\"country\":\"Mexico\"},{\"id\":\"144\",\"country\":\"Marshall Islands\"},{\"id\":\"145\",\"country\":\"Macedonia\"},{\"id\":\"146\",\"country\":\"Mali\"},{\"id\":\"147\",\"country\":\"Malta\"},{\"id\":\"148\",\"country\":\"Myanmar\"},{\"id\":\"149\",\"country\":\"Montenegro\"},{\"id\":\"150\",\"country\":\"Mongolia\"},{\"id\":\"151\",\"country\":\"Northern Mariana Islands\"},{\"id\":\"152\",\"country\":\"Mozambique\"},{\"id\":\"153\",\"country\":\"Mauritania\"},{\"id\":\"154\",\"country\":\"Montserrat\"},{\"id\":\"155\",\"country\":\"Martinique\"},{\"id\":\"156\",\"country\":\"Mauritius\"},{\"id\":\"157\",\"country\":\"Malawi\"},{\"id\":\"158\",\"country\":\"Malaysia\"},{\"id\":\"159\",\"country\":\"Mayotte\"},{\"id\":\"160\",\"country\":\"Namibia\"},{\"id\":\"161\",\"country\":\"New Caledonia\"},{\"id\":\"162\",\"country\":\"Niger\"},{\"id\":\"163\",\"country\":\"Norfolk Island\"},{\"id\":\"164\",\"country\":\"Nigeria\"},{\"id\":\"165\",\"country\":\"Nicaragua\"},{\"id\":\"166\",\"country\":\"Niue\"},{\"id\":\"167\",\"country\":\"Netherlands\"},{\"id\":\"168\",\"country\":\"Norway\"},{\"id\":\"169\",\"country\":\"Nepal\"},{\"id\":\"170\",\"country\":\"Nauru\"},{\"id\":\"171\",\"country\":\"New Zealand\"},{\"id\":\"172\",\"country\":\"Oman\"},{\"id\":\"173\",\"country\":\"Pakistan\"},{\"id\":\"174\",\"country\":\"Panama\"},{\"id\":\"175\",\"country\":\"Pitcairn Islands\"},{\"id\":\"176\",\"country\":\"Peru\"},{\"id\":\"177\",\"country\":\"Philippines\"},{\"id\":\"178\",\"country\":\"Palau\"},{\"id\":\"179\",\"country\":\"Papua New Guinea\"},{\"id\":\"180\",\"country\":\"Poland\"},{\"id\":\"181\",\"country\":\"Puerto Rico\"},{\"id\":\"182\",\"country\":\"North Korea\"},{\"id\":\"183\",\"country\":\"Portugal\"},{\"id\":\"184\",\"country\":\"Paraguay\"},{\"id\":\"185\",\"country\":\"Palestine\"},{\"id\":\"186\",\"country\":\"French Polynesia\"},{\"id\":\"187\",\"country\":\"Qatar\"},{\"id\":\"188\",\"country\":\"R\\u00e9union\"},{\"id\":\"189\",\"country\":\"Romania\"},{\"id\":\"190\",\"country\":\"Russia\"},{\"id\":\"191\",\"country\":\"Rwanda\"},{\"id\":\"192\",\"country\":\"Saudi Arabia\"},{\"id\":\"193\",\"country\":\"Sudan\"},{\"id\":\"194\",\"country\":\"Senegal\"},{\"id\":\"195\",\"country\":\"Singapore\"},{\"id\":\"196\",\"country\":\"South Georgia\"},{\"id\":\"197\",\"country\":\"Svalbard and Jan Mayen\"},{\"id\":\"198\",\"country\":\"Solomon Islands\"},{\"id\":\"199\",\"country\":\"Sierra Leone\"},{\"id\":\"200\",\"country\":\"El Salvador\"},{\"id\":\"201\",\"country\":\"San Marino\"},{\"id\":\"202\",\"country\":\"Somalia\"},{\"id\":\"203\",\"country\":\"Saint Pierre and Miquelon\"},{\"id\":\"204\",\"country\":\"Serbia\"},{\"id\":\"205\",\"country\":\"South Sudan\"},{\"id\":\"206\",\"country\":\"S\\u00e3o Tom\\u00e9 and Pr\\u00edncipe\"},{\"id\":\"207\",\"country\":\"Suriname\"},{\"id\":\"208\",\"country\":\"Slovakia\"},{\"id\":\"209\",\"country\":\"Slovenia\"},{\"id\":\"210\",\"country\":\"Sweden\"},{\"id\":\"211\",\"country\":\"Swaziland\"},{\"id\":\"212\",\"country\":\"Sint Maarten\"},{\"id\":\"213\",\"country\":\"Seychelles\"},{\"id\":\"214\",\"country\":\"Syria\"},{\"id\":\"215\",\"country\":\"Turks and Caicos Islands\"},{\"id\":\"216\",\"country\":\"Chad\"},{\"id\":\"217\",\"country\":\"Togo\"},{\"id\":\"218\",\"country\":\"Thailand\"},{\"id\":\"219\",\"country\":\"Tajikistan\"},{\"id\":\"220\",\"country\":\"Tokelau\"},{\"id\":\"221\",\"country\":\"Turkmenistan\"},{\"id\":\"222\",\"country\":\"Timor-Leste\"},{\"id\":\"223\",\"country\":\"Tonga\"},{\"id\":\"224\",\"country\":\"Trinidad and Tobago\"},{\"id\":\"225\",\"country\":\"Tunisia\"},{\"id\":\"226\",\"country\":\"Turkey\"},{\"id\":\"227\",\"country\":\"Tuvalu\"},{\"id\":\"228\",\"country\":\"Taiwan\"},{\"id\":\"229\",\"country\":\"Tanzania\"},{\"id\":\"230\",\"country\":\"Uganda\"},{\"id\":\"231\",\"country\":\"Ukraine\"},{\"id\":\"232\",\"country\":\"United States Minor Outlying Islands\"},{\"id\":\"233\",\"country\":\"Uruguay\"},{\"id\":\"234\",\"country\":\"United States\"},{\"id\":\"235\",\"country\":\"Uzbekistan\"},{\"id\":\"236\",\"country\":\"Vatican City\"},{\"id\":\"237\",\"country\":\"Saint Vincent and the Grenadines\"},{\"id\":\"238\",\"country\":\"Venezuela\"},{\"id\":\"239\",\"country\":\"British Virgin Islands\"},{\"id\":\"240\",\"country\":\"United States Virgin Islands\"},{\"id\":\"241\",\"country\":\"Vietnam\"},{\"id\":\"242\",\"country\":\"Vanuatu\"},{\"id\":\"243\",\"country\":\"Wallis and Futuna\"},{\"id\":\"244\",\"country\":\"Samoa\"},{\"id\":\"245\",\"country\":\"Yemen\"},{\"id\":\"246\",\"country\":\"South Africa\"},{\"id\":\"247\",\"country\":\"Zambia\"},{\"id\":\"248\",\"country\":\"Zimbabwe\"}]";

        List<Country> countries = new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(str);

            for(int i=0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                Country c = new Country(obj.getInt("id"), obj.getString("country"));

                countries.add(c);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return countries;
    }
}
