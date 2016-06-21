package me.tools;

/**
 * 字符串扩展工具
 *
 * @author leizhimin 2008-10-23 13:23:09
 */
public class StringUtils {

    /**
     * 将一个字符串的首字母改为大写或者小写
     *
     * @param srcString 源字符串
     * @param flag      大小写标识，ture小写，false大些
     * @return 改写后的新字符串
     */
    public static String toLowerCaseInitial(String srcString, boolean flag) {
        StringBuilder sb = new StringBuilder();
        if (flag) {
            sb.append(Character.toLowerCase(srcString.charAt(0)));
        } else {
            sb.append(Character.toUpperCase(srcString.charAt(0)));
        }
        sb.append(srcString.substring(1));
        return sb.toString();
    }

    /**
     * 将一个字符串按照句点（.）分隔，返回最后一段
     *
     * @param clazzName 源字符串
     * @return 句点（.）分隔后的最后一段字符串
     */
    public static String getLastName(String clazzName) {
        String[] ls = clazzName.split("\\.");
        return ls[ls.length - 1];
    }

    public static String trimEnd(String str) {
        return str.substring(0, str.length() - 1);
    }

    public static void main(String[] args) {
        System.out.println("转换后的字符串为：" + toLowerCaseInitial("Attxxdds", true));
        System.out.println("--------------");
        System.out.println("最后一段字符串为：" + getLastName("aaa.bbb.ccc"));
    }
}
