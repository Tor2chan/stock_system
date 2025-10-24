package th.team.stock.commons;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import ch.qos.logback.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils implements ApiConstant{
    
	/**
	 * 
	 * @param entries
	 * @param message
	 * @param addOn
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> response(
			Object entries,
			String message,
			Map<String, Object> addOn) {

		Map<String, Object> response = new HashMap<>();
		response.put(STATUS, 200);
		response.put(MESSAGE, message);
		response.put(ENTRIES, entries);
		if (null != addOn) {
			for (Map.Entry<String, Object> entry : addOn.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				response.put(key, value);
			}
		}
		return response;
	}

	/**
	 * 
	 * @param message
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> responseError(String message) {

		Map<String, Object> response = new HashMap<>();
		response.put(STATUS, 400);
		response.put(MESSAGE, message);

		return response;
	}
	
	/**
	 * 
	 * @param status
	 * @param message
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> responseByStatus(int status, String message) {

		Map<String, Object> response = new HashMap<>();
		response.put(STATUS, status);
		response.put(MESSAGE, message);

		return response;
	}
	
	/**
	 * i18n
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		try {
			
			ResourceBundle resourceBundle = ResourceBundle.getBundle(CLASSPATH);
			return resourceBundle.getString(key);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}
	
	/**
	 * concatLikeParam
	 * @param param
	 * @param start
	 * @param end
	 * @return String
	 */
	public static String concatLikeParam(String param, boolean start, boolean end) {

		if (StringUtils.isBlank(param)) return "";
		
		StringBuilder result = new StringBuilder();
		if (start) result.append("%");
		result.append(param);
		if (end) result.append("%");

		return result.toString();
	}
	
	/**
	 * convertDateSqlDate
	 * @param date
	 * @param isStart
	 * @param isEnd
	 * @return String
	 */
	public static String convertDateSqlDate(Date date, boolean isStart, boolean isEnd) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (date == null) return null;
		if (isStart) {
			return formatDate.format(date) + " 00:00";
		} else if (isEnd) {
			return formatDate.format(date) + " 23:59";
		} else {
			return formatDateTime.format(date);
		}
	}

    /**
	 * convertBudhistYearToNormalYear
	 * @param date
	 * @return Date
	 */
	public static Date convertBudhistYearToNormalYear(Date date) {
        if (date == null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -543); // Adjust Buddhist to Gregorian

        return calendar.getTime();
        
	}

    /**
	 * convertYearToBudhistYear
	 * @param date
	 * @return String
	 */
	
    public static String convertYearToBudhistYear(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateStr);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, 543); // Adjust Gregorian to Buddhist

            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace(); // Or handle it appropriately
            return null;
        }
    }

	/**
	 * joinParam
	 * @param objs
	 * @param otherParam
	 * @return Object[]
	 */
    public static Object[] joinParam(Object[] objs, Object... otherParam) {
        List<Object> params = new ArrayList<>();
        if(objs!=null){
            params.addAll(Arrays.asList(objs));
        }
        if(otherParam!=null && otherParam.length>0){
            params.addAll(Arrays.asList(otherParam));
        }
        return params.toArray();
    }

    /**
     * joinParam
     * @param objs
     * @param obj2
     * @return Object[]
     */
    public static Object[] joinParam(List<Object> objs, List<Object> obj2) {
        List<Object> params = new ArrayList<>();
        if(objs!=null){
            params.addAll(Arrays.asList(objs));
        }
        if(obj2!=null){
            params.addAll(Arrays.asList(obj2));
        }
        return params.toArray();
    }

    /**
     * hashSha256
     * @param value
     * @return
     */
    public static String hashSha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            byte[] byteData = md.digest();
            // convert the byte to hex
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("==============[ hashSha256 ]==============");
            log.error(e.getMessage(), e);
            return null;
        }
    }
    
    
    // Regular expression to match LDAP special characters
    private static final Pattern LDAP_SPECIAL_CHARS_PATTERN = Pattern.compile("[#\\\"+;<\\\\\\>]");

    /**
     * Escape LDAP special characters in a given string by replacing them with their ASCII hex values.
     *
     * @param input the input string to escape
     * @return the escaped string
     */
    public static String escapeLdapSpecialCharacters(String input) {
        Matcher matcher = LDAP_SPECIAL_CHARS_PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, escapeLdapSpecialCharacter(matcher.group()));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String escapeLdapSpecialCharacter(String character) {
        byte[] bytes = character.getBytes();
        StringBuilder escaped = new StringBuilder();
        for (byte b : bytes) {
            escaped.append('\\');
            escaped.append(String.format("%02X", b));
        }
        return escaped.toString();
    }
    
    /**
     * formatDate
     * @param date
     * @param format
     * @param locale
     * @return
     */
    public static String formatDate(Date date, String format, Locale locale){
        if(date == null)
        {
            return "";
        }

        DateFormat dateFormat = new SimpleDateFormat(format, locale);
        return dateFormat.format(date);
    }

    /**
     * checkBetweenDate
     * @param activePeriodStart
     * @param activePeriodEnd
     * @param now
     * @return boolean
     */
    public static boolean checkBetweenDate(Date activePeriodStart, Date activePeriodEnd, Date now) {
        if (activePeriodStart == null) {
            return activePeriodEnd == null || now.compareTo(activePeriodEnd) < 0;
        } else if (activePeriodEnd == null) {
            return activePeriodStart.compareTo(now) < 0;
        } else {
            return activePeriodStart.compareTo(now) * now.compareTo(activePeriodEnd) > 0;
        }
    }

    /**
     * generateRandom6DigitNumber
     * @return OTP
     */
    public static String generateRandom6DigitNumber() {
        SecureRandom secureRandom = new SecureRandom();
        // Generate a number in the range 0-999999
        int number = secureRandom.nextInt(1000000);
        // Format the number to be exactly 6 digits, with leading zeros if necessary
        return String.format("%06d", number);
    }

    /**
     * generateRandom6DigitString
     * @return REFCODE
     */
    public static String generateRandom6DigitString() {
        SecureRandom secureRandom = new SecureRandom();
        final String CHARACTER = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = secureRandom.nextInt(CHARACTER.length());
            sb.append(CHARACTER.charAt(index));
        }
        return sb.toString();
    }

    /**
     * verify null or empty string
     * @param s
     * @return "" or s
     */
    public static String isNullReturnBlank(String s) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        return s;
    }
    
}
