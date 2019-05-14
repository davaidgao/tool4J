package com.gdwii.util;

/**
 * 字符串辅助方法
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
        int i = cs.charAt(0) == '+'  || cs.charAt(0) == '-' ? 1 : 0; // 包括正负号

        if(i == sz){ // 只有正负号则不认为是数字
            return false;
        }

        // 从i开始递增处理
        for (; i < sz; i++) {
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
