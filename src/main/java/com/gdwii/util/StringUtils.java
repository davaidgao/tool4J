package com.gdwii.util;

/**
 *
 * @author gdw
 * @since 1.0
 *
 */
public abstract class StringUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs){
        return !isEmpty(cs);
    }

    public static boolean isNumeric(CharSequence cs){
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotNumeric(CharSequence cs){
		return !isNumeric(cs);
	}


}
