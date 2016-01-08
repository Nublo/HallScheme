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
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
    private int schemeBackgroundColor, unavailableSeatColor, chosenColor, sceneBackgroundColor;
    private int selectedSeats, maxSelectedSeats;
    private Typeface typeface;
    private String sceneName;

    private Scene scene;
    private ZoomableImageView image;
    private SeatListener listener;
    private MaxSeatsClickListener maxSeatsClickListener;
    private List<Zone> zones;
    private ZoneListener zoneListener;

    public HallScheme(ZoomableImageView image, Seat[][] seats, Context context) {
        this.selectedSeats = 0;
        this.maxSelectedSeats = -1;
        this.image = image;
        this.seats = seats;
        nullifyMap();

        image.setClickListener(new ImageClickListener() {
            @Override
            public void onClick(Point p) {
                p.x -= scene.getLeftYOffset();
                p.y -= scene.getTopXOffset();
                clickScheme(p);
            }
        });

        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
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
        sceneBackgroundColor = unavailableSeatColor;
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
        this.scene = new Scene(ScenePosition.NONE, 0, 0, offset/2);
        image.setImageBitmap(getImageBitmap());
    }

    public void setScenePosition(ScenePosition position) {
        scene = new Scene(position, width, height, offset/2);
        image.setImageBitmap(getImageBitmap());
    }

    public void setSceneName(String name) {
        sceneName = name;
        image.setImageBitmap(getImageBitmap());
    }

    public void setBackgroundColor(int color) {
        schemeBackgroundColor = color;
        image.setImageBitmap(getImageBitmap());
    }

    public void setUnavailableSeatBackgroundColor(int color) {
        unavailableSeatColor = color;
        image.setImageBitmap(getImageBitmap());
    }

    public void setChosenSeatTextColor(int color) {
        textPaint.setColor(color);
        image.setImageBitmap(getImageBitmap());
    }

    public void setChosenSeatBackgroundColor(int color) {
        chosenColor = color;
        image.setImageBitmap(getImageBitmap());
    }

    public void setMarkerColor(int color) {
        markerPaint.setColor(color);
        image.setImageBitmap(getImageBitmap());
    }

    public void setSceneTextColor(int color) {
        scenePaint.setColor(color);
        image.setImageBitmap(getImageBitmap());
    }

    public void setSceneBackgroundColor(int color) {
        sceneBackgroundColor = color;
        image.setImageBitmap(getImageBitmap());
    }

    public void setMaxSelectedSeats(int maxSelectedSeats) {
        this.maxSelectedSeats = maxSelectedSeats;
    }

    public void setSeatListener(SeatListener listener) {
        this.listener = listener;
    }

    public void setMaxSeatsClickListener(MaxSeatsClickListener maxSeatsClickListener) {
        this.maxSeatsClickListener = maxSeatsClickListener;
    }

    /**
     * Set custom typeface to scheme.
     * Be careful when using. Bold typefaces can be drawn incorrectly.
     * @param typeface - Typeface to be setted
     */
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        markerPaint.setTypeface(typeface);
        scenePaint.setTypeface(typeface);
        textPaint.setTypeface(typeface);
        image.setImageBitmap(getImageBitmap());
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
        image.setImageBitmap(getImageBitmap());
    }

    public void setZoneListener(ZoneListener zoneListener) {
        this.zoneListener = zoneListener;
    }

    private Paint initTextPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeMiter(0);
        paint.setTextSize(25);
        paint.setTypeface(typeface);
        return paint;
    }

    private void clickScheme(Point point) {
        if (findZoneClick(point))
            return;
        Point p = new Point(point.x - offset/2, point.y - offset/2);
        int row = p.x / (seatWidth + seatGap);
        int seat = p.y / (seatWidth + seatGap);
        if (canSeatPress(p, row, seat)) {
            clickScheme(row, seat);
        }
    }

    public boolean findZoneClick(Point p) {
        for (Zone zone : zones) {
            int topX = offset/2 + zone.leftTopX() * (seatWidth + seatGap);
            int topY = offset/2 + zone.leftTopY() * (seatWidth + seatGap);
            int bottomX = offset/2 + (zone.leftTopX() + zone.width()) * (seatWidth + seatGap) - seatGap;
            int bottomY = offset/2 + (zone.leftTopY() + zone.height()) * (seatWidth + seatGap) - seatGap;
            if (p.x >= topX && p.x <= bottomX && p.y >= topY && p.y <= bottomY) {
                if (zoneListener != null)
                    zoneListener.zoneClick(zone.id());
                return true;
            }
        }
        return false;
    }

    public void clickSchemeProgrammatically(int row, int seat) {
        if (SeatStatus.canSeatBePressed(seats[row][seat].status())) {
            clickScheme(seat, row);
        }
    }

    private void clickScheme(int row, int seat) {
        Seat pressedSeat = seats[seat][row];
        if (updateSelectedSeatCount(pressedSeat)) {
            notifySeatListener(pressedSeat);
            pressedSeat.setStatus(pressedSeat.status().pressSeat());
            image.setImageBitmap(getImageBitmap());
        } else if (maxSeatsClickListener != null) {
            maxSeatsClickListener.maxSeatsReached(pressedSeat.id());
        }
    }

    /**
     * Increases or decreases current selected seats count
     * @param seat Seat that has been clicked
     * @return false If selected seats reached max limit, false otherwise
     */
    public boolean updateSelectedSeatCount(Seat seat) {
        if (seat.status() == SeatStatus.FREE) {
            if (maxSelectedSeats == -1 || selectedSeats + 1 <= maxSelectedSeats) {
                selectedSeats++;
            } else if (selectedSeats + 1 > maxSelectedSeats) {
                return false;
            }
            return true;
        }
        selectedSeats--;
        return true;
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

        for(Zone zone : zones) {
            if (zone.width() == 0 || zone.height() == 0
                    || zone.leftTopX() + zone.width() > width
                    || zone.leftTopY() + zone.height() > height)
                continue;
            backgroundPaint.setColor(zone.color());
            int topX = offset/2 + zone.leftTopX() * (seatWidth + seatGap) + scene.getLeftYOffset();
            int topY = offset/2 + zone.leftTopY() * (seatWidth + seatGap) + scene.getTopXOffset();
            int bottomX = offset/2 + (zone.leftTopX() + zone.width()) * (seatWidth + seatGap) - seatGap + scene.getLeftYOffset();
            int bottomY = offset/2 + (zone.leftTopY() + zone.height()) * (seatWidth + seatGap) - seatGap + scene.getTopXOffset();
            tempCanvas.drawRect(topX, topY, bottomX, bottomY, backgroundPaint);
        }

        drawScene(tempCanvas);

        return tempBitmap;
    }

    private void drawScene(Canvas canvas) {
        if (scene.position == ScenePosition.NONE) {
            return;
        }
        backgroundPaint.setColor(sceneBackgroundColor);
        int topX=0, topY=0, bottomX=0, bottomY=0;
        if (scene.position == ScenePosition.NORTH) {
            int totalWidth = width * (seatWidth + seatGap) - seatGap + offset;
            topX = offset / 2;
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
            topY = offset / 2;
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
        zones = new ArrayList<>();
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
        public int width, height, offset;

        public void setScenePosition(ScenePosition position, int offset) {
            this.position = position;
            this.offset = offset;
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

        public Scene(ScenePosition position, int width, int height, int offset) {
            this.width = width;
            this.height = height;
            setScenePosition(position, offset);
        }

        public int getTopXOffset() {
            if (position == ScenePosition.NORTH) {
                return dimension + offset;
            }
            return 0;
        }

        public int getLeftYOffset() {
            if (position == ScenePosition.EAST) {
                return dimension + offset;
            }
            return 0;
        }

        public int getBottomXOffset() {
            if (position == ScenePosition.SOUTH) {
                return dimension + offset;
            }
            return 0;
        }

        public int getRightYOffset() {
            if (position == ScenePosition.WEST) {
                return dimension + offset;
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