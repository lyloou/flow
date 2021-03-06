- [CalendarView/QUESTION_ZH.md at master · huanghaibin-dev/CalendarView](https://github.com/huanghaibin-dev/CalendarView/blob/master/QUESTION_ZH.md)

[Share Element Transition between Recycler View and Fragment](https://medium.com/@DeepakDroid/share-element-transition-between-recycler-view-and-fragment-8ce5084fd7a2)

- [x] 如何在 RecyclerView.ViewHolder 中直接获取属性，而不用 findViewById的方式？
    ```kotlin
    // 导入 itemView 类
    import kotlinx.android.synthetic.item_forecast.view.*
    // 在 ViewHolder 中直接调用 
    itemView.tvName.text = "hello, world!"
    ```

- [x] 如何能立刻获取room中的全部数据；
    ```kotlin
    @Query("SELECT * FROM $TABLE_FLOW")
    fun getAllDbFlow(): Array<DbFlow>
    ```
    
- [x] WorkManager 具体的线程是怎么做的；



[Retrofit+kotlin Coroutines（协程）+mvvm（Jetpack架构组件）实现更简洁的网络请求_移动开发_喻志强的博客-CSDN博客](https://yuzhiqiang.blog.csdn.net/article/details/101012090)



## 使用Matisse
```kotin
// [Which is the best image picker library for Android? - Quora](https://www.quora.com/Which-is-the-best-image-picker-library-for-Android)
// [Matisse](https://mindorks.com/android/store/Image-Pickers/zhihu/matisse)
implementation 'com.zhihu.android:matisse:0.5.3-beta3'
```

```kotlin
private fun takePhotoByMatisse() {
    // [zhihu/Matisse: A well-designed local image and video selector for Android](https://github.com/zhihu/Matisse)
    Matisse.from(this)
        .choose(MimeType.ofImage())
        .countable(true)
        .maxSelectable(1)
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .thumbnailScale(0.85f)
        .capture(true)
        .theme(R.style.Matisse_Dracula)
        .captureStrategy(CaptureStrategy(true, "${BuildConfig.APPLICATION_ID}.fileprovider"))
        .imageEngine(GlideEngine())
        .showPreview(false) // Default is `true`
        .forResult(REQUEST_CODE_CHOOSE)
}
```