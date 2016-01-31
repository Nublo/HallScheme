package by.anatoldeveloper.hallscheme.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import by.anatoldeveloper.hallscheme.R;
import by.anatoldeveloper.hallscheme.hall.ImageClickListener;

/**
 * Created by Nublo on 28.10.2015.
 * Copyright Nublo
 */
public class ZoomableImageView extends ImageView {

    private final static int ANIMATION_ZOOM_DURATION = 400;
    private final static float ANIMATION_ZOOM_PARAMETER = 1.3f;
    private static final int SWIPE_MIN_DISTANCE = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 800;

    private Matrix matrix = new Matrix();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private static final int CLICK = 3;
    private int mode = NONE;

    private boolean isClick;

    private PointF last = new PointF();
    private PointF start = new PointF();
    private float minScale = 1f;
    private float maxScale = 3.0f;
    private float[] m;

    private float redundantXSpace, redundantYSpace;
    private float width, height;
    private float saveScale = 1f;
    private float right, bottom, origWidth, origHeight, bmWidth, bmHeight;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private boolean shouldOnMeasureBeCalled = true;
    private int onMeasure = 0;

    private ImageClickListener listener;
    private boolean zoomByDoubleTap;

    public ZoomableImageView(Context context, AttributeSet attr)
    {
        super(context, attr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attr, R.styleable.ZoomableImageView, 0, 0);
        try {
            zoomByDoubleTap = a.getBoolean(R.styleable.ZoomableImageView_doubleTap, true);
        } finally {
            a.recycle();
        }
        super.setClickable(true);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureListener());
        matrix.setTranslate(1f, 1f);
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                mScaleDetector.onTouchEvent(event);
                mGestureDetector.onTouchEvent(event);

                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                PointF curr = new PointF(event.getX(), event.getY());

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        last.set(event.getX(), event.getY());
                        start.set(last);
                        mode = DRAG;
                        isClick = true;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        last.set(event.getX(), event.getY());
                        start.set(last);
                        mode = ZOOM;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isClick && (Math.abs(curr.x - last.x) > CLICK || Math.abs(curr.y-last.y) > CLICK)) {
                            isClick = false;
                        }
                        if (mode == ZOOM || (mode == DRAG && saveScale > minScale))
                        {
                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float scaleWidth = Math.round(origWidth * saveScale);
                            float scaleHeight = Math.round(origHeight * saveScale);
                            if (scaleWidth < width)
                            {
                                deltaX = 0;
                                if (y + deltaY > 0)
                                    deltaY = -y;
                                else if (y + deltaY < -bottom)
                                    deltaY = -(y + bottom);
                            }
                            else if (scaleHeight < height)
                            {
                                deltaY = 0;
                                if (x + deltaX > 0)
                                    deltaX = -x;
                                else if (x + deltaX < -right)
                                    deltaX = -(x + right);
                            }
                            else
                            {
                                if (x + deltaX > 0)
                                    deltaX = -x;
                                else if (x + deltaX < -right)
                                    deltaX = -(x + right);

                                if (y + deltaY > 0)
                                    deltaY = -y;
                                else if (y + deltaY < -bottom)
                                    deltaY = -(y + bottom);
                            }
                            matrix.postTranslate(deltaX, deltaY);
                            last.set(curr.x, curr.y);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        if (xDiff < CLICK && yDiff < CLICK)
                            performClick();
                        if (isClick) {
                            v.performClick();
                            if (listener != null) {
                                listener.onClick(new Point((int)((event.getX()-x)/m[0]), (int)((event.getY()-y)/m[0])));
                            }
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                }
                setImageMatrix(matrix);
                invalidate();
                return true;
            }

        });
    }

    public void setClickListener(ImageClickListener listener) {
        this.listener = listener;
    }

    public void setZoomByDoubleTap(boolean zoomByDoubleTap) {
        this.zoomByDoubleTap = zoomByDoubleTap;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        super.setImageBitmap(bm);
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
    }

    public void setShouldOnMeasureBeCalled(boolean b) {
        if (b) {
            onMeasure = 0;
        }
        this.shouldOnMeasureBeCalled = b;
    }

    public void zoom(boolean zoomIn) {
        float endScale;
        if (zoomIn) endScale = saveScale * ANIMATION_ZOOM_PARAMETER;
        else endScale = saveScale / ANIMATION_ZOOM_PARAMETER;
        if (endScale > maxScale) endScale = maxScale;
        if (endScale < minScale) endScale = minScale;
        ValueAnimator zoomAnimation = ValueAnimator.ofFloat(saveScale, endScale);
        zoomAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                zoomScale(value);
            }
        });
        zoomAnimation.setDuration(ANIMATION_ZOOM_DURATION);
        zoomAnimation.start();
    }

    public void zoomScale(float endScale) {
        scale(endScale/saveScale, width/2, height/2);
        setImageMatrix(matrix);
        invalidate();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
            return true;
        }
    }

    private void scale(float scaleFactor, float xCenter, float yCenter) {
        float origScale = saveScale;
        saveScale *= scaleFactor;
        if (saveScale > maxScale) {
            saveScale = maxScale;
            scaleFactor = maxScale / origScale;
        }
        else if (saveScale < minScale) {
            saveScale = minScale;
            scaleFactor = minScale / origScale;
        }
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        if (origWidth * saveScale <= width || origHeight * saveScale <= height)
        {
            matrix.postScale(scaleFactor, scaleFactor, width / 2, height / 2);
            if (scaleFactor < 1)
            {
                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                if (scaleFactor < 1)
                {
                    if (Math.round(origWidth * saveScale) < width)
                    {
                        if (y < -bottom)
                            matrix.postTranslate(0, -(y + bottom));
                        else if (y > 0)
                            matrix.postTranslate(0, -y);
                    }
                    else
                    {
                        if (x < -right)
                            matrix.postTranslate(-(x + right), 0);
                        else if (x > 0)
                            matrix.postTranslate(-x, 0);
                    }
                }
            }
        }
        else
        {
            matrix.postScale(scaleFactor, scaleFactor, xCenter, yCenter);
            matrix.getValues(m);
            float x = m[Matrix.MTRANS_X];
            float y = m[Matrix.MTRANS_Y];
            if (scaleFactor < 1) {
                if (x < -right)
                    matrix.postTranslate(-(x + right), 0);
                else if (x > 0)
                    matrix.postTranslate(-x, 0);
                if (y < -bottom)
                    matrix.postTranslate(0, -(y + bottom));
                else if (y > 0)
                    matrix.postTranslate(0, -y);
            }
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap( MotionEvent e ) {
            if (zoomByDoubleTap)
                zoom(true);
            return false;
        }

        @Override
        public void onLongPress( MotionEvent e ) {}

        @Override
        public boolean onScroll( MotionEvent e1, MotionEvent e2, float distanceX, float distanceY ) { return false;}

        @Override
        public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY ) {
            if (e1 == null || e2 == null) return false;
            if ( e1.getPointerCount() > 1 || e2.getPointerCount() > 1 ) return false;
            if ( mScaleDetector.isInProgress() || mode == ZOOM) return false;
            if ( (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY || Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) &&
                    (Math.abs(e2.getX() - e1.getX()) > SWIPE_MIN_DISTANCE || Math.abs(e2.getY() - e1.getY()) > SWIPE_MIN_DISTANCE)) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();
                //animateFling(velocityX/20, velocityY/20);
                animateFling(diffX, diffY);
                return true;
            }
            return false;
        }
    }

    private void animateFling(final float diffX, final float diffY) {
        ValueAnimator flingAnimation = ValueAnimator.ofFloat(diffX/5, 0);
        flingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float newDiffX = (float) valueAnimator.getAnimatedValue();
                float newDiffY = (newDiffX * diffY)/diffX;
                flingMatrix(newDiffX, newDiffY);
            }
        });
        flingAnimation.setDuration(300);
        flingAnimation.setInterpolator(new DecelerateInterpolator());
        flingAnimation.start();
    }

    private void flingMatrix(float xValue, float yValue) {
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        matrix.getValues(m);
        float x = m[Matrix.MTRANS_X];
        float y = m[Matrix.MTRANS_Y];
        float xFreeSpace = width - (origWidth * saveScale);
        xFreeSpace = (xFreeSpace < 0) ? 0.0f : xFreeSpace / 2;
        if ((x + xValue - xFreeSpace) <= -right) {
            xValue = -(x + right + xFreeSpace);
        }
        else if ((x + xValue - xFreeSpace) >= 0) {
            xValue = -x;
        }
        float yFreeSpace = height - (origHeight * saveScale);
        yFreeSpace = (yFreeSpace < 0) ? 0.0f : yFreeSpace / 2;
        if ((y + yValue - yFreeSpace) <= -bottom) {
            yValue = -(y + bottom + yFreeSpace);
        } else if ((y + yValue - yFreeSpace) >= 0) {
            yValue = -y;
        }
        matrix.postTranslate(xValue, yValue);
        setImageMatrix(matrix);
        invalidate();
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!shouldOnMeasureBeCalled && onMeasure > 2) {
            return;
        } else if (!shouldOnMeasureBeCalled) {
            onMeasure++;
        }
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        //Fit to screen.
        float scale = Math.min(width / bmWidth, height / bmHeight);
        matrix.setScale(scale, scale);
        setImageMatrix(matrix);
        saveScale = 1f;

        // Center the image
        redundantYSpace = height - (scale * bmHeight);
        redundantXSpace = width - (scale * bmWidth);
        redundantYSpace /= 2;
        redundantXSpace /= 2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = width - 2 * redundantXSpace;
        origHeight = height - 2 * redundantYSpace;
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        setImageMatrix(matrix);
    }

}