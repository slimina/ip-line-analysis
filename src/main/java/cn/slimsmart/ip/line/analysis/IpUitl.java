package cn.slimsmart.ip.line.analysis;

public class IpUitl {

	public static Long getIP(String ipaddr) {
		String ip[] = ipaddr.split("\\.");
		Long ipLong = 256 * 256 * 256 * Long.parseLong(ip[0]) + 256 * 256 * Long.parseLong(ip[1]) + 256 * Long.parseLong(ip[2]) + Long.parseLong(ip[3]);
		return ipLong;
	}

	public static String getIP(Long ipaddr) {
		long y = ipaddr % 256;
		long m = (ipaddr - y) / (256 * 256 * 256);
		long n = (ipaddr - 256 * 256 * 256 * m - y) / (256 * 256);
		long x = (ipaddr - 256 * 256 * 256 * m - 256 * 256 * n - y) / 256;
		return m + "." + n + "." + x + "." + y;
	}
	
	public static String  getIPRang(String ip ,long len){
		int n = 32-(Long.toBinaryString(len).length()-1);
		return ip+"/"+n;
	}
	
	
	
	public static void main(String [] aa){
		System.out.println(getIPRang("14.196.0.0",131072));
	}
}
