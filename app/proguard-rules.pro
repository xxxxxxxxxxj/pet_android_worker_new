
# This is a configuration file for ProGuard.
# Author: Gao Yu
# http://proguard.sourceforge.net/index.html#manual/usage.html

# 代码混淆压缩比，在0~7之间，默认为5,一般不下需要修改
-optimizationpasses 5
# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共的库的类
# 默认跳过，有些情况下编写的代码与类库中的类在同一个包下，并且持有包中内容的引用，此时就需要加入此条声明
-dontskipnonpubliclibraryclasses
# 不做预检验，preverify是proguard的四个步骤之一
# Android不需要preverify，去掉这一步可以加快混淆速度
-dontpreverify
# 有了verbose这句话，混淆后就会生成映射文件 包含有类名->混淆后类名的映射关系 然后使用printmapping指定映射文件的名称
-verbose
# 指定混淆时采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不改变
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# This dnsjava class uses old Sun API
-dontnote org.xbill.DNS.spi.DNSJavaNameServiceDescriptor
-dontwarn org.xbill.DNS.spi.DNSJavaNameServiceDescriptor

-dontwarn org.apache.commons.codec.binary.*
-dontwarn org.apache.log4j.*
-dontwarn net.sf.json.*
-dontwarn android.net.http.**
-dontwarn org.codehaus.jackson.map.ext.*
-dontwarn com.tendcloud.tenddata.**
-dontwarn android.support.**
-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep,allowshrinking class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes *Annotation*,Signature,Exceptions,InnerClasses,SourceFile,LineNumberTable

-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

#友盟
-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}

-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
 *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.com.umeng.soexample.R$*{
public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
public static final int *;
    }
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keepattributes Signature
#友盟 end

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public static void createNotificationForPushMessage(android.content.Context, java.lang.String);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class sun.misc.Unsafe { *; }
-keep class * extends java.lang.annotation.Annotation { *; }
-keep class org.codehaus.jackson.** { *; }
-keep class org.jivesoftware.** { *; }
-keep class org.akita.** { *; }
-keep class org.akita.util.JsonMapper
-keep class android.support.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep class com.pingan.papd.ui.views.** { *; }
-keep class **.R
-keep class **.R$* {
    <fields>;
}

##---------------Begin: proguard configuration for #paiti_sdk  ----------
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.edu.live.baseui.**{ *; }
-keep class com.edu.live.baseapi.**{ *; }
-keep class com.edu.student.mo.**{ *; }
-keep class cn.robotpen.model.**{ *; }
-keep class com.edu.student.im.model.**{ *; }
-keep class com.edu.student.resp.**{ *; }
-keep class com.google.gson.** { *;}

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.app.Fragment
##---------------End: proguard configuration for #paiti_sdk  ----------


##---------------Begin: proguard configuration for libs  ----------
##---------------End: proguard configuration for libs  ----------


##---------------Begin: proguard configuration for #this app  ----------
-keep class com.xuexibao.livewb.student.model.** { *;}
-keep class com.xuexibao.livewb.student.api.** { *;}
-keep class com.xuexibao.livewb.student.api.student.** { *;}
-keep class com.xuexibao.livewb.student.ui.common.** { *;}
-keep class com.xuexibao.livewb.student.ui.widget.** { *;}
-keep interface com.xuexibao.livewb.student.biz.** { *;}
-keep class * implements com.xuexibao.livewb.student.model.MobileObject { *;}
-keep class * implements com.xuexibao.livewb.student.api.internal.IRequest { *;}
-keep class * implements com.xuexibao.livewb.student.api.internal.IResponse { *;}
-keep class com.liveaa.techrecorder.ui.loginmine.TeacherInfoWebActivity$MyJavaScriptInterface  {
*;
}
-keepattributes *JavascriptInterface*
##---------------End: proguard configuration for #student_app  ----------

##--------------Start:proguard configuration for # umeng socialize -----------

-keep public class com.xuexibao.livewb.student.R$*{
    public static final int *;
}
-keep public class com.xuexibao.livewb.student.R$*{
    public static final int *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
##--------------End:proguard configuration for # umeng socialize -----------



##-------------- for app ----------------
-keep class com.dida.live.recorder.biz.api.** {*;}
-keep class com.dida.live.recorder.util.Env {*;}
-keep class com.dida.live.recorder.mo.** {*;}

##-------------- end: ----------------

#============== retrofit ===============
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#============== Glide ==================
-keep public class * implements com.bumptech.glide.module.GlideModule

#============== Data-Binding ===========
-keep class android.databinding.** { *; }
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepattributes *Annotation*
-keepattributes javax.xml.bind.annotation.*
-keepattributes javax.annotation.processing.*
-keepclassmembers class * extends java.lang.Enum { *; }
-keepclasseswithmembernames class android.**
-keepclasseswithmembernames interface android.**

#-dontobfuscate

-libraryjars  <java.home>/lib/rt.jar
-libraryjars  <java.home>/lib/jce.jar
-dontwarn

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#//-keep,allowshrinking class * extends android.content.BroadcastReceiver

-keep class com.jph.takephoto.** { *; }
-dontwarn com.jph.takephoto.**

-keep class com.darsh.multipleimageselect.** { *; }
-dontwarn com.darsh.multipleimageselect.**

-keep class com.soundcloud.android.crop.** { *; }
-dontwarn com.soundcloud.android.crop.**
#butter knife

# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-keep class **$$ViewBinder { *; }

-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings

-keep public class com.coremedia.** {*;}
-keep public class com.googlecode.** {*;}

#RxBus
-dontwarn com.hwangjr.rxbus.**
-keep class com.hwangjr.rxbus.** { *;}
-keep class rx.internal.util.unsafe.** { *; }
-keepattributes *Annotation
-keepclassmembers class ** {
    @com.hwangjr.rxbus.annotation.Subscribe public *;
    @com.hwangjr.rxbus.annotation.Produce public *;
}

#ijkplayer
-keep class tv.danmaku.ijk.media.player.** { *; }
-dontwarn tv.danmaku.ijk.media.player.*
-keep interface tv.danmaku.ijk.media.player.** { *; }

-keep class com.blankj.utilcode.** { *; }
-keepclassmembers class com.blankj.utilcode.** { *; }
-dontwarn com.blankj.utilcode.**

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}

#crop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-keep class com.jph.takephoto.** { *; }
-dontwarn com.jph.takephoto.**

-keep class com.darsh.multipleimageselect.** { *; }
-dontwarn com.darsh.multipleimageselect.**

-keep class com.soundcloud.android.crop.** { *; }
-dontwarn com.soundcloud.android.crop.**

#ILive start

-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.*
#ILive end


#shared sdk start
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
#shared sdk end

#vLayout start
-keepattributes InnerClasses
-keep class com.alibaba.android.vlayout.ExposeLinearLayoutManagerEx { *; }
-keep class android.support.v7.widget.RecyclerView$LayoutParams { *; }
-keep class android.support.v7.widget.RecyclerView$ViewHolder { *; }
-keep class android.support.v7.widget.ChildHelper { *; }
-keep class android.support.v7.widget.ChildHelper$Bucket { *; }
-keep class android.support.v7.widget.RecyclerView$LayoutManager { *; }
#vLayout end

#极光start
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#极光end

#react native start

-keep class * extends com.facebook.react.bridge.JavaScriptModule { *; }
-keep class * extends com.facebook.react.bridge.NativeModule { *; }
-keepclassmembers,includedescriptorclasses class * { native <methods>; }
-keepclassmembers class *  { @com.facebook.react.uimanager.UIProp <fields>; }
-keepclassmembers class *  { @com.facebook.react.uimanager.annotations.ReactProp <methods>; }
-keepclassmembers class *  { @com.facebook.react.uimanager.annotations.ReactPropGroup <methods>; }

-keep class com.facebook.** { *; }
-dontwarn com.facebook.react.**

#react

#hotfix start
#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-dontwarn com.alibaba.sdk.android.utils.**
#防止inline
-dontoptimize
#hotfix end