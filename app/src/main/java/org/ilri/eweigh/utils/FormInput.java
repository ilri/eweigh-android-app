package org.ilri.eweigh.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Create form inputs from existing views in XML
 */

public class FormInput {

    public static EditText setEditText(Context context, int resourceId){
        return (EditText) LayoutInflater.from(context).inflate(resourceId, null);
    }
}
