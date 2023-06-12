/* 
* @(#)StringUtil.java
*/
package com.glp.common.utils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 * @date 2008-06-13
 * @author yinty
 * 
 * @功能说明：
 * 字符串工具类，提供如是否为空串、空白串判断，字符替换串替换，空白串首尾剪切等函数，具体分为下面几个大类
 * 1. 字符串内容判断函数
 * 2. 字符串格式化函数
 * 3. 字符串截取函数
 * 4. 字符串与集合之间的相互转换函数
 * 
 * @版本更新列表
 * 修改版本: 1.0.2
 * 修改日期：2009-01-22
 * 修改说明：添加空白串判断函数 isBlank(), 非空白串判断函数 notBlank()
 * 复审人：
 * 
 * 修改版本: 1.0.1
 * 修改日期：2009-01-16
 * 修改说明：添加占位符替换函数 replacePlaceholder()；对象字符串化后剪除空白函数 trimOrNullToEmpty()
 * 复审人：
 * 
 * 修改版本: 1.0.0
 * 修改日期：2008-06-13
 * 修改人 : yinty
 * 修改说明：形成初始版本
 * 复审人：
 *</pre>
 */

public class StringUtil {

	/** 空串常量定义 */
	public static final String EMPTY = "";

	/** 未找到常量定义 */
	public static final int INDEX_NOT_FOUND = -1;

	//------------------------ 字符串内容判断函数 -------------------------------------	
	/**
	 * 判断字符串是否为空、null 或空白串
	 * @param s
	 * @return
	 */
	public static boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 判断字符串是否不为空、null 或空白串
	 * @param s
	 * @return
	 */
	public static boolean notBlank(String s) {
		return s != null && s.trim().length() > 0;
	}

	/**
	 * 判断字符串是否为空
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	/**
	 * 判断字符串长度是否不为空
	 * @param s
	 * @return
	 */
	public static boolean notEmpty(String s) {
		return (s != null && s.length() > 0);
	}
	
	/**
	 * 判断字符串是否为空指针，空的话回传空字符串
	 * @param s
	 * @return
	 */
	public static String nonNull(String s){
        return s == null ? "" : s;
    }
	
	/**
	 * 判断字符串是否有字符
	 * @param s
	 * @return
	 */
	public static boolean hasText(String s) {
		if (isEmpty(s)) {
			return false;
		}
		int i = s.length();
		while (i > 0) {
			if (s.charAt(--i) > ' ') {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串是否为16进制的字符串表示
	 * @param s
	 * @param index
	 * @return
	 */
	public static boolean isHexPrefix(String s, int index) {
		return s.charAt(index) == '0' && s.charAt(index + 1) == 'x';
	}

	/**
	 * 判断字符是否是数字表示，实例效果如下
	 * <pre>   
	 * StringUtils.isNumeric("123")  = true
	 * StringUtils.isNumeric("12 3") = false
	 * StringUtils.isNumeric("12.3") = false
	 * </pre>
	 * @param s
	 * @return
	 */
	public static boolean isNumeric(String s) {
		if (s == null) {
			return false;
		}
		int sz = s.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(s.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	//------------------------ 字符串格式化函数 -------------------------------------

	/**
	 * 字符串首尾剪切函数，为null则返回空串
	 * @param o
	 * @return
	 */
	public static String trim(Object o) {
		if (o == null) {
			return "";
		}
		return String.valueOf(o).trim();
	}

	/**
	 * 清除字符串中的所有空格，实例效果如下 
	 * <pre>   
	 * StringUtils.deleteWhitespace("   ab  c  ") = "abc"
	 * </pre>
	 * @param s
	 * @return
	 */
	public static String deleteWhitespace(String s) {
		if (isEmpty(s)) {
			return s;
		}
		int length = s.length();
		char[] chs = new char[length];
		int count = 0;
		for (int i = 0; i < length; i++) {
			char ch = s.charAt(i);
			if (!Character.isWhitespace(ch)) {
				chs[count++] = ch;
			}
		}
		if (count == length) {
			return s;
		}
		return new String(chs, 0, count);
	}
	
	
	public static String trimAllString(String s) {
		if (isEmpty(s)) {
			return s;
		}
		int length = s.length();
		char[] chs = new char[length];
		int count = 0;
		for (int i = 0; i < length; i++) {
			char ch = s.charAt(i);
			if (!Character.isWhitespace(ch) && !Character.isSpaceChar(ch)) {
				chs[count++] = ch;
			}
		}
		if (count == length) {
			return s;
		}
		return new String(chs, 0, count);
	}

	/**
	 * 将串首字母大写
	 * @param word
	 * @return
	 */
	public static String upperCaseInitial(String word) {
		word = StringUtil.trim(word);
		if (word.length() == 0) {
			return "";
		}
		String s = word.substring(0, 1);
		word = s.toUpperCase() + word.substring(1);
		return word;
	}

	/**
	 * 将串首字母小写
	 * @param word
	 * @return
	 */
	public static String lowerCaseInitial(String word) {
		word = StringUtil.trim(word);
		if (word.length() == 0) {
			return "";
		}
		String s = word.substring(0, 1);
		word = s.toLowerCase() + word.substring(1);
		return word;
	}

	/**
	 * 串首连续2个字母大写时候，将第二个字母转成小写
	 * 
	 * @param s
	 * @return
	 */
	public static String removeFirstTwoCapital(String s) {
		int len = 2;
		if (s.length() < len) {
			return s;
		}
		char c0 = s.charAt(0);
		char c1 = s.charAt(1);
		if (Character.isLowerCase(c0) || Character.isLowerCase(c1)) {
			return s;
		}
		return Character.toString(c0) + Character.toString(c1).toLowerCase() + s.substring(2);
	}

	/**
	 * 对字符串左填充空格，实例效果如下 
	 * <pre>   
	 * StringUtils.leftPad("bat", 3)  = "bat"
	 * StringUtils.leftPad("bat", 5)  = "  bat"
	 * </pre>
	 * @param s - 被填充字符串
	 * @param size - 填充到指定长度
	 * @return
	 */
	public static String leftPad(String s, int size) {
		return leftPad(s, size, ' ');
	}

	/**
	 * 对字符串右填充空格，实例效果如下 
	 * <pre>   
	 * StringUtils.rightPad("bat", 3)  = "bat"
	 * StringUtils.rightPad("bat", 5)  = "bat  "
	 * </pre>
	 * @param s - 被填充字符串
	 * @param size - 填充到指定长度
	 * @return
	 */
	public static String rightPad(String s, int size) {
		return rightPad(s, size, ' ');
	}

	/**
	 * 使用指定的字符，对字符串左填充，实例效果如下
	 * <pre>   
	 * StringUtils.leftPad("bat", 3, 'z')  = "bat"
	 * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
	 * </pre>
	 * @param s - 被填充字符串
	 * @param size - 填充到指定长度
	 * @param padChar - 用来填充的字符
	 * @return
	 */
	public static String leftPad(String s, int size, char padChar) {
		if (s == null) {
			return null;
		}
		int pads = size - s.length();
		if (pads <= 0) {
			return s;
		}
		return padding(pads, padChar).concat(s);
	}

	/**
	 * 使用指定的字符，对字符串右填充，实例效果如下
	 * <pre>    
	 * StringUtils.rightPad("bat", 3, 'z')  = "bat"
	 * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
	 * </pre>    
	 * @param s - 被填充字符串
	 * @param size - 填充到指定长度
	 * @param padChar - 用来填充的字符
	 * @return
	 */
	public static String rightPad(String s, int size, char padChar) {
		if (s == null) {
			return null;
		}
		int pads = size - s.length();
		if (pads <= 0) {
			return s;
		}
		return s.concat(padding(pads, padChar));
	}

	/**
	 * 对字符串缩写，用 "..." 来表示省略部分
	 * <pre>    
	 * StringUtils.abbr("abcdefghijklmno", 0, 30)  = "abcdefghijklmno"
	 * StringUtils.abbr("abcdefghijklmno", 0, 10)  = "abcdefg..."
	 * </pre>    
	 * @param s - 被缩写的字符串
	 * @param maxLength - 缩写后能达到的最大长度
	 * @return
	 */
	public static String abbr(String s, int maxLength) {
		if (s == null) {
			return null;
		}
		if (s.length() <= maxLength) {
			return s;
		}
		return s.substring(0, maxLength - 3) + "...";
	}

	/**
	 * 字符填充函数
	 * @param repeat - 填充次数
	 * @param padChar - 用来填充的字符
	 * @return
	 */
	private static String padding(int repeat, char padChar) {
		if (repeat < 0) {
			throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
		}
		final char[] buf = new char[repeat];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = padChar;
		}
		return new String(buf);
	}

	/**
	 * 制作串
	 * @param s - 被复制串
	 * @param times - 复制次数
	 * @return
	 */
	public static String mkstr(String s, int times) {
		StringBuffer buf = new StringBuffer();
		while (times-- > 0) {
			buf.append(s);
		}
		return buf.toString();
	}

	//------------------------ 字符串截取函数 -------------------------------------

	/**
	 * 获得指定字符串的，左边长度为<code>len</code>的字符串
	 * <pre>    
	 * StringUtils.left("abc", 0)   = ""
	 * StringUtils.left("abc", 2)   = "ab"
	 * StringUtils.left("abc", 4)   = "abc"
	 * </pre>
	 * @param s
	 * @param len
	 * @return
	 */
	public static String left(String s, int len) {
		if (s == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (s.length() <= len) {
			return s;
		}
		return s.substring(0, len);
	}

	/**
	 * 获得指定字符串的，右边长度为<code>len</code>的字符串
	 * <pre>   
	 * StringUtils.right("abc", 0)   = ""
	 * StringUtils.right("abc", 2)   = "bc"
	 * StringUtils.right("abc", 4)   = "abc"
	 * </pre>
	 * @param s
	 * @param len
	 * @return
	 */
	public static String right(String s, int len) {
		if (s == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (s.length() <= len) {
			return s;
		}
		return s.substring(s.length() - len);
	}

	/**
	 * 获取指定长度，若超过长度，则添加 ... 表示省略
	 * @param s
	 * @param len
	 * @return
	 */
	public static String abbreviate(String s, int len) {
		if (s == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (s.length() <= len) {
			return s;
		}
		return s.substring(s.length() - len) + "...";
	}

	/**
	 * 获得指定字符串的，从<code>startIndex</code>开始，其后长度为<code>len</code>的字符串
	 * <pre>    
	 * StringUtils.mid("abc", 0, 2)   = "ab"
	 * StringUtils.mid("abc", 0, 4)   = "abc"
	 * </pre>
	 * @param s
	 * @param startIndex
	 * @param len
	 * @return
	 */
	public static String mid(String s, int startIndex, int len) {
		if (s == null) {
			return null;
		}
		if (len < 0 || startIndex > s.length()) {
			return EMPTY;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (s.length() <= (startIndex + len)) {
			return s.substring(startIndex);
		}
		return s.substring(startIndex, startIndex + len);
	}

	/**
	 * 获得两字符之间的字符
	 * <pre>   
	 * StringUtils.substringBetween("[yabcz]", "[", "]") = "yabcz"
	 * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
	 * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 * @param s
	 * @param open
	 * @param close
	 * @return
	 */
	public static String substringBetween(String s, String open, String close) {
		if (s == null || open == null || close == null) {
			return null;
		}
		int start = s.indexOf(open);
		if (start != -1) {
			int end = s.indexOf(close, start + open.length());
			if (end != -1) {
				return s.substring(start + open.length(), end);
			}
		}
		return null;
	}

	//------------------------ 字符串与集合之间的相互转换函数 -------------------------------------

	/**
	 * 将以','为分隔的字符串转换为 String[]
	 * <pre>
	 * StringUtils.toArray("a, b, c") = new String[]{"a", "b", "c"}
	 * </pre>
	 * @param commaDelim
	 * @return
	 */
	public static String[] toArray(String commaDelim) {
		if ((commaDelim == null) || (commaDelim.trim().length() == 0)) {
			return new String[0];
		}
		List<String> list = toList(commaDelim);
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 将以','为分隔的字符串转换为 List
	 * StringUtils.toList("a, b, c") = List {"a", "b", "c"}
	 * @param commaDelim
	 * @return
	 */
	public static List<String> toList(String commaDelim) {
		if ((commaDelim == null) || (commaDelim.trim().length() == 0)) {
			return Collections.emptyList();
		}
		String s = null;
		int len = commaDelim.length();
		char leftChar = '[';
		char rightChar = ']';
		if (commaDelim.charAt(0) == leftChar && commaDelim.charAt(len - 1) == rightChar) {
			s = commaDelim.substring(1, len - 1);
		} else {
			s = commaDelim;
		}
		List<String> list = new ArrayList<String>();
		StringTokenizer token = new StringTokenizer(s, ",");
		while (token.hasMoreTokens()) {
			String item = token.nextToken();
			String trimmed = item.trim();
			list.add(trimmed);
		}
		return list;
	}

	/**
	 * 将以','为分隔的字符串转换为 Set
	 * StringUtils.toSet("a, b, c") = Set {"a", "b", "c"}
	 * @param commaDelim
	 * @return
	 */
	public static Set<String> toSet(String commaDelim) {
		if ((commaDelim == null) || (commaDelim.trim().length() == 0)) {
			return Collections.emptySet();
		}
		String s = null;
		int len = commaDelim.length();
		char leftChar = '[';
		char rightChar = ']';
		if (commaDelim.charAt(0) == leftChar && commaDelim.charAt(len - 1) == rightChar) {
			s = commaDelim.substring(1, len - 1);
		} else {
			s = commaDelim;
		}
		Set<String> set = new LinkedHashSet<String>();
		StringTokenizer token = new StringTokenizer(s, ",");
		while (token.hasMoreTokens()) {
			String item = token.nextToken();
			String trimmed = item.trim();
			set.add(trimmed);
		}
		return set;
	}

	/**
	 * 将以','为分隔的, 且具有key:/= value格式的字符串转换为 Map
	 * <pre> 
	 * StringUtils.toMap("name: foo, age: 21") = Map {name=foo, age=21}
	 * </pre>
	 * @param commaDelim
	 * @return
	 */
	public static Map<String, String> toMap(String commaDelim) {
		if ((commaDelim == null) || (commaDelim.trim().length() == 0)) {
			return Collections.emptyMap();
		}
		String s = null;
		int len = commaDelim.length();
		if (commaDelim.charAt(0) == '{' && commaDelim.charAt(len - 1) == '}') {
			s = commaDelim.substring(1, len - 1);
		} else {
			s = commaDelim;
		}
		Map<String, String> map = new LinkedHashMap<String, String>();
		StringTokenizer token = new StringTokenizer(s, ",");
		while (token.hasMoreTokens()) {
			String item = token.nextToken();
			int index = item.indexOf(':');
			if (index <= 0) {
				index = item.indexOf('=');
			}
			if (index <= 0) {
				continue;
			}
			String key = item.substring(0, index).trim();
			String val = item.substring(index + 1).trim();
			map.put(key, val);
		}
		return map;
	}

	/**
	 * 将数组中的元素转换为，以 '[' 开头、 ']' 结尾的、 ", "为分隔的字符串
	 * <pre>
	 * StringUtils.toString(array[a, b, c]) = "[a, b, c]"
	 * </pre>
	 * @param array
	 * @return
	 */
	public static String toString(Object[] array) {
		if (array == null) {
			return "null";
		}
		if (array.length == 0) {
			return "";
		}
		return toString(array, ", ", 0, array.length);
	}

	/**
	 * 将数组中的自<code>startIndex</code>到<code>endIndex</code>元素, 转换为以 '[' 开头、 ']' 结尾的、 <code>separator</code>为分隔的字符串
	 * @param array 数组
	 * @param separator	分隔符
	 * @param startIndex 开始索引
	 * @param endIndex 结束索引
	 * @return
	 */
	public static String toString(Object[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return "null";
		}
		if (array.length == 0) {
			return "";
		}
		int bufferSize = endIndex - startIndex;
		bufferSize *= ((array[startIndex] == null ? 0x10 : array[startIndex].toString().length()) + separator.length());
		StringBuilder sb = new StringBuilder(bufferSize + 2);
		sb.append('[');
		for (int i = startIndex; i < endIndex; i++) {
			if (i != startIndex) {
				sb.append(separator);
			}
			sb.append(array[i]);
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * 将 Collection 中的元素转换为，以 '[' 开头、 ']' 结尾的、 ", "为分隔的字符串
	 * @param collection
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String toString(Collection collection) {
		return toString(collection, ", ");
	}

	/**
	 * 将 Collection 中的元素,转换为以 '[' 开头、 ']' 结尾的、 <code>separator</code>为分隔的字符串
	 * @param collection
	 * @param separator
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public static String toString(Collection collection, String separator) {
		if (collection == null) {
			return "null";
		}
		int bufferSize = collection.size() * (0x10 + separator.length());
		StringBuilder sb = new StringBuilder(bufferSize + 2);
		sb.append('[');
		Iterator iter = collection.iterator();
		boolean hasNext = iter.hasNext();
		while (hasNext) {
			Object element = iter.next();
			sb.append(element);
			hasNext = iter.hasNext();
			if (hasNext) {
				sb.append(separator);
			}
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * 将 Map 中的元素转换为，以 '{' 开头、 '}' 结尾的、 ','为分隔的, key = value格式的字符串
	 * @param map
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public static String toString(Map map) {
		return toString(map, ", ", " = ");
	}

	/**
	 * 将 Map 中的元素转换为，以 '{' 开头、 '}' 结尾的、 <code>separator</code>为分隔的, key <code>equalSymbol</code> value格式的字符串
	 * @param map
	 * @param separator
	 * @param equalSymbol
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String toString(Map map, String separator, String equalSymbol) {
		if (map == null) {
			return "null";
		}
		int bufferSize = map.size() * (0x10 + separator.length() + equalSymbol.length());
		StringBuffer sb = new StringBuffer(bufferSize + 2);
		sb.append("{");
		Iterator iter = map.entrySet().iterator();
		boolean hasNext = iter.hasNext();
		while (hasNext) {
			Map.Entry entry = (Map.Entry) iter.next();
			sb.append(entry.getKey()).append(equalSymbol).append(entry.getValue());
			hasNext = iter.hasNext();
			if (hasNext) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 将指定对象转换成字符串，并进行首尾空白串剪除
	 * 特别指出，若所给对象为时间、日期对象，将按 yyyy-MM-dd HH:mm:ss 格式进行转换
	 * 若所给对象为 null， 则返回 "" 空字符串
	 * @param value
	 * @return
	 */
	public static String trimOrNullToEmpty(Object value) {
		if (value == null) {
			return "";
		}
		if (value instanceof String) {
			return ((String) value).trim();
		} else if (value instanceof java.util.Date || value instanceof java.sql.Timestamp
				|| value instanceof java.sql.Date) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value).trim();
		}
		return String.valueOf(value).trim();
	}

	/**
	 * 用指定字符 c 链接两个字符串 l, r
	 * @param l - 左串，链接前先剪除首尾空格，然后对其尾部的 c 删除1次（仅删除一次）
	 * @param r - 右串，链接前先剪除首尾空格，然后对其首部的 c 删除1次（仅删除一次）
	 * @param c - 链接字符
	 * @return l + c + r
	 */
	public static String concate(String l, String r, char c) {
		l = l == null ? "" : l.trim();
		int llen = l.length();
		if (llen > 0 && l.charAt(llen - 1) == c) {
			l = l.substring(0, llen - 1);
		}
		r = r == null ? "" : r.trim();
		int rlen = r.length();
		if (rlen > 0 && r.charAt(0) == c) {
			r = r.substring(1);
		}
		return l + c + r;
	}

	/**
	 * 得到一个字符串，在前面增加指定的前缀
	 * @param str 源字符串
	 * @param prefix 指定的前缀
	 * @return 增加了指定前缀的字符串，如果字符串本身就已经以prefix开始，则直接返回原字符串
	 */
	public static String getString(String str, String prefix) {
		boolean b = isEmpty(str) || isEmpty(prefix) || str.startsWith(prefix);
		return b ? str : prefix + str;
	}

	/**
	 * 将16进制字符串转成字节数组
	 * @param strhex
	 * @return
	 */
	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 != 0) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}

	/**
	 * 将字节数组转换成16进制的字符串
	 * @param bts
	 * @return
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	public static String escapeHTMLTags(final String str) {
		if (str == null) {
			return null;
		}
		// 替换时先判断是否存在需要替换的字符,提高性能
		if (str.indexOf('<') == -1 && str.indexOf('>') == -1 && str.indexOf('"') == -1) {
			return str;
		}

		int stringLength = str.length();
		// StringBuilder buf = new StringBuilder();
		StringBuilder buf = new StringBuilder((int) (stringLength * 1.1));
		for (int i = 0; i < stringLength; ++i) {
			char c = str.charAt(i);

			switch (c) {
			case '<':
				buf.append("&lt;");
				break;
			case '>':
				buf.append("&gt;");
				break;
			case '"':
				buf.append("&quot;");
				break;
			default:
				buf.append(c);
			}
		}
		return buf.toString();
	}

	public static boolean isPureNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return str.matches("^\\d+$");
	}

	/**
	 * 判断是否有不可显示字符（ 包括空白字符、控制字符）
	 * @param s
	 * @return
	 */
	public static boolean hasHiddenChar(String s) {
		if (s == null) {
			return false;
		}
		int length = s.length();
		if (length == 0) {
			return false;
		}
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			if (Character.isWhitespace(c)) {
				return true;
			}
			if (Character.isISOControl(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ognl静态方法调用
	 */
	public static String hideMiddle(String input, int leftLen, int rightLen) {
		if (leftLen < 0 || rightLen < 0) {
			throw new IllegalArgumentException();
		}

		if (input == null) {
			return null;
		}

		if (input.length() <= leftLen + rightLen) {
			return input;
		}

		int repeat = input.length() - leftLen - rightLen;
		return new StringBuilder().append(StringUtils.left(input, leftLen)).append(StringUtils.repeat("*", repeat))
				.append(StringUtils.right(input, rightLen)).toString();
	}

	/** 
	 * 把中文转成Unicode码 
	 * @param str 
	 * @return 
	 */
	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			//汉字范围 \u4e00-\u9fa5 (中文)
			if (chr1 >= 19968 && chr1 <= 171941) {
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	public static String redisDeserialize(byte[] bytes) {
		return (bytes == null ? "" : new String(bytes, Charset.forName("UTF8")));
	}

	public static byte[] redisSerialize(Object object) {
		if (object == null) {
			return null;
		} else {
			return String.valueOf(object).getBytes(Charset.forName("UTF8"));
		}
	}

}
