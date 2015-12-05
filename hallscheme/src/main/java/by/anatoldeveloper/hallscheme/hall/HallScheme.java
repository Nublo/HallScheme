package by.anatoldeveloper.hallscheme.hall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;

import by.anatoldeveloper.hallscheme.view.ZoomableImageView;
import by.anatoldeveloper.hallscheme.R;

/**
 * Created by Nublo on 28.10.2015.
 * Copyright Nublo
 */
public class HallScheme {

    private int mWidth, mHeight;
    private Seat[][] seats;

    private final Rect textBounds = new Rect();
    private Paint mTextPaint, mBackgroundPaint, mMarkerPaint, mScenePaint;
    private int seatWidth, seatGap, offset;
    private int mSchemeBackgroundColor, mUnavailableSeatColor, mChosenColor;
    private Typeface robotoMedium;
    private String sceneName;

    public int mSelectedSeats, mSelectedPrice, mSelectedCharge;
    private Scene mScene;
    private ZoomableImageView image;
    private SeatListener listener;

    public HallScheme(ZoomableImageView image, Seat[][] seats, Context context) {
        this.image = image;
        this.seats = seats;
        this.mScene = new Scene("", 0, 0);
        nullifyMap();

        image.setClickListener(new ImageClickListener() {
            @Override
            public void onClick(Point p) {
                p.x -= mScene.getLeftYOffset();
                p.y -= mScene.getTopXOffset();
                clickScheme(p);
            }
        });

        robotoMedium = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        sceneName = context.getString(R.string.scene);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mSchemeBackgroundColor = context.getResources().getColor(R.color.light_grey);
            mUnavailableSeatColor = context.getResources().getColor(R.color.disabled_color);
            mChosenColor = context.getResources().getColor(R.color.orange);
            mMarkerPaint = initTextPaint(context.getResources().getColor(R.color.black_gray));
            mScenePaint = initTextPaint(context.getResources().getColor(R.color.black_gray));
        } else {
            mSchemeBackgroundColor = context.getColor(R.color.light_grey);
            mUnavailableSeatColor = context.getColor(R.color.disabled_color);
            mChosenColor = context.getColor(R.color.orange);
            mMarkerPaint = initTextPaint(context.getColor(R.color.black_gray));
            mScenePaint = initTextPaint(context.getColor(R.color.black_gray));
        }
        mTextPaint = initTextPaint(Color.WHITE);
        mScenePaint.setTextSize(35);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mSchemeBackgroundColor);
        mBackgroundPaint.setStrokeWidth(0);

        seatWidth = 30;
        seatGap = 5;
        offset = 30;
        mHeight = seats.length;
        mWidth = seats[0].length;
        image.setImageBitmap(getImageBitmap());
    }

    public void setScenePosition(ScenePosition position) {
        mScene.setScenePosition(position);
        image.setImageBitmap(getImageBitmap());
    }

    public void setSeatListener(SeatListener listener) {
        this.listener = listener;
    }

    private Paint initTextPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeMiter(0);
        paint.setTextSize(25);
        paint.setTypeface(robotoMedium);
        return paint;
    }

    private void clickScheme(Point point) {
        Point p = new Point(point.x - offset/2, point.y - offset/2);
        int row = p.x / (seatWidth + seatGap);
        int seat = p.y / (seatWidth + seatGap);
        if (canSeatPress(p, row, seat)) {
            Seat pressedSeat = seats[seat][row];
            notifySeatListener(pressedSeat);
            pressedSeat.setStatus(pressedSeat.status().pressSeat());
            image.setImageBitmap(getImageBitmap());
        }
    }

    public boolean canSeatPress(Point p, int row, int seat) {
        if (row >= mWidth || (p.x % (seatWidth + seatGap) >= seatWidth)
                || p.x <= 0) {
            return false;
        }
        if (seat >= mHeight || (p.y % (seatWidth + seatGap) >= seatWidth)
                || p.y <= 0) {
            return false;
        }
        return SeatStatus.canSeatBePressed(seats[seat][row].status());
    }

    private Bitmap getImageBitmap() {
        int bitmapHeight = mHeight * (seatWidth + seatGap) - seatGap + offset + mScene.getTopXOffset() + mScene.getBottomXOffset();
        int bitmapWidth = mWidth * (seatWidth + seatGap) - seatGap + offset + mScene.getLeftYOffset() + mScene.getRightYOffset();

        Bitmap tempBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(tempBitmap);
        mBackgroundPaint.setColor(mSchemeBackgroundColor);
        tempCanvas.drawRect(0, 0, bitmapWidth, bitmapHeight, mBackgroundPaint);

        for (int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++) {
                if (seats[i][j].status() == SeatStatus.EMPTY)
                    continue;
                if (seats[i][j].status() == SeatStatus.INFO) {
                    drawTextCentred(tempCanvas, mMarkerPaint, seats[i][j].marker(),
                            offset/2 + (seatWidth + seatGap) * j + seatWidth/2 + mScene.getLeftYOffset(),
                            offset/2 + (seatWidth + seatGap) * i + seatWidth/2 + mScene.getTopXOffset());
                    continue;
                }
                if (seats[i][j].status() == SeatStatus.BUSY) {
                    mBackgroundPaint.setColor(mUnavailableSeatColor);
                } else if (seats[i][j].status() == SeatStatus.FREE) {
                    mBackgroundPaint.setColor(seats[i][j].color());
                } else if (seats[i][j].status() == SeatStatus.CHOSEN) {
                    mBackgroundPaint.setColor(mChosenColor);
                }
                tempCanvas.drawRect(offset/2 + (seatWidth + seatGap) * j + mScene.getLeftYOffset(),
                        offset/2 + (seatWidth + seatGap) * i + mScene.getTopXOffset(),
                        offset/2 + (seatWidth + seatGap) * j + seatWidth + mScene.getLeftYOffset(),
                        offset/2 + (seatWidth + seatGap) * i + seatWidth + mScene.getTopXOffset(),
                        mBackgroundPaint);
                if (seats[i][j].status() == SeatStatus.CHOSEN) {
                    drawTextCentred(tempCanvas, mTextPaint, seats[i][j].selectedSeat(),
                            offset/2 + (seatWidth + seatGap) * j + seatWidth/2 + mScene.getLeftYOffset(),
                            offset/2 + (seatWidth + seatGap) * i + seatWidth/2 + mScene.getTopXOffset());
                }
            }
        }

        drawScene(tempCanvas);

        return tempBitmap;
    }

    private void drawScene(Canvas canvas) {
        if (mScene.position == ScenePosition.NONE) {
            return;
        }
        mBackgroundPaint.setColor(mUnavailableSeatColor);
        int topX=0, topY=0, bottomX=0, bottomY=0;
        if (mScene.position == ScenePosition.NORTH) {
            int totalWidth = mWidth * (seatWidth + seatGap) - seatGap + offset;
            topX = 0;
            topY = totalWidth / 2 - mWidth * 6;
            bottomX = topX + mScene.dimension;
            bottomY = topY + mScene.dimensionSecond;
        }
        if (mScene.position == ScenePosition.SOUTH) {
            int totalWidth = mWidth * (seatWidth + seatGap) - seatGap + offset;
            topX = mHeight * (seatWidth + seatGap) - seatGap + offset;
            topY = totalWidth / 2 - mWidth * 6;
            bottomX = topX + mScene.dimension;
            bottomY = topY + mScene.dimensionSecond;
        }
        if (mScene.position == ScenePosition.EAST) {
            int totalHeight = mHeight * (seatWidth + seatGap) - seatGap + offset;
            topX = totalHeight / 2 - mHeight * 6;
            topY = 0;
            bottomX = topX + mScene.dimensionSecond;
            bottomY = topY + mScene.dimension;
        }
        if (mScene.position == ScenePosition.WEST) {
            int totalHeight = mHeight * (seatWidth + seatGap) - seatGap + offset;
            topX = totalHeight / 2 - mHeight * 6;
            topY = mWidth * (seatWidth + seatGap) - seatGap + offset;
            bottomX = topX + mScene.dimensionSecond;
            bottomY = topY + mScene.dimension;
        }
        canvas.drawRect(topY, topX, bottomY, bottomX, mBackgroundPaint);
        canvas.save();
        if (mScene.position == ScenePosition.EAST) {
            canvas.rotate(270, (topY+bottomY)/2, (topX+bottomX)/2);
        } else if (mScene.position == ScenePosition.WEST) {
            canvas.rotate(90, (topY+bottomY)/2, (topX+bottomX)/2);
        }
        drawTextCentred(canvas, mScenePaint, sceneName, (topY+bottomY)/2, (topX+bottomX)/2);
        canvas.restore();
    }

    private void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy){
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    private void nullifyMap() {
        mWidth = 0;
        mHeight = 0;
        mSelectedSeats = 0;
        mSelectedPrice = 0;
        mSelectedCharge = 0;
        image.setShouldOnMeasureBeCalled(true);
    }

    public void notifySeatListener(Seat s) {
        if (s.status() == SeatStatus.FREE) {
            if (listener != null)
                listener.selectSeat(s.id());
        } else {
            if (listener != null)
                listener.unSelectSeat(s.id());
        }
    }

    public static class Scene {

        private ScenePosition position;
        private int dimension = 90;
        public int dimensionSecond;

        public void setScenePosition(ScenePosition position) {
            position = this.position;
        }

        public Scene(String scene, int width, int height) {
            switch (scene) {
                case "north":
                    position = ScenePosition.NORTH;
                    dimensionSecond = width * 12;
                    break;
                case "south":
                    position = ScenePosition.SOUTH;
                    dimensionSecond = width * 12;
                    break;
                case "east":
                    position = ScenePosition.EAST;
                    dimensionSecond = height * 12;
                    break;
                case "west":
                    position = ScenePosition.WEST;
                    dimensionSecond = height * 12;
                    break;
                default:
                    position = ScenePosition.NONE;
                    dimension = 0;
                    dimensionSecond = 0;
            }
        }

        public int getTopXOffset() {
            if (position == ScenePosition.NORTH) {
                return dimension;
            }
            return 0;
        }

        public int getLeftYOffset() {
            if (position == ScenePosition.EAST) {
                return dimension;
            }
            return 0;
        }

        public int getBottomXOffset() {
            if (position == ScenePosition.SOUTH) {
                return dimension;
            }
            return 0;
        }

        public int getRightYOffset() {
            if (position == ScenePosition.WEST) {
                return dimension;
            }
            return 0;
        }

        @Override
        public String toString() {
            return String.format("Left - %d, Right- %d, Top - %d, Bottom - %d", getLeftYOffset(), getRightYOffset(), getTopXOffset(), getBottomXOffset());
        }

    }

    public enum SeatStatus {
        FREE, BUSY, EMPTY, CHOSEN, INFO;

        public static boolean canSeatBePressed(SeatStatus status) {
            return (status == FREE || status == CHOSEN);
        }

        public SeatStatus pressSeat() {
            if (this == FREE)
                return CHOSEN;
            if (this == CHOSEN)
                return FREE;
            return this;
        }

    }

    @Override
    public String toString() {
        return String.format("height = %d; width = %d", mHeight, mWidth);
    }

}