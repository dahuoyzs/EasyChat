package com.bigfire.easychat.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ IDE    ：Eclipse
 * @ Author ：dahuo
 * @ Date   ：2017年8月14日  上午10:20:33
 * @ Addr   ：China ZhengZhou
 * @ email  ：835476090@qq.com
 */
public class IoUtil
{
	private static String result;
	public static String readStringFormFile(String path)
	{
		File f = new File(path);
		try
		{
			FileInputStream fis = new FileInputStream(f);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			// result = new String(b,"utf-8");
			result = new String(b);
			fis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public static Boolean writeStringToFile(String path, String resources)
	{
		FileOutputStream fos;
		File f = new File(path);
		try
		{
			fos = new FileOutputStream(f);
			fos.write(resources.getBytes());
			fos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static byte[] InputStreamToByteArray(InputStream input)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try
		{
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer)))
			{
				output.write(buffer, 0, n);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return output.toByteArray();
	}
	public static String InputStreamToString(InputStream input)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try
		{
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer)))
			{
				output.write(buffer, 0, n);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		String res = new String(output.toByteArray());
		return res;
	}
}
