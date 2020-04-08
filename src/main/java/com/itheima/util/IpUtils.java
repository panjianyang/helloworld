package com.itheima.util;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;


public class IpUtils {
	public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	public static final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");

	public static String longToIpV4(long longIp) {
		int octet3 = (int) ((longIp >> 24) % 256);
		int octet2 = (int) ((longIp >> 16) % 256);
		int octet1 = (int) ((longIp >> 8) % 256);
		int octet0 = (int) ((longIp) % 256);
		return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
	}

	public static long ipV4ToLong(String ip) {
		String[] octets = ip.split("\\.");
		return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
				+ (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
	}

	public static boolean isIPv4Private(String ip) {
		long longIp = ipV4ToLong(ip);
		return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
				|| (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
				|| longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
	}

	public static boolean isIPv4Valid(String ip) {
		return pattern.matcher(ip).matches();
	}

	public static String getIpFromRequest(HttpServletRequest request) {
		String ip;
		boolean found = false;
		if ((ip = request.getHeader("x-forwarded-for")) != null) {
			StrTokenizer tokenizer = new StrTokenizer(ip, ",");
			while (tokenizer.hasNext()) {
				ip = tokenizer.nextToken().trim();
				if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			try {
				ip = getClientIpAddr(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ip;
	}

	/**
	 * 获取访问者IP
	 *
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 *
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 *
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}
}
