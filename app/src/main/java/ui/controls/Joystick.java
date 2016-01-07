package ui.controls;

import org.libsdl.app.SDLActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import ui.activity.GameActivity;

public class Joystick extends View {

    // =========================================
    // Private Members
    // =========================================

    private final String TAG = "JoystickView";
    private Paint circlePaint;
    private double touchX, touchY;
    private int innerPadding;
    private int handleRadius;
    private int handleInnerBoundaries;
    private int sensitivity;
    public static boolean isGameEnabled = false;

    // =========================================
    // Constructors
    // =========================================

    public Joystick(Context context) {
        super(context);
        initJoystickView();
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystickView();
    }

    public Joystick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initJoystickView();
    }

    // =========================================
    // Initialization
    // =========================================

    private void initJoystickView() {
        setFocusable(true);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		circlePaint.setColor(Color.argb(40, 255, 255, 255));
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerPadding = 10;
        sensitivity = 10;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Here we make sure that we have a perfect circle
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);
        int d = Math.min(measuredWidth, measuredHeight);

        handleRadius = (int) (d * 0.25);
        handleInnerBoundaries = handleRadius;

        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        int result = 0;
        // Decode the measurement specifications.
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            result = 200;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (!isGameEnabled) {
            final int px = getMeasuredWidth() / 2;
            final int py = getMeasuredHeight() / 2;
            final int radius = Math.min(px, py);
            canvas.drawCircle(px, py, radius - innerPadding, circlePaint);
            canvas.save();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        playerMovement(event);
        return true;
    }


    private void playerMovement(MotionEvent event) {
        int actionType = event.getAction();
        switch (actionType) {
            case MotionEvent.ACTION_MOVE: {

                int px = getMeasuredWidth() / 2;
                int py = getMeasuredHeight() / 2;
                final int radius = Math.min(px, py) - handleInnerBoundaries;
                touchX = (event.getX() - px);
                touchX = Math.max(Math.min(touchX, radius), -radius);
                touchY = (event.getY() - py);
                touchY = Math.max(Math.min(touchY, radius), -radius);
                if (touchY < -radius / 3 && (touchX > 0 || touchX < 0))
                    SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_W);
                else
                    SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_W);

                if (touchY > radius / 3 && (touchX > 0 || touchX < 0))
                    SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_S);

                else
                    SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_S);

                if ((touchX < -radius / 3) && (touchY > 0 || touchY < 0))
                    SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_A);

                else
                    SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_A);

                if ((touchX > radius / 3) && (touchY > 0 || touchY < 0))
                    SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_D);
                else
                    SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_D);

                break;
            }
            case MotionEvent.ACTION_UP: {
                releaseKeys();

                break;
            }
            default: {
                releaseKeys();
                break;
            }
        }
    }

    private void releaseKeys() {
        SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_W);
        SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_S);
        SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_A);
        SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_D);

    }

}