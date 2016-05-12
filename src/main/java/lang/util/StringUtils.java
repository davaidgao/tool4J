package lang.util;

/**
 * String 常用类
 * 扩展自{@link org.apache.commons.lang3.StringUtils}
 * 
 * @author gaodawei
 * @since 1.0.0
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{
	public static boolean isNotNumeric(CharSequence cs){
		return !isNumeric(cs);
	}
}
