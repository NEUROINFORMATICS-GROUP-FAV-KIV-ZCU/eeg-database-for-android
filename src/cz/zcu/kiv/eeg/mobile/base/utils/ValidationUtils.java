package cz.zcu.kiv.eeg.mobile.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.webkit.URLUtil;

public class ValidationUtils {

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

    public static boolean isEmpty(String s){
        return s == null || s.isEmpty();
    }

	public static boolean isUsernameFormatInvalid(String username) {
		return  isEmpty(username) || !isEmailValid(username);
	}

	public static boolean isPasswordFormatInvalid(String password) {
		return isEmpty(password);
	}

	public static boolean isUrlFormatInvalid(String url) {
		return isEmpty(url) || !URLUtil.isValidUrl(url) || "http://".equals(url) || "https://".equals(url);
	}

    public static boolean isDateValid(String date, String datePattern) {
        SimpleDateFormat sf = new SimpleDateFormat(datePattern);
        sf.setLenient(false);
        try {
            sf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
