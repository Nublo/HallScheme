HallScheme
====================

### Description

HallScheme is a lightweight simple library for creating rectangle halls.

![Alt text](/art/hall_scheme_1.png?raw=true)  

![Alt text](/art/hall_scheme_1_zoom.png?raw=true)

### Demo

[![HallScheme Demo on Google Play Store](http://developer.android.com/images/brand/en_generic_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=by.anatoldeveloper.hallscheme.example)

### Integration

**1)** Add as a dependency to your ``build.gradle``:

```groovy
dependencies {
    compile 'com.github.Nublo:hallscheme:1.0.0'
}
```

**2)** Add ``by.anatoldeveloper.ZoomableImage`` to your layout XML file. Content is automatically centered within free space.

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <by.anatoldeveloper.hallscheme.view.ZoomableImageView
        android:id="@+id/zoomable_image"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</RelativeLayout>
```

**3)** Customize `Seat` interface:

```
public class SeatExample implements Seat {

    public int id;
    public int color = Color.RED;
    public String marker;
    public String selectedSeatMarker;
    public HallScheme.SeatStatus status;

    @Override
    public int id() {
        return id;
    }

    @Override
    public int color() {
        return color;
    }

    @Override
    public String marker() {
        return marker;
    }

    @Override
    public String selectedSeat() {
        return selectedSeatMarker;
    }

    @Override
    public HallScheme.SeatStatus status() {
        return status;
    }

    @Override
    public void setStatus(HallScheme.SeatStatus status) {
        this.status = status;
    }

}
```

**4)** Attach ZoomableImage to `HallScheme` and add a 2-dimesional `Seat` array:

```
ZoomableImageView imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
Seat seats[][] = new Seat[10][10];
HallScheme scheme = new HallScheme(imageView, seats, getActivity());
```

**5)** Set `SeatListener` to `HallScheme` to handle click events on seats:

```
scheme.setSeatListener(new SeatListener() {

            @Override
            public void selectSeat(int id) {
                Toast.makeText(getActivity(), "select seat " + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unSelectSeat(int id) {
                Toast.makeText(getActivity(), "unSelect seat " + id, Toast.LENGTH_SHORT).show();
            }

        });
```

### Seat interface
To use the library you should implement custom `Seat`. It has the following methods:

**a)** `int id()` should return current `id` for current `Seat`. It will also be later returned when you attach `SeatListener` for scheme.
 
**b)** `int color()` should return the color of the checked `Seat`.

**c)** `String marker()` should return text representation for empty `Seat`. For example you can use it to see row and column.

**d)** `String selectedSeat();` should return `Seat` number when the `Seat` is checked.

**e)** `HallScheme.SeatStatus status();` should return current state of the `Seat`. The following statuses are supported:

+ `FREE` - seat is free and can be checked;
+ `CHOSEN` - seat is chosen and can be unchecked;
+ `BUSY` - seat cannot be used;
+ `INFO` - the seat is empty, but you can use to show some text there;
+ `EMPTY` - the seat is empty.

**f)** `void setStatus(HallScheme.SeatStatus status);` update current `Seat` status. 

### Features:

**a)** Zoomable image.

**b)** Fling while touching image.

**c)** Smooth animation available either on double tap or on gesture move.

**d)** Showing scheme scene from any of 4 sides.

`hallScheme.setScenePosition(ScenePosition.NORTH);`

+ `NORTH`
+ `SOUTH`
+ `EAST`
+ `WEST`

**e)** Adding text to show row or hall numbers.

**f)** Handling clicking on seats on your scheme.

**g)** Full customization of color for drawing.

+ Setting background color for scheme:

    ```java
    scheme.setBackgroundColor(Color.RED);
    ```
+ Setting color for `BUSY` seats:

    ```java
    scheme.setUnavailableSeatBackgroundColor(Color.RED);
    ```
+ Setting background color for `CHOSEN` seats:

    ```java
    scheme.setChosenSeatBackgroundColor(Color.RED);
    ```
+ Setting text color for `CHOSEN` seats:

    ```java
    scheme.setChosenSeatTextColor(Color.RED);
    ```
+ Setting color for `INFO` seats:

    ```java
    scheme.setMarkerColor(Color.RED);
    ```
+ Setting text color for Scene:

    ```java
    scheme.setScenePaintColor(Color.RED);
    ```
+ Setting background color for Scene:

    ```java
    scheme.setSceneBackgroundColor(Color.RED);
    ```

**h)** Setting custom name for scene:

```
scheme.setSceneName("Custom name");
```

### Restrictions

Image is currently drawing on standart `Bitmap`. Maximum size of `Bitmap` depends on underlying OpenGL implementation and it varies depending on device. It shouldn't be less then `2048x2048`. With current implementation your hall dimension(either rows or columns) shouldn't be more then `58`. Maximum scheme that can be drawn is `58x58`. Of course, if you know that devices using your app will have bigger maximum size - you can draw larger schemes.

### License

```
The MIT License (MIT)

Copyright (c) 2015 Varyvonchyk Anatol

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```