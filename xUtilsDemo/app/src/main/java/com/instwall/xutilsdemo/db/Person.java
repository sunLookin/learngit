package com.instwall.xutilsdemo.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

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
