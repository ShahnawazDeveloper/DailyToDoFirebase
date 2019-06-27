package com.mytodolist.utility;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static boolean validateEmptyEditText(EditText et_advertise) {
        return et_advertise.getText().toString().trim().isEmpty();
    }

    public static boolean validateEmail(EditText et_email) {
        String email = et_email.getText().toString().trim();
        return (email.isEmpty() || !isValidEmail(email));
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidWebUrl(String url) {
        return !TextUtils.isEmpty(url) && !Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean validatePassword(AppCompatEditText et_password) {
        String password = et_password.getText().toString().trim();
        return (password.isEmpty() || !isValidPassword(password));
    }

    public static boolean validateConfirmPassword(EditText et_password, EditText et_confirm_password) {
        return !et_password.getText().toString().trim().equals(et_confirm_password.getText().toString().trim());
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^.*(?=.{8,})(?=.*\\d)(?=.*[a-zA-Z]).*$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean validateMobileNo(AppCompatEditText et_mobile_no) {
        String mobile_no = et_mobile_no.getText().toString().trim();
        return (mobile_no.isEmpty() || !isValidMobileNo(mobile_no));
    }

    private static boolean isValidMobileNo(String mobile_no) {
        return !TextUtils.isEmpty(mobile_no) && Patterns.PHONE.matcher(mobile_no).matches();
    }

    public static void requestFocus(Activity context, View view) {
        if (view.requestFocus()) {
            context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
