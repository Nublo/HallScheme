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

    private int width, height;
    private Seat[][] seats;

    private final Rect textBounds = new Rect();
    private Paint textPaint, backgroundPaint, markerPaint, scenePaint;
    private int seatWidth, seatGap, offset;
    private int schemeBackgroundColor, unavailableSeatColor, chosenColor;
    private Typeface robotoMedium;
    private String sceneName;

    private Scene scene;
    private ZoomableImageView image;
    private SeatListener listener;

    public HallScheme(ZoomableImageView image, Seat[][] seats, Context context) {
        this.image = image;
        this.seats = seats;
        this.scene = new Scene(ScenePosition.NONE, 0, 0);
        nullifyMap();

        image.setClickListener(new ImageClickListener() {
            @Override
            public void onClick(Point p) {
                p.x -= scene.getLeftYOffset();
                p.y -= scene.getTopXOffset();
                clickScheme(p);
            }
        });

        robotoMedium = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        sceneName = context.getString(R.string.scene);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            schemeBackgroundColor = context.getResources().getColor(R.color.light_grey);
            unavailableSeatColor = context.getResources().getColor(R.color.disabled_color);
            chosenColor = context.getResources().getColor(R.color.orange);
            markerPaint = initTextPaint(context.getResources().getColor(R.color.black_gray));
            scenePaint = initTextPaint(context.getResources().getColor(R.color.black_gray));
        } else {
            schemeBackgroundColor = context.getColor(R.color.light_grey);
            unavailableSeatColor = context.getColor(R.color.disabled_color);
            chosenColor = context.getColor(R.color.orange);
            markerPaint = initTextPaint(context.getColor(R.color.black_gray));
            scenePaint = initTextPaint(context.getColor(R.color.black_gray));
        }
        textPaint = initTextPaint(Color.WHITE);
        scenePaint.setTextSize(35);

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(schemeBackgroundColor);
        backgroundPaint.setStrokeWidth(0);

        seatWidth = 30;
        seatGap = 5;
        offset = 30;
        height = seats.length;
        width = seats[0].length;
        image.setImageBitmap(getImageBitmap());
    }

    public void setScenePosition(ScenePosition position) {
        scene = new Scene(position, width, height);
        image.setImageBitmap(getImageBitmap());
    }

    public void setSceneName(String name) {
        sceneName = name;
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
        if (row >= width || (p.x % (seatWidth + seatGap) >= seatWidth)
                || p.x <= 0) {
            return false;
        }
        if (seat >= height || (p.y % (seatWidth + seatGap) >= seatWidth)
                || p.y <= 0) {
            return false;
        }
        return SeatStatus.canSeatBePressed(seats[seat][row].status());
    }

    private Bitmap getImageBitmap() {
        int bitmapHeight = height * (seatWidth + seatGap) - seatGap + offset + scene.getTopXOffset() + scene.getBottomXOffset();
        int bitmapWidth = width * (seatWidth + seatGap) - seatGap + offset + scene.getLeftYOffset() + scene.getRightYOffset();

        Bitmap tempBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(tempBitmap);
        backgroundPaint.setColor(schemeBackgroundColor);
        tempCanvas.drawRect(0, 0, bitmapWidth, bitmapHeight, backgroundPaint);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (seats[i][j].status() == SeatStatus.EMPTY)
                    continue;
                if (seats[i][j].status() == SeatStatus.INFO) {
                    drawTextCentred(tempCanvas, markerPaint, seats[i][j].marker(),
                            offset / 2 + (seatWidth + seatGap) * j + seatWidth / 2 + scene.getLeftYOffset(),
                            offset / 2 + (seatWidth + seatGap) * i + seatWidth / 2 + scene.getTopXOffset());
                    continue;
                }
                if (seats[i][j].status() == SeatStatus.BUSY) {
                    backgroundPaint.setColor(unavailableSeatColor);
                } else if (seats[i][j].status() == SeatStatus.FREE) {
                    backgroundPaint.setColor(seats[i][j].color());
                } else if (seats[i][j].status() == SeatStatus.CHOSEN) {
                    backgroundPaint.setColor(chosenColor);
                }
                tempCanvas.drawRect(offset/2 + (seatWidth + seatGap) * j + scene.getLeftYOffset(),
                        offset/2 + (seatWidth + seatGap) * i + scene.getTopXOffset(),
                        offset/2 + (seatWidth + seatGap) * j + seatWidth + scene.getLeftYOffset(),
                        offset/2 + (seatWidth + seatGap) * i + seatWidth + scene.getTopXOffset(),
                        backgroundPaint);
                if (seats[i][j].status() == SeatStatus.CHOSEN) {
                    drawTextCentred(tempCanvas, textPaint, seats[i][j].selectedSeat(),
                            offset / 2 + (seatWidth + seatGap) * j + seatWidth / 2 + scene.getLeftYOffset(),
                            offset / 2 + (seatWidth + seatGap) * i + seatWidth / 2 + scene.getTopXOffset());
                }
            }
        }

        drawScene(tempCanvas);

        return tempBitmap;
    }

    private void drawScene(Canvas canvas) {
        if (scene.position == ScenePosition.NONE) {
            return;
        }
        backgroundPaint.setColor(unavailableSeatColor);
        int topX=0, topY=0, bottomX=0, bottomY=0;
        if (scene.position == ScenePosition.NORTH) {
            int totalWidth = width * (seatWidth + seatGap) - seatGap + offset;
            topX = 0;
            topY = totalWidth / 2 - width * 6;
            bottomX = topX + scene.dimension;
            bottomY = topY + scene.dimensionSecond;
        }
        if (scene.position == ScenePosition.SOUTH) {
            int totalWidth = width * (seatWidth + seatGap) - seatGap + offset;
            topX = height * (seatWidth + seatGap) - seatGap + offset;
            topY = totalWidth / 2 - width * 6;
            bottomX = topX + scene.dimension;
            bottomY = topY + scene.dimensionSecond;
        }
        if (scene.position == ScenePosition.EAST) {
            int totalHeight = height * (seatWidth + seatGap) - seatGap + offset;
            topX = totalHeight / 2 - height * 6;
            topY = 0;
            bottomX = topX + scene.dimensionSecond;
            bottomY = topY + scene.dimension;
        }
        if (scene.position == ScenePosition.WEST) {
            int totalHeight = height * (seatWidth + seatGap) - seatGap + offset;
            topX = totalHeight / 2 - height * 6;
            topY = width * (seatWidth + seatGap) - seatGap + offset;
            bottomX = topX + scene.dimensionSecond;
            bottomY = topY + scene.dimension;
        }
        canvas.drawRect(topY, topX, bottomY, bottomX, backgroundPaint);
        canvas.save();
        if (scene.position == ScenePosition.EAST) {
            canvas.rotate(270, (topY+bottomY)/2, (topX+bottomX)/2);
        } else if (scene.position == ScenePosition.WEST) {
            canvas.rotate(90, (topY+bottomY)/2, (topX+bottomX)/2);
        }
        drawTextCentred(canvas, scenePaint, sceneName, (topY+bottomY)/2, (topX+bottomX)/2);
        canvas.restore();
    }

    private void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy){
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    private void nullifyMap() {
        width = 0;
        height = 0;
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
        private int dimension;
        public int dimensionSecond;
        public int width, height;

        public void setScenePosition(ScenePosition position) {
            this.position = position;
            dimension = 90;
            switch (position) {
                case NORTH:
                    dimensionSecond = width * 12;
                    break;
                case SOUTH:
                    dimensionSecond = width * 12;
                    break;
                case EAST:
                    dimensionSecond = height * 12;
                    break;
                case WEST:
                    dimensionSecond = height * 12;
                    break;
                case NONE:
                    dimensionSecond = 0;
                    dimension = 0;
                    break;
                default:
                    dimensionSecond = 0;
                    dimension = 0;
                    this.position = ScenePosition.NONE;
                    break;
            }
        }

        public Scene(ScenePosition position, int width, int height) {
            this.width = width;
            this.height = height;
            setScenePosition(position);
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
        return String.format("height = %d; width = %d", height, width);
    }

}