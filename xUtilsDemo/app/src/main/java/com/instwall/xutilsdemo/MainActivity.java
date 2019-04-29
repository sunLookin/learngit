package com.instwall.xutilsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.instwall.xutilsdemo.db.Person;
import com.instwall.xutilsdemo.db.dbClass;
import com.instwall.xutilsdemo.image.imageClass;
import com.instwall.xutilsdemo.net.netGet;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private Map<String,Object> mMap;
    private static final String URL = "https://login.bce.baidu.com/?account=&redirect=http%3A%2F%2Fbeian.bce.baidu.com%2F";
    private static final String URL_PICTURE = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
    private String[] image_url = {"http://www.pptbz.com/d/file/p/201708/4dead99f67999407561e6a041f4c32d8.jpg",
    "http://a1.att.hudong.com/81/56/01300536242912136717569360137.jpg","http://c.hiphotos.baidu.com/zhidao/pic/item/1b4c510fd9f9d72aca2c81d0d52a2834359bbbb7.jpg",
    "http://img0.imgtn.bdimg.com/it/u=3781622115,2109691529&fm=214&gp=0.jpg"};
    private DbManager db;
    @ViewInject(R.id.common_get)
    Button commonGet;
    @ViewInject(R.id.common_post)
    Button commonPost;
    @ViewInject(R.id.cache_get)
    Button cacheGet;
    @ViewInject(R.id.cache_post)
    Button cachePost;
    @ViewInject(R.id.upload_file)
    Button uploadFile;
    @ViewInject(R.id.download_file)
    Button download_file;
    @ViewInject(R.id.scroll)
    ScrollView mScrollView;
    @ViewInject(R.id.dispaly_image)
    ImageView mImageView;
    @ViewInject(R.id.common_load_image)
    Button common_load_image;
    @ViewInject(R.id.imageoptions_load_image)
    Button imageoptions_load_image;
    @ViewInject(R.id.load_commonCalback_image)
    Button load_commonCallback_image;
    @ViewInject(R.id.load_drawable)
    Button load_drawable;
    @ViewInject(R.id.load_file)
    Button load_file;
    @ViewInject(R.id.db_create)
    Button db_create;
    @ViewInject(R.id.db_delete)
    Button db_delete;
    @ViewInject(R.id.table_delete)
    Button table_delete;
    @ViewInject(R.id.find_one)
    Button find_one;
    @ViewInject(R.id.find_all)
    Button find_all;
    @ViewInject(R.id.if_find)
    Button if_find;
    @ViewInject(R.id.modfiy_data)
    Button modfiy_data;
    @ViewInject(R.id.delete_data)
    Button delete_data;
    @ViewInject(R.id.display_content)
    TextView displayContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(MainActivity.this);
        mMap = new HashMap<>();
        mMap.put("username","ss");
        mMap.put("password","123");
    }



    @Event(value = {R.id.common_get,R.id.common_post,
            R.id.cache_get,R.id.cache_post,R.id.upload_file,R.id.download_file,
            R.id.common_load_image,R.id.imageoptions_load_image,R.id.load_commonCalback_image,
            R.id.load_drawable,R.id.load_file,R.id.db_create,R.id.db_delete,
            R.id.table_delete,R.id.find_one,R.id.find_all,R.id.if_find,
            R.id.modfiy_data,R.id.delete_data},type = View.OnClickListener.class)
    private void buttonClickEvent(View view){
        //TODO 单击事件，value里面存放的是需要单击的控件集合，type为控件的事件类型
        //参数和控件的单击事件参数一致
        switch (view.getId()){
            case R.id.common_get:
                mScrollView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                netGet.get(URL, mMap, new netGet.BackCallNet() {
                    @Override
                    public void successful(String stream) {
                        displayContent.setText(stream);
                    }

                    @Override
                    public void failed(Throwable ex) {
                        displayContent.setText(ex.getMessage());
                    }

                    @Override
                    public void cache(String stream) {

                    }
                });
                break;
            case R.id.common_post:
                mScrollView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                netGet.post(URL, mMap, new netGet.BackCallNet() {
                    @Override
                    public void successful(String stream) {
                        displayContent.setText(stream);
                    }

                    @Override
                    public void failed(Throwable ex) {
                        displayContent.setText(ex.getMessage());
                    }

                    @Override
                    public void cache(String stream) {

                    }
                });
                break;

            case R.id.cache_get:
                mScrollView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                netGet.cacheGet(URL, null, new netGet.BackCallNet() {
                    @Override
                    public void successful(String stream) {
                        displayContent.setText(stream);
                    }

                    @Override
                    public void failed(Throwable ex) {
                        displayContent.setText(ex.getMessage());
                    }

                    @Override
                    public void cache(String stream) {
                        displayContent.setText(stream);
                    }
                });
                break;
            case R.id.cache_post:
                mScrollView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                netGet.cachePost(URL, null, new netGet.BackCallNet() {
                    @Override
                    public void successful(String stream) {
                        displayContent.setText(stream);
                    }

                    @Override
                    public void failed(Throwable ex) {
                        displayContent.setText(ex.getMessage());
                    }

                    @Override
                    public void cache(String stream) {
                        displayContent.setText(stream);
                    }
                });
                break;
            case R.id.upload_file:
                mScrollView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                netGet.uploadFile("https://www.baidu.com", new netGet.BackCallNet() {
                    @Override
                    public void successful(String stream) {
                        displayContent.setText(stream);
                    }

                    @Override
                    public void failed(Throwable ex) {
                        displayContent.setText(ex.getMessage());
                    }

                    @Override
                    public void cache(String stream) {

                    }
                });
                break;
            case R.id.download_file:
                mScrollView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                netGet.downFile(this,URL_PICTURE, new netGet.BackCallNet() {
                    @Override
                    public void successful(String stream) {

                    }

                    @Override
                    public void failed(Throwable ex) {

                    }

                    @Override
                    public void cache(String stream) {

                    }
                });
                break;
            case R.id.common_load_image:
                mScrollView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                imageClass.loadCommonImage(mImageView,image_url[0]);
                break;
            case R.id.imageoptions_load_image:
                mScrollView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                imageClass.loadImageOptionsImage(mImageView,image_url[1],this);
                break;
            case R.id.load_commonCalback_image:
                mScrollView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                imageClass.loadCommonCalbackImage(mImageView,image_url[2]);
                break;
            case R.id.load_drawable:
                mScrollView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                imageClass.loadDrawable(mImageView,image_url[3]);
                break;
            case R.id.load_file:
                mScrollView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                imageClass.loadFile(mImageView,image_url[0]);
                break;
            case R.id.db_create:
                //数据库的创建
                if (db == null) {
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
                }else {
                    Toast.makeText(this,"数据库为空",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.db_delete:
                if (db != null) {
                    try {
                        db.dropDb();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(this,"数据库为空",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.table_delete:
                if (db != null) {
                    try {
                        db.dropTable(Person.class);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(this,"数据库为空",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.find_one:
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

                break;
            case R.id.find_all:
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
                }else {
                    Toast.makeText(this,"数据库为空",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.if_find:
                if (db != null) {
                    WhereBuilder whereBuilder = WhereBuilder.b();
                    whereBuilder.and("id",">",1);
                    try {
                        List<Person> all = db.selector(Person.class).where(whereBuilder).findAll();
                        displayContent.setText("");
                        for (int i=0;i<all.size();i++){
                            displayContent.append(all.get(i).toString());
                        }

//                        db.selector(Person.class).where("id",">",1).findAll();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(this,"数据库为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.modfiy_data:
                //第一种
//                try {
//                    Person first = db.findFirst(Person.class);
//                    first.setName("张三01");
//                    db.update(first,"name");
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
                //第二种
                try {
                    Person first = db.findFirst(Person.class);
                    first.setName("张三02");
                    db.saveOrUpdate(first);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.delete_data:
                //第一种(删除表里面的所有数据)
//                try {
//                    db.delete(Person.class);
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
                //第二种
                WhereBuilder whereBuilder = WhereBuilder.b();
                whereBuilder.and("id","=","2");
                try {
                    db.delete(Person.class,whereBuilder);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
