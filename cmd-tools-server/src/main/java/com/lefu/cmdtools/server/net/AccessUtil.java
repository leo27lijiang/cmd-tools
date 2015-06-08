package com.lefu.cmdtools.server.net;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccessUtil {
	/**
	 * 匹配有效的IP规则，IP的4个段都可以使用*做匹配
	 */
	public static final String regex = "(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)";
	public static final Pattern pattern = Pattern.compile(regex);
	
	/**
	 * 检查 ip 是否匹配 ips 中的规则
	 * @param ips
	 * @param ip
	 * @return
	 */
	public static boolean testAccess(String[] ips, String ip) {
		String[] domains = ip.split("[\\.]");
		for (String s : ips) {
			String[] segment = s.split("[\\.]");
			boolean isMatched = true;
			for (int i = 0; i < 4; i++) {
				if (!matching(segment[i], domains[i])) {
					isMatched = false;
					break;
				}
			}
			if (isMatched) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean matching(String reg, String real) {
		if (reg.equals("*")) {
			return true;
		} else {
			return reg.equals(real);
		}
	}
	
	public static boolean isIP(String ip) {
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}
	
	public static void main(String[] args) {
		String[] reg = new String[]{"192.168.13.*"};
		String id = "192.168.14.64";
		System.out.println(testAccess(reg, id));
	}
	
}
