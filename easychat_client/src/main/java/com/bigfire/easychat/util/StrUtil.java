package com.bigfire.easychat.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/22  8:58
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class StrUtil {
    /**
     * MD5参数，让方法进行32位的MD5加密
     */
    public final static int MD5_32 = 0;
    /**
     * MD5参数，让方法进行16位的MD5加密
     */
    public final static int MD5_16 = 1;
    /**
     * 本工具类内的错误信息，可以通过类名加本属性查看错误信息
     */
    public static String errorMsg="";
    /**
     * 获取两个指定文本中间的字符串
     * @param source 源字符串
     * @param start 指定文本字符串头
     * @param end	指定文本字符串尾
     * @return 返回一个中间字符串
     */
    public static String getBetweenfirst(String source,String start,String end)
    {
        return getBetween(source, start, end, 1);
    }
    /**
     * 获取两个指定文本中间的字符串
     * @param source 源字符串
     * @param start 指定文本字符串头
     * @param end	指定文本字符串尾
     * @param index 如果文本中间有多个，指定欲获取的位置
     * @return 返回一个指定位置的中间字符串
     */
    public static String getBetween(String source,String start,String end,int index)
    {
        String content[]=null;
        String[] starts= source.split(start);
        content = new String[starts.length];
        for (int i = 0; i < starts.length; i++) {
            if (starts[i].contains(end)) {
                String[] ends=starts[i].split(end);
                content[i]=ends[0];
            }
        }
        return content[index];
    }
    /**
     * 获取两个指定文本中间的全部字符串
     * @param source 源字符串
     * @param start 指定文本字符串头
     * @param end	指定文本字符串尾
     * @return 返回一个指定位置的中间字符串数组
     */
    private static String[] result;
    public static String[] getBetweenAll(String resources,String start,String end){
        if(resources.contains(start)&&resources.contains(end)){
            String[] starts = resources.split(start);
            result=new String[starts.length-1];
            for (int i = 1; i < starts.length; i++) {
                String[] ends = starts[i].split(end);
                result[i-1]=ends[0];
            }
        }else {
            throw new RuntimeException("\n\n字符串中可能不存在你找的文本\n");
        }
        return result;
    }
    /**
     * 查找一文本在另一文本中最先出现的位置，位置值从 1 开始。如果未找到，返回-1。
     * @param source 源字符串
     * @param find	要找文本
     * @return 返回一个整数值
     */
    public static int findStringindex(String source,String find) {
        return findStringindex(source, find, 1);
    }
    /**
     * 查找一文本在另一文本中第几次出现的位置，位置值从 1 开始。如果未找到，返回-1。
     * @param source  源字符串
     * @param find	要找文本
     * @param index 第几次
     * @return 返回一个整数值
     */
    public static int findStringindex(String source,String find,int index) {
        int	findstrlength=find.length();

        if (source.contains(find)) {
            if (source.startsWith(find))
            {
                return 0;
            }
            String[] strs=source.split(find);
            if(index>strs.length){
                return-1;
            }else {
//				for(String s:strs)
//					System.out.println(s);
                int indexs=0;
                index--;
                for (int i = 0; i <=index; i++) {
                    if (i==0) {
                        indexs=strs[0].length();
                    }else {
                        indexs=indexs+strs[i].length()+findstrlength;
                    }
                }
                return indexs;
            }
        }else {
            return -1;
        }
    }
    /**
     * 统计某个字符串在源字符串中一共出现的次数
     * @param source 源字符串
     * @param find	欲要统计的字符串
     * @return 返回一共出现的次数
     */
    public static int countTextContainNumber(String source,String find){
        String[] strs=source.split(find);
        int number=strs.length-1;
        if (source.startsWith(find)) {
            number++;
        }
        if (source.endsWith(find)) {
            number++;
        }
        return number;
    }
    /**
     * 把一个字符串进行MD5加密
     * @param source 欲要加密的文本
     * @return 32位的加密后的文本
     */
    public static String MD5(String source) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
    /**
     * 把一个字符串进行MD5加密   参数2错误 会返回-1
     * @param source 欲要加密的文本
     * @param digit 需要的位数16位 或者32位  0表示32位，1表示16位
     * @return 需要的加密文本，16位或者32位
     */
    public static String MD5(String source,int digit) {
        if (digit==MD5_32) {
            return MD5(source);
        }else if(digit==MD5_16){
            return MD5(source).substring(8, 24);
        }else {
            errorMsg="参数数值错误\n\n参数digit说明:\nMD5_32表示获取32位MD5加密值也可写0\nMD5_16表示获取16位MD5加密值也可写1\n";
            return "-1";
        }
    }
    /**
     *   在第一次某段字符串间插入字符串    返回插入后的文本
     *   批量操作，可以在多对标签中的第一对标签间插入字符串
     * @param source 源文本
     * @param start 字符串头
     * @param end  字符串尾
     * @param insert  欲插入文本
     * @return 返回插入后的文本
     */
    public static String insertfirst(String source,String start,String end,String insert){
        return insert(source, start, end, insert, 1);
    }
    /**
     *   在最后一次某段字符串间插入字符串    返回插入后的文本
     *   批量操作，可以在多对标签中的最后一对标签间插入字符串
     * @param source 源文本
     * @param start 字符串头
     * @param end  字符串尾
     * @param insert  欲插入文本
     * @return  返回插入后的文本
     */
    public static String insertend(String source,String start,String end,String insert){
        int temp=0;
        String[] strs=source.split(start);
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].startsWith(end))
                temp++;
        }
        return insert(source, start, end, insert, temp);
    }
    /**
     * 批量在某个字符串后面加入一段文本
     * @param source 源文本
     * @param start 插入位置前的字符串是什么
     * @param insert  欲要插入的文本
     * @return   返回插入后的文本
     */
    public static String insertAfterAll(String source,String start,String insert){
        String result = "";
        String[] strs=source.split(start);
        for (int i = 0; i < strs.length; i++) {
            result+=strs[i]+start+insert;
        }
        return result;
    }
    /**
     * 在指定某个字符串后面加入一段文本  多个标签时，在第几个标签后插入，
     * 参数num如果大于文本中start字符串出现的总次数，将返回源文本
     * @param source 源文本
     * @param start 插入位置前的字符串是什么
     * @param insert 欲要插入的文本
     * @param num 第几次出现的位子后
     * @return     返回插入后的文本
     */
    public static String insertAfter(String source,String start,String insert,int num){
        String result = "";
        int temp=0;
        if (source.endsWith(start)) {
            temp++;
        }
        String[] strs=source.split(start);
        for (int i = 0; i < strs.length; i++) {
            if (num==temp) {
                result+=strs[i]+start+insert;
            }else {
                result+=strs[i]+start;
            }
            temp++;
        }
        if(num>=temp){
            errorMsg="参数越界异常\n\n文本中一共只有"+temp+"对元素，所以不能在"+num+"个元素内插入数组\n";
            return source;
        }
        return result;
    }
    /**
     * 在指定两个字符串间插入一段文本，
     * 参数num如果大于出现的总次数将返回源文本
     * @param source  源文本
     * @param start 插入位置前面的字符串是什么
     * @param end 插入位置后面的字符串是什么
     * @param insert 欲要插入的文本
     * @param num  第几次出现
     * @return 返回插入后的文本
     */
    public static String insert(String source,String start,String end,String insert,int num){
        String result="";
        int temp=0;
        if (source.startsWith(start)) {
            temp++;
        }else if(source.endsWith(end)){
            temp++;
        }
        String[] strs=source.split(start);
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].startsWith(end)) {
                if (temp==num) {
                    result = result+start+insert+strs[i];
                }
                else{
                    if (i!=0) {
                        result+=start+strs[i];
                    }else {
                        result+=strs[i];
                    }
                }
                temp++;
            }else {
                result+=strs[i];
            }
        }
        if(num>=temp)
        {
            errorMsg="参数越界异常\n\n文本中一共只有"+temp+"对元素，所以不能在"+num+"个元素内插入数组\n";
            return source;
        }
        return result;
    }
    /**
     * 批量处理在start和end间插入一段文本。
     * @param source 源文本
     * @param start 字符串头
     * @param end  字符串尾
     * @param insert 欲要插入的文本
     * @return 返回插入后的文本
     */
    public static String insertAll(String source,String start,String end,String insert){
        String result="";
        String[] strs=source.split(start);
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].startsWith(end)) {
                if (i!=strs.length)
                    result = result+start+insert+strs[i];
            }else {
                result+=strs[i];
            }
        }
        return result;
    }
    /**
     * 在指定位置后插入一段文本
     * @param source 源文本
     * @param index  要插入的位置
     * @param insert  要插入的文本
     * @return 返回插入后的文本
     */
    public static String insert(String source,int index,String insert){
        String result="";
        String[] strs=source.split(source.charAt(index-1)+"", 2);
        result=strs[0]+source.charAt(index-1)+insert+strs[1];
        return result;
    }
    /**
     * 获取第几位之后的所有文本
     * @param source 源文本
     * @param num 位置
     * @return 返回截取后的文本
     */
    public static String getStringright(String source, int index) {
        return source.substring(source.length()-index);
    }
    /**
     * 获取前多少位的文本
     * @param source 源文本
     * @param num 位置
     * @return 返回截取后的文本
     */
    public static String getStringleft(String source, int index) {
        return source.substring(0,index);
    }
    public static String getLeft(String source, String cut) {
        int index=findStringindex(source, cut);
        return source.substring(0,index);
    }
    public static String getRight(String source, String cut) {
        int index=findStringindex(source, cut);

        return source.substring(index+1,source.length());
    }

    /**
     * 打印字符串并且换行
     * @param object 字符串
     */
    public static void pln(Object object) {
        System.out.println(object);
    }
    /**
     * 打印字符
     * @param object 字符串
     */
    public static void p(Object object) {
        System.out.print(object);
    }
    /**
     * 打印字符 并且显示打印时的行号
     * @param object 字符串
     */
    public static void print(Object object)
    {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        String where = ste.getMethodName()+"(" +ste.getFileName()+":"+ ste.getLineNumber()+")";
        if(System.getProperties().getProperty("os.name").toLowerCase().contains("linux")){
            where = ste.getMethodName()+"(" +ste.getFileName()+":"+ ste.getLineNumber()+")";
        }else {
            where = ste.getClassName()+"."+ste.getMethodName()+"(" +ste.getFileName()+":"+ ste.getLineNumber()+")";
        }
        System.out.print("["+where+"]");
        System.out.println(object);
    }
}
