注意1：先配置根布局gradle：butterknife必须配置
   2：

1：封装BaseActivity，包括toast，包括加载中、加载失败、加载成功、三种状态布局。包含butterknife和eventbus
    状态栏 默认是有白色状态栏的，主要包含返回按钮、标题、最右边标题、和最右边图标。
使用说明：
1：配置butterknife：(参考地址：https://github.com/JakeWharton/butterknife)
    1.1：项目根目录的build.gradle -> buildscript -> repositories ->
           加上  mavenCentral()
    1.2：项目根目录的build.gradle -> buildscript -> dependencies ->
            加上 classpath 'com.jakewharton:butterknife-gradle-plugin:10.2.1'
2：配置适配器BaseRecyclerViewAdapterHelper：(参考地址：https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
    2.1：项目根目录的build.gradle -> allprojects  -> repositories -> 加上  maven { url 'https://jitpack.io' }
3：项目主题替换成 BaseAppTheme
