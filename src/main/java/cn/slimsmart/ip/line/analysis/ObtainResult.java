package cn.slimsmart.ip.line.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ObtainResult {

	/**
	 * 存储netname和line名称映射的文件
	 */
	private final static String NETNAME_LINE = "src/main/resources/netname-line.conf";

	public static void main(String[] args) {
		File file = new File("src/main/resources/ip-line");
		File unknown = new File("src/main/resources/ip-line-unknown");
		getSaveIpInfo(file,unknown,"src/main/resources/data/");

	}

	public static void getSaveIpInfo(File ipLineFile, File unknownFile, String dataPath) {
		Map<String, List<String>> result = analysis(ipLineFile, unknownFile);
		if(!dataPath.endsWith("/") && !dataPath.endsWith("\\")){
			dataPath +="/";
		}
		for (Entry<String, List<String>> en : result.entrySet()) {
			File file =new File(dataPath+en.getKey());
			saveFile(file,en.getKey(),en.getValue());
		}
	}

	public static void saveFile(File file, String line, List<String> ipList) {
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(file));
			wr.write("acl \"" + line + "\" {");
			wr.newLine();
			if (ipList.size() > 0) {
				for (String ip : ipList) {
					wr.write("\t" + ip + ";");
					wr.newLine();
				}
			}
			wr.write("};");
			wr.newLine();
			wr.write("#EOF");
			wr.flush();
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Map<String, String> getNetNameLineMap() {
		Map<String, String> mapping = new HashMap<String, String>();
		try {
			File file = new File(NETNAME_LINE);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (!line.equals("")) {
					String[] arr = line.split("\\s+");
					mapping.put(arr[0].trim(), arr[1].trim());
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping;
	}

	private static Map<String, List<String>> analysis(File ipLineFile, File unknownFile) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		Map<String, String> netNameLineMap = getNetNameLineMap();
		Set<String> lineSet = new HashSet<String>();
		for (Entry<String, String> en : netNameLineMap.entrySet()) {
			lineSet.add(en.getValue());
		}
		for (String line : lineSet) {
			result.put(line, new ArrayList<String>());
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(ipLineFile));
			BufferedWriter wr = new BufferedWriter(new FileWriter(unknownFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (!line.equals("")) {
					String[] arr = line.split("\\s+");
					String l = IpUitl.getLineByNetname(arr[2], netNameLineMap);
					if (l == null || "".equals(l.trim())) {
						l = IpUitl.getLineByNetname(arr[3], netNameLineMap);
					}
					if (l != null && !"".equals(l = l.trim())) {
						result.get(l).add(IpUitl.getIPRang(arr[0], Long.valueOf(arr[1])));
					} else {
						wr.write(line);
						wr.newLine();
					}
				}
			}
			wr.flush();
			wr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
