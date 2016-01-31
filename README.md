HallScheme
====================

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-HallScheme-green.svg?style=true)](https://android-arsenal.com/details/1/2935)

### Description

HallScheme is a lightweight simple library for creating rectangle halls.

![Alt text](/art/hall_scheme_1.png?raw=true)  

![Alt text](/art/hall_scheme_1_zoom.png?raw=true)

### Demo

[![HallScheme Demo on Google Play Store](/art/google-play-badge.png?raw=true)](https://play.google.com/store/apps/details?id=by.anatoldeveloper.hallscheme.example)

### Integration

**1)** Add as a dependency to your ``build.gradle``:

```groovy
dependencies {
    compile 'com.github.Nublo:hallscheme:1.1.0'
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

Zooming can be enabled/disabled either from code:

`imageView.setZoomByDoubleTap(false);`

or from xml:

	<?xml version="1.0" encoding="utf-8"?>
	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    	xmlns:hallscheme="http://schemas.android.com/apk/res"
    	android:orientation="vertical"
    	android:layout_width="match_parent"
    	android:layout_height="match_parent"
    	android:background="#FFD8E2E6">

    	<by.anatoldeveloper.hallscheme.view.ZoomableImageView
        	android:id="@+id/zoomable_image"
        	android:layout_width="match_parent"
        	android:layout_height="match_parent"
        	hallscheme:doubleTap="false"/>
	</RelativeLayout>

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

**g)** Full customization for drawing:

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
+ Setting custom typeface for Scene:

    ```java
    scheme.setTypeface(typeface);
    ```

**h)** Setting custom name for scene:
	
	scheme.setSceneName("Custom name");	

**i)** Setting limit of checked seats and set listener for this event:

    scheme.setMaxSelectedSeats(4);
	scheme.setMaxSeatsClickListener(new MaxSeatsClickListener() {
        @Override
        public void maxSeatsReached(int id) {
            // Do something
        }
    });
    
By default it is unlimitted. So user can check as many seats as he wants.

**j)** Programmatically click on scheme:

	scheme.clickSchemeProgrammatically(3, 4);
	
So if seat was checked it becomes unchecked and `Seatlistener` will be notified.
If seat was unchecked and limit is not reached seat becomes checked and `SeatListener` will be notified, otherway(when limit reached) seat will not be checked and `MaxSeatsListener` will be notified.

**k)** Adding seat areas.

Seat area will be drawn like a solid rectangle on scene and can have size from 1x1 block to all scheme. When user is clicking on seat area listener will be notified. Clicking on seat area is not counting in `maxSelectedSeats`. 

You can see how to use seat areas in examples. Main principle like with seats. You should set left top corner, width, height and color. Name for seat area is not used in current library implementation.


### Restrictions

Image is currently drawing on standart `Bitmap`. Maximum size of `Bitmap` depends on underlying OpenGL implementation and it varies depending on device. It shouldn't be less then `2048x2048`. With current implementation your hall dimension(either rows or columns) shouldn't be more then `58`. Maximum scheme that can be drawn is `58x58`. Of course, if you know that devices using your app will have bigger maximum size - you can draw larger schemes.

### ChangeLog

**Version 1.1.0:**

+ Ability to set custom typeface
+ Disable zooming by double tap
+ Programmatically click seat on scheme
+ Limiting checked seats
+ Added seat areas

### License

```
The MIT License (MIT)

Copyright (c) 2016 Varyvonchyk Anatol

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