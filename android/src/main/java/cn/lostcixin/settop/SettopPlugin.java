package cn.lostcixin.settop;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import static android.content.Context.ACTIVITY_SERVICE;

/** SettopPlugin */
public class SettopPlugin implements FlutterPlugin, MethodCallHandler {
  private Context appContext;
  private MethodChannel methodChannel;

  public static void registerWith(PluginRegistry.Registrar registrar) {
    final SettopPlugin instance = new SettopPlugin();
    instance.onAttachedToEngine(registrar.context(), registrar.messenger());
  }

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger());
  }

  private void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
    this.appContext = applicationContext;
    methodChannel = new MethodChannel(messenger, "settop");
    methodChannel.setMethodCallHandler(this);
  }
  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("setTopApp")) {
        setTopApp();
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }

  /**
   * 判断本地是否已经安装好了指定的应用程序包
   *
   * @param packageNameTarget ：待判断的 App 包名，如 微博 com.sina.weibo
   * @return 已安装时返回 true,不存在时返回 false
   */
  public boolean appIsExist(String packageNameTarget) {
    if (!"".equals(packageNameTarget.trim())) {
      PackageManager packageManager = this.appContext.getPackageManager();
      List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
      for (PackageInfo packageInfo : packageInfoList) {
        String packageNameSource = packageInfo.packageName;
        if (packageNameSource.equals(packageNameTarget)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 将本应用置顶到最前端
   * 当本应用位于后台时，则将它切换到最前端
   */
  public void setTopApp() {
    if (!isRunningForeground(this.appContext)) {
      /**获取ActivityManager*/
      ActivityManager activityManager = (ActivityManager) this.appContext.getSystemService(ACTIVITY_SERVICE);

      /**获得当前运行的task(任务)*/
      List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(100);
      for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
        /**找到本应用的 task，并将它切换到前台*/
        if (taskInfo.topActivity.getPackageName().equals(this.appContext.getPackageName())) {
          activityManager.moveTaskToFront(taskInfo.id, 0);
          break;
        }
      }
    }
  }

  /**
   * 判断本应用是否已经位于最前端
   *
   * @param context
   * @return 本应用已经位于最前端时，返回 true；否则返回 false
   */
  public static boolean isRunningForeground(Context context) {
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
    /**枚举进程*/
    for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
      if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
        if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
          return true;
        }
      }
    }
    return false;
  }
}
