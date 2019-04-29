### 1. xUtils详解
*xUtils是基于Afinal开发的目前功能比较完善的一个android开源框架*
- xUtils包含了orm、http(s)、image、view注解。
- 基于高效稳定的orm工具，http模块得以更加方便的实现cookie(支持domain、path、expiry)和缓存(Cache-Control、Last-Modified、ETag)的支持。
- 有强大的http及其下载缓存的支持，image模块的实现相当的简洁，并且支持回收被view持有，但被Mem Cache移除的图片，减少页面回退时的闪烁
- view注解模块支持了各种view注入和事件绑定，包括拥有多种方法的listenre的支持。
    #### 其他的特性
- 支持超大文件的上传(2G以上)
- 更全面的http请求协议支持
- 拥有更加灵活的ORM，和greenDao一致的性能
- 更多的事件注解支持且不受混淆影响
- 图片支持gif(受系统兼容性影响，部分gif文件只能静态显示)，webp；支持圆角、圆形、方形等裁剪，支持自动旋转

**总的说，xUtils主要有四个模块：注解模块、网络模块、图片加载模块、数据库模块**

### 2. 使用前的配置
- 在gradle中进行以来的配置
```
implementation 'org.xutils:xutils:3.5.1'
```
- 需要在manifest中添加权限
```
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

### 3.初始化
- 新建一个类，继承Application
```
public class XUtilsApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);  //这个会影响性能
    }
}
```
- 在工程的manifest中注册XUtilsApplication类
```
<application
        android:name=".xUtilsApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <action android:name="android.intent.action.VIEW"/>
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
```
***
# 具体模块学习
### 一、注解模块
*注解(Annotation)为我们在代码中添加信息提供了一种形式化的方法，是我们可以在稍后某个时刻方便地使用这些数据*
- @ContentView 加载布局使用，将@ContentView放在activity或fragment的上方
```
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(MainActivity.this);
    }
}
```
- @ViewInject View注解的作用是代替我们写了findViewById这行代码，一般用于敏捷开发
```
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    
    @ViewInject(R.id.button)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(MainActivity.this);
    }
}
```
- @Event 处理控件的各种响应事件(注：方法必须是私有的、方法的参数必须和对应的接口一致)
```
@Event(value={R.id.button},type=View.OnClickListener.class)
    private void buttonClickEvent(View view){
        //TODO 单击事件，value里面存放的是需要单击的控件集合，type为控件的事件类型
        //参数和控件的单击事件参数一致
    }
```

### 二、网络模块
*xUtils3网络模块大大方便了在实际开发中网络模块的开发，xUtils网络模块大致包括GET请求、POST请求、如何使用其他的请求方式、上传方式、下载方式、使用缓存等功能*
- get请求
```
public static void get(String Url, Map<String,Object> map, final BackCallNet backCallNet){
        RequestParams requestParams = new RequestParams(Url);
        for (String key:map.keySet()){
            requestParams.addParameter(key,map.get(key));
        }
        //CommomCallback是通用的一个回调接口，参数String使我们需要返回的值
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                backCallNet.successful(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.getMessage();
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        
    }
    
    public interface BackCallNet{
        void successful(String stream);
        void failed(Throwable ex);
    }
```

- post请求
```
public static void post(String url, Map<String,Object> map, final BackCallNet backCallNet){
        RequestParams requestParams = new RequestParams(url);
        for (String key:map.keySet()){
            requestParams.addParameter(key,map.get(key));
        }
        
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                backCallNet.successful(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public interface BackCallNet{
        void successful(String stream);
        void failed(Throwable ex);
    }
```
- 带有缓存
```
//第一次采用网络的方式进行访问，在缓存的时间内去获取缓存的结果，这里设置的10s，
//10s结束之后会在此访问网络。
//在缓存期间，先访问onCache方法，再访问onSuccess方法。访问onSuccess方法的时候，参数的值是空值，因为没有访问网络
public static void cacheGet(String url, Map<String,Object> map, final BackCallNet  backCallNet){
        RequestParams requestParams = new RequestParams(url);
        requestParams.setCacheMaxAge(10*1000); //添加缓存的时间
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("stcLog","access network>>" + (TextUtils.isEmpty(result)));
                if (result != null) {
                    backCallNet.successful(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                Log.d("stcLog","get cache result");
                //TODO 在setCacheMaxAge设置范围，如果再次调用Get请求
                //TODO 返回true：缓存内容返回，相信本地缓存。
                //TODO 返回false，缓存内容被返回，不相信本地缓存，任然请求网络
                backCallNet.cache("缓存" + result);
                return true;
            }
        });
    }

    public interface BackCallNet{
        void successful(String stream);
        void failed(Throwable ex);
        void cache(String stream);
    }
```
- 带有缓存post
```
public static void cachePost(String url, Map<String,Object> map, final BackCallNet backCallNet){
        RequestParams requestParams = new RequestParams(url);
        requestParams.setCacheMaxAge(10*1000);
        x.http().post(requestParams, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //在缓存期间，会调用这个方法，但是result为空
                if (result != null) {
                    backCallNet.successful(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                backCallNet.cache(result);
                return true;
            }
        });
    }

    public interface BackCallNet{
        void successful(String stream);
        void failed(Throwable ex);
        void cache(String stream);
    }
```
- 上传文件
```
public static void uploadFile(String url, final BackCallNet backCallNet){
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path + "/Document/test");
        final RequestParams requestParams = new RequestParams(url);
        requestParams.setMultipart(true);
        requestParams.addBodyParameter("file",file);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    backCallNet.successful(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
```

- 文件下载(支持断点续传)
```
public static void downFile(final Context context, String url, BackCallNet backCallNet){
        String path = Environment.getExternalStorageDirectory().getPath();
        final File file = new File(path + "/Document/qq");
        RequestParams requestParams = new RequestParams(url);
        //下载的文件保存地址
        requestParams.setSaveFilePath(file.getPath());
        //自动为文件命名
        requestParams.setAutoRename(true);
        x.http().post(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.d("stcLog","下载成功:" + result);
                //这是在调用安装程序
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(result),"application/vnd.android.package-archive");
                context.startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("stcLog","下载失败:" + ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.d("stcLog","成功");

            }

            //网络请求之前回调
            @Override
            public void onWaiting() {
                Log.d("stcLog","准备开始下载");
            }

            //网络请求开始的时候回调
            @Override
            public void onStarted() {
                Log.d("stcLog","开始下载");
            }

            //下载的时候不间断的回调
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Log.d("stcLog","总大小：" + getTwo(total) + "M,已下载大小：" + getTwo(current) + "M" + ",进度：" + getProgress(total,current) + "%");
            }
        });
    }
```

### 三、图片加载模块
*xUtils图片模块，重点在于加载图片的4个bind方法，loadDrawable与loadFile和imageOptions用法*

**bind的几个方法**
1. void bind(ImageView view, String url)
2. void bind(ImageView view, String url, ImageOptions options)
3. void bind(ImageView view, String url, Callback.CommonCallback<Drawable> callback)
4. void bind(ImageView view, String url, ImageOption options, Callback.CommonCallback<Drawable> callback)
5. Callback.Cancelable loadDrawable(String url, ImageOptions options, Callback.CommonCallback<Drawable> callback)
6. Callback.Cancelable loadFile(String url, ImageOptions options, Callback.CommonCallback<File> callback)
- xUtils图片模块，重点在于加载图片的4个bind方法，loadDrawable与loadFile用法和imageOptions用法
- 通过imageOptions.Builder().set方法设置图片的属性，然后通过bind方法加载图片
**ImageOptions.Builder().set方法设置属性**
*使用ImageOptions options = new ImageOptions.Builder().setFadeIn(true).build;淡入效果。来进行实例*
- .setCircular(true) 设置图片显示为原型
- .setSquare(true)  设置图片显示为正方形
- .setCrop(true).setSize(200,200)   设置大小
- .setAnimation(animation)  设置动画
- .setFailureDrawable(Drawable failureDrawable) 设置加载失败的动画
- .setFailureDrawable(int failureDrawable)  以资源id加载失败的动画
- .setLoadingDrawable(Drawable loadingDrawable) 设置加载中的动画
- .setLoadingDrawable(int loadingDrawable)  以资源id加载加载中的动画
- .setIgnoreGif(false)  忽略gif图片
- .setParamsBuilder(ParamsBuilder paramsBuilder)    在网络请求中添加一些参数
- .setRaduis(int raduis)    设置拐角弧度
- .setUseMemCache(true) 设置使用内存缓存，默认是true
- 普通加载图片
```
 //没有imageOptions普通加载
    public static void loadCommonImage(ImageView imageView, String url){
        x.image().bind(imageView,url);
    }
```
- 圆形图片加载
```
//有ImageOptions的加载圆形图片
    public static void loadImageOptionsImage(ImageView imageView,String url){
        ImageOptions imageOptions = new ImageOptions.Builder().setCircular(true).build();
        x.image().bind(imageView,url,imageOptions);
    }
```
- 设置圆角
```
//有ImageOptions的加载圆角
    public static void loadImageOptionsImage(ImageView imageView,String url){
        ImageOptions imageOptions = new ImageOptions.Builder().setRadius(200).setFadeIn(true).build();
        x.image().bind(imageView,url,imageOptions);
    }
```
- 加载有CommonCallcback回调的方法
```
public static void loadCommonCalbackImage(ImageView imageView,String url){
        x.image().bind(imageView, url, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                Log.d("stcLog","[loadCommonCalbackImage onSuccess]:" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
```
- 加载loadDrawable
```
public static void loadDrawable(final ImageView imageView, String url){
        x.image().loadDrawable(url, null, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                imageView.setImageDrawable(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
```

- 加载loadFile(当我们通过bind()或者loadDrawable()方法加载了一张图片后，他会保存到本地文件中，那当我们需要这张图片的时候，就可以通过loadFile()方法进行查找)
```
public static void loadFile(final ImageView imageView, String url){
        x.image().loadFile(url, null, new Callback.CacheCallback<File>() {
            @Override
            public boolean onCache(File result) {
                //这里做图片的另存操作
                return false;
            }

            @Override
            public void onSuccess(File result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
```

### 四、数据库模块
- 配置数据库
```
public static void initDB(){
        DbManager.DaoConfig mDbManager = new DbManager.DaoConfig();
        //设置数据库的名字，默认是xutils.db
        mDbManager.setDbName("sun.db");
        //设置数据库允许事务
        mDbManager.setAllowTransaction(true);
        //设置数据库的版本
        mDbManager.setDbVersion(1);
        //设置表创建监听
        mDbManager.setTableCreateListener(new DbManager.TableCreateListener() {
            @Override
            public void onTableCreated(DbManager db, TableEntity<?> table) {
                Log.d("stcLog","create table listener:" + table.getName());     
            }
        });
        //设置数据库更新的监听
        mDbManager.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                
            }
        });
        
        //设置数据库打开的监听
        mDbManager.setDbOpenListener(new DbManager.DbOpenListener() {
            @Override
            public void onDbOpened(DbManager db) {
                Log.d("stcLog","database open listener");
                //开启数据库支持多线程操作，提高性能
                db.getDatabase().enableWriteAheadLogging();
            }
        });
        DbManager db = x.getDb(mDbManager);
    }
```
- 创建数据库表
```
/**
 * onCreated = "sql" sql：当第一次创建表需要插入数据时候在此写sql语句例：CREATE UNIQUE INDEX index_name ON person(id,name)
 */
//创建数据库的实体表
@Table(name="person",onCreated="")
public class Person {
    //name数据库的一个字段，isId数据库的主键，autoGen是否自动增长，property添加约束
    @Column(name="id",isId=true,autoGen=true,property="NOT NULL")
    private int id;
    @Column(name="name")
    private String name;

    //必须有一个空的构造函数，否则创建失败
    public Person() {
    }

    public Person(String name) {
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```
- 创建数据库
```
 DbManager.DaoConfig daoConfig = dbClass.initDB();
                db = x.getDb(daoConfig);
                List<Person> list = new ArrayList<>();
                Person person_zhang = new Person("张三");
                Person person_li = new Person("李四");
                Person person_wang = new Person("王五");
                list.add(person_zhang);
                list.add(person_li);
                list.add(person_wang);
                try {
                    //不仅可以插入单条数据，也可以插入集合
                    db.save(list);
                } catch (DbException e) {
                    e.printStackTrace();
                }
```
- 删除数据库
```
 try {
        db.dropDb();
    } catch (DbException e) {
        e.printStackTrace();
}
```
- 表的删除
``` 
try {
        db.dropTable(Person.class);
    } catch (DbException e) {
        e.printStackTrace();
}
```
- 数据的查询
```
//查询第一个数据
if (db != null) {
        try {
             Person first = db.findFirst(Person.class);
             displayContent.setText(first.toString());
        } catch (DbException e) {
             e.printStackTrace();
            }
}else {
    Toast.makeText(this,"数据库为空",Toast.LENGTH_SHORT).show();
}
//查询所有的数据
if (db != null) {
    try {
        List<Person> all = db.findAll(Person.class);
        displayContent.setText("");
        for (int i=0;i<all.size();i++){
            displayContent.append(all.get(i).toString());
            }
        } catch (DbException e) {
            e.printStackTrace();
            }
               
} else {
        Toast.makeText(this,"数据库为空",Toast.LENGTH_SHORT).show();
}
//条件查询
if (db != null) {
    WhereBuilder whereBuilder = WhereBuilder.b();
    whereBuilder.and("id",">",1);
    try {
        List<Person> all = db.selector(Person.class).where(whereBuilder).findAll();
        displayContent.setText("");
        for (int i=0;i<all.size();i++){
            displayContent.append(all.get(i).toString());
        }
        //第二种写法，如果还有其他的条件，在后面添加and
//      db.selector(Person.class).where("id",">",1).findAll();
    } catch (DbException e) {
        e.printStackTrace();
    }
}else {
    Toast.makeText(this,"数据库为空",Toast.LENGTH_SHORT).show();
}
```
- 修改数据
```
//第一种
try {
    Person first = db.findFirst(Person.class);
    first.setName("张三01");
    db.update(first,"name");
} catch (DbException e) {
    e.printStackTrace();
}

//第二种
try {
    Person first = db.findFirst(Person.class);
    first.setName("张三02");
    db.saveOrUpdate(first);
} catch (DbException e) {
    e.printStackTrace();
}


```
- 删除数据
```
//第一种(删除表里面的所有数据)
try {
    db.delete(Person.class);
} catch (DbException e) {
    e.printStackTrace();
}
//第二种
WhereBuilder whereBuilder = WhereBuilder.b();
whereBuilder.and("id","=","2");
try {
    db.delete(Person.class,whereBuilder);
} catch (DbException e) {
    e.printStackTrace();
}
```
