package com.ipinpar.app.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionUtils {
 /**
  * 程序包名
  */
 private final static String PACKAGE_NAME = "com.ipinpar.app";

 /**
  * 获取当前程序版本号
  * @param context
  * @return
  */
 public static int getCurrentVersionCode(Context context) {
  try {
   return context.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionCode;
  } catch (NameNotFoundException e) {
   return -1;
  }
 }

 /**
  * 获取当前程序版本名称
  * @param context
  * @return
  */
 public static String getCurrentVersionName(Context context) {
  try {
   return context.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionName;
  } catch (NameNotFoundException e) {
   return "";
  }
 }

 /**
  * 根据版本名判断得到的版本号是否为新版本
  * 
  * @param versionName  得到的版本号
  * @param currentVersionName  当前版本号
  * @return
  */
 public static boolean isNewVersion(String versionName,
   String currentVersionName) {
  return versionName.compareToIgnoreCase(currentVersionName) > 0;

 }

 /**
  * 根据版本号判断得到的版本号是否为新版本
  * 
  * @param versionCode得到的版本号
  * @param currentVersionCode
  *            当前版本号
  * @return
  */
 public static boolean isNewVersion(int versionCode, int currentVersionCode) {
  return versionCode > currentVersionCode;

 }

}