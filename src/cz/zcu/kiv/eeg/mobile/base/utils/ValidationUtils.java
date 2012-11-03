package cz.zcu.kiv.eeg.mobile.base.utils;

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

	public static boolean isUsernameFormatInvalid(String username) {
		return username == null || username.isEmpty() || !isEmailValid(username);
	}

	public static boolean isPasswordFormatInvalid(String password) {
		return password == null || password.isEmpty();
	}

	public static boolean isUrlFormatInvalid(String url) {
		return url == null || url.isEmpty() || !URLUtil.isValidUrl(url) || "http://".equals(url)
				|| "https://".equals(url);
	}
}
