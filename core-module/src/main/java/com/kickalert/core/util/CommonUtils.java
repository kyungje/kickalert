package com.kickalert.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
public class CommonUtils {
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String CATEGORY_PREFIX = "/";

    /*	 * 공백 또는 null 체크	 */
    public static boolean isEmpty(Object obj) {
        if(obj == null) return true;
        if((obj instanceof String) && (((String)obj).trim().length() == 0)) { return true; }
        if(obj instanceof Map) { return ((Map<?, ?>) obj).isEmpty(); }
        if(obj instanceof Map) { return ((Map<?, ?>)obj).isEmpty(); }
        if(obj instanceof List) {return ((List<?>)obj).isEmpty();}
        if(obj instanceof Object[]) {
            return (((Object[])obj).length == 0);
        }
        return false;
    }

    public static String toUTCStringFromDateTime(LocalDateTime localDateTime){
        if(isEmpty(localDateTime)){
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
    }

    public static String buildFileName(String category, String originalFileName, String loginId) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);

        String fileName = StringUtils.hasText(loginId) ? loginId : originalFileName.substring(0, fileExtensionIndex);

        return category + CATEGORY_PREFIX + fileName + fileExtension;
    }

    public static String makeUserIdentificationId(Byte length){
        char pwCollection[] = new char[] {
                '1','2','3','4','5','6','7','8','9','0',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};//배열에 선언

        String ranPw = "";

        for (int i = 0; i < length; i++) {
            int selectRandomPw = (int)(Math.random()*(pwCollection.length)); //Math.rondom()은 0.0이상 1.0미만의 난수를 생성해 준다.
            ranPw += pwCollection[selectRandomPw];
        }

        return ranPw;
    }

    public static LocalDateTime convertStringToLocalDateTime(String dateTimeStr) {
        log.info("dateTime : {}", dateTimeStr);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}
