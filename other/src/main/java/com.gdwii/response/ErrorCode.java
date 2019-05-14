package com.gdwii.response;

/**
 * 错误编码
 */
public interface ErrorCode {
    /**
     * 错误信息编码
     * @return 错误编码
     */
    int code();

    /**
     * 错误信息描述
     * @return 错误描述
     */
    String msg();

    /**
     * 错误信息描述
     * @param extraInfos 额外补充信息
     * @return 完整的描述信息
     */
    default String msg(Object ... extraInfos){
        return String.format(msg(), extraInfos);
    }
}