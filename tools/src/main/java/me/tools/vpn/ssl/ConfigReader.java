package me.tools.vpn.ssl;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigReader {
    private Map<String, Map<String, List<String>>> map = null;
    private String currentSection = null;

    public ConfigReader(String path) {
        this.map = new HashMap();

        try {
            File e = new File(path);
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(e));
            InputStreamReader in = new InputStreamReader(is, "GBK");
            BufferedReader reader = new BufferedReader(in);
            this.read(reader);
            in.close();
            is.close();
        } catch (IOException var6) {
            var6.printStackTrace();
            throw new RuntimeException("IO Exception:" + var6);
        }
    }

    private void read(BufferedReader reader) throws IOException {
        String line = null;

        while((line = reader.readLine()) != null) {
            this.parseLine(line);
        }

    }

    private void parseLine(String line) {
        line = line.trim();
        if(!line.matches("^\\#.*$")) {
            if(line.matches("^\\[\\S+\\]$")) {
                String i = line.replaceFirst("^\\[(\\S+)\\]$", "$1");
                this.addSection(this.map, i);
            } else if(line.matches("^\\S+=.*$")) {
                int i1 = line.indexOf("=");
                String key = line.substring(0, i1).trim();
                String value = line.substring(i1 + 1).trim();
                this.addKeyValue(this.map, this.currentSection, key, value);
            }

        }
    }

    private void addKeyValue(Map<String, Map<String, List<String>>> map, String currentSection, String key, String value) {
        if(map.containsKey(currentSection)) {
            Map childMap = (Map)map.get(currentSection);
            if(!childMap.containsKey(key)) {
                ArrayList list = new ArrayList();
                list.add(value);
                childMap.put(key, list);
            } else {
                ((List)childMap.get(key)).add(value);
            }

        }
    }

    private void addSection(Map<String, Map<String, List<String>>> map, String section) {
        if(!map.containsKey(section)) {
            this.currentSection = section;
            HashMap childMap = new HashMap();
            map.put(section, childMap);
        }

    }

    public List<String> get(String section, String key) {
        return this.map.containsKey(section)?(this.get(section).containsKey(key)?(List)this.get(section).get(key):null):null;
    }

    public Map<String, List<String>> get(String section) {
        return this.map.containsKey(section)?(Map)this.map.get(section):null;
    }

    public Map<String, Map<String, List<String>>> get() {
        return this.map;
    }

    public String getStringValue(String section, String item) {
        String sRet = "";
        Map sectionmap = this.get(section);
        if(sectionmap == null) {
            return sRet;
        } else {
            List list = (List)sectionmap.get(item);
            sRet = (String)list.get(0);
            return sRet;
        }
    }

    public int getIntValue(String section, String item) {
        String sRet = "";
        Map sectionmap = this.get(section);
        if(sectionmap == null) {
            return 0;
        } else {
            List list = (List)sectionmap.get(item);
            sRet = (String)list.get(0);
            int nRet;
            if(sRet == null) {
                nRet = 0;
            } else {
                nRet = Integer.parseInt(sRet);
            }

            return nRet;
        }
    }

    public boolean getBoolValue(String section, String item) {
        String sRet = "";
        Map sectionmap = this.get(section);
        if(sectionmap == null) {
            return false;
        } else {
            List list = (List)sectionmap.get(item);
            sRet = (String)list.get(0);
            return sRet == null?false:sRet.equalsIgnoreCase("1");
        }
    }
}
