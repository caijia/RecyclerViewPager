# RecyclerViewPager
自动循环的RecyclerView

# Android Studio 引入
1. 在Project的build.gradle文件里面加入
```
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url  "https://dl.bintray.com/caijialib/caijiaLibray"
        }
    }
}
```

2. 在Module的build.gradle文件里面加入
```
compile 'com.caijia:looperRecyclerView:1.0'
```

# Useage
1. 布局文件
```
<FrameLayout
        android:layout_width="match_parent"
        android:layout_height="180dp">

        <com.caijia.widget.looperrecyclerview.LooperPageRecyclerView
            android:id="@+id/pager_recycler_view"
            android:layout_width="match_parent"
            android:layout_height="180dp"
            app:prIsAutoScroll="true"
            app:prIsLooper="true"
            app:prInterval="6000"
            app:prSpeedTimes="5"/>

        <com.caijia.widget.looperrecyclerview.RecyclerViewCircleIndicator
            android:id="@+id/indicator"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            android:layout_marginBottom="8dp"
            app:rviNormalColor="#ffffff"
            app:rviRadius="3dp"
            app:rviSelectedColor="#998888"
            app:rviSpace="8dp" />

    </FrameLayout>
```

2. 代码里使用
- 初始化LooperPageRecyclerView
```
looperPageRecyclerView = findViewById(R.id.pager_recycler_view);
```

- 初始化RecyclerViewCircleIndicator
```
indicator = findViewById(R.id.indicator);
```
- 初始化数据跟RecyclerView的使用一样
```
looperPageRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        colorStrings = getData();
        adapter = new TextPagerAdapter(colorStrings);
        looperPageRecyclerView.setAdapter(adapter);
```
- 关联指示器
```
indicator.setupWithRecyclerView(looperPageRecyclerView);
```

