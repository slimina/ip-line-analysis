package cn.slimsmart.ip.line.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Test {
	
	static String ipv4="http://www.cnisp.org/Home/Ipv4?ip=";
	static String ipas = "http://www.cnisp.org/Home/IpAs?searchAction=ip&keyword=";
	
	public static void main(String[] args) throws Exception {
		File file = new File("src/main/resources/ip-line-unknown");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (!line.equals("")) {
				 String[] aa =line.split("\\s+");
				 if(getUrlContent(ipv4+aa[0]).indexOf("该段地址不属于中国使用")!=-1 && getUrlContent(ipas+aa[0]).indexOf("<tr>            <td>            </td>            <td>            </td>            <td>            </td>            <td>            </td>            <td>            </td>            <td>            </td>        </tr>")!=-1){
					 System.out.println(aa[2]);
				 }
			}
		}
		br.close();
	}
	static String getUrlContent(String urlIP) throws Exception{
		URL url = new URL(urlIP);
		URLConnection conn = url.openConnection();
		InputStream inStream = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
}
