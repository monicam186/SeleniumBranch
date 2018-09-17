package com.branch.qatest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;


class Logs {
	private static String sTestName;
	public static String createLogFile(String sTest_FullName) throws FileNotFoundException, InterruptedException {
		sTestName = sTest_FullName;
		String sTestLogFile = System.getProperty("user.dir")+ "/Reports/" + sTestName + ".log";
		String sCalledMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		String msg = "\n" + (new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime())) + "::" + Thread.currentThread().getStackTrace()[2].getClassName() + "." + sCalledMethodName + "\n";
		System.out.println(msg);
		PrintWriter writerOut = new PrintWriter(new FileOutputStream(new File(sTestLogFile), true));
		writerOut.append(msg); writerOut.close();
		return sCalledMethodName;
	}

	public static void log(String msg) throws FileNotFoundException {
		
		String sTestLogFile = System.getProperty("user.dir") + "/Reports/" + sTestName + ".log";
		msg = msg.replace("^^BLUE", "");
	
		if (StringUtils.countMatches(msg, "\n")>15) {
			for (int i =0;i <15; i++) {
				System.out.println(msg.split("\n")[i]);
			}
		}
		else
		System.out.println(msg);		
		
		PrintWriter writerOut = new PrintWriter(new FileOutputStream(new File(sTestLogFile), true));
		writerOut.append(msg + "\n"); writerOut.close();
	}
}