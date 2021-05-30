/*
 * MIT License
 *
 * Copyright (c) [2021] [Rufen Khokhar]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.rkgroup.advanceqrscanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;


final class ScanAreaControlView extends View {
    private static final long ANIMATION_DELAY = 80L;
    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final int POINT_SIZE = 10;
    private static final String TAG = "ScanAreaControlView";
    private final Rect f16927j = new Rect();
    private final Paint insideFramePaint = new Paint();
    private final Rect f16932o = new Rect();
    private final int[] f16933p = new int[2];
    private int mDefaultLaserColor = Color.RED;
    private int scannerAlpha;
    private Paint mLaserPaint;
    private boolean mIsLaserEnabled;
    private boolean isFrameFixed;
    private Rect mFramingRect = new Rect();
    private Paint mMaskPaint;
    private Paint mFramePaint;
    private OnFrameChangeListener onFrameChangeListener;
    private Drawable frameScalarImage;
    private PointF touchPoint;
    private float x;
    private float y;
    private int mFrameCornersSize = 0;
    private int mFrameCornersRadius = 0;
    private float mFrameSize = 0.75f;
    private float mFrameRatioWidth = 1f;
    private float mFrameRatioHeight = 1f;
    private Path mPath;
    private boolean insideFrameColorEnable;

    ScanAreaControlView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    ScanAreaControlView(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //set up laser paint
        mLaserPaint = new Paint();
        mLaserPaint.setColor(mDefaultLaserColor);
        mLaserPaint.setStyle(Paint.Style.FILL);
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint.setStyle(Paint.Style.FILL);
        //  mMaskPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        mFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFramePaint.setStyle(Paint.Style.STROKE);
        //  mFramePaint.setColor(Color.TRANSPARENT);
        PointF pointF = new PointF();
        DefaultSizeCalculator.getDefaultSize(context, pointF);
        this.x = pointF.x / 2.0f;
        this.y = pointF.y / 2.0f;
        this.frameScalarImage = context.getResources().getDrawable(R.drawable.ic_scalling_frame);
        this.insideFramePaint.setColor(context.getResources().getColor(android.R.color.darker_gray));
        final Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        mPath = path;
    }

    public void drawLaser(Canvas canvas) {
        Rect framingRect = mFramingRect;

        // Draw a red "laser scanner" line through the middle to show decoding is active
        mLaserPaint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
        scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
        int middle = framingRect.height() / 2 + framingRect.top;
        canvas.drawRect(framingRect.left + 2, middle - 1, framingRect.right - 1, middle + 2, mLaserPaint);

        postInvalidateDelayed(ANIMATION_DELAY,
                framingRect.left - POINT_SIZE,
                framingRect.top - POINT_SIZE,
                framingRect.right + POINT_SIZE,
                framingRect.bottom + POINT_SIZE);
    }

    private void invalidateFrameRect() {
        invalidateFrameRect(getWidth(), getHeight());
    }

    private boolean m22648g(float f) {
        int i = this.f16932o.right;
        return ((float) (i - (this.frameScalarImage.getIntrinsicWidth() * 2))) <= f && f <= ((float) i);
    }

    private boolean m22649h(float f) {
        int i = this.f16932o.bottom;
        return ((float) (i - (this.frameScalarImage.getIntrinsicHeight() * 2))) <= f && f <= ((float) i);
    }

    private float[] m22650j(StringPreference wVar) {
        try {
            String o = wVar.mo16476o(getContext(), "auto");
            if (o != null) {
                String[] split = o.split(" ");
                if (split.length == 2) {
                    return new float[]{Float.parseFloat(split[0]), Float.parseFloat(split[1])};
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "m22650j: ", e);
        }
        return new float[]{this.x, this.y};
    }

    private void m22651l(float f, float f2, float f3, float f4) {
        float min = Math.min((float) (this.f16927j.width() / 2), Math.max((float) this.frameScalarImage.getIntrinsicWidth(), f3));
        float min2 = Math.min((float) (this.f16927j.height() / 2), Math.max((float) this.frameScalarImage.getIntrinsicHeight(), f4));
        this.mFramingRect.set(Math.round(f - min), Math.round(f2 - min2), Math.round(f + min), Math.round(f2 + min2));
    }

    @NonNull
    com.rkgroup.advanceqrscanner.Rect getmFramingRect() {
        return new com.rkgroup.advanceqrscanner.Rect(mFramingRect.left, mFramingRect.top, mFramingRect.right, mFramingRect.bottom);
    }

    void notifyOnFrameChange() {
        if (this.onFrameChangeListener != null) {
            this.onFrameChangeListener.onFrameChange(mFramingRect);
        }
    }

    /**
     * get inside frame color during scaling the frame
     *
     * @return color inside frame during scaling the frame
     */
    @ColorInt
    public int getInsideFrameColor() {
        return insideFramePaint.getColor();
    }

    /**
     * set inside frame color during scaling the frame
     *
     * @param color set inside frame color
     */
    void setInsideFrameColor(@ColorInt final int color) {
        insideFramePaint.setColor(color);
        invalidate();
    }

    /**
     * InsideFrameColorEnable during the scaling the frame
     *
     * @return true if InsideFrameColorEnable, false otherwise
     */
    boolean isInsideFrameColorEnable() {
        return insideFrameColorEnable;
    }

    void setInsideFrameColorEnable(boolean enabled) {
        insideFrameColorEnable = enabled;
        invalidate();
    }


    void mo16221k() {
        Point c = DefaultSizeCalculator.getRealSizeOfScreen(getContext());
        int i = c.y / 2;
        getLocationOnScreen(this.f16933p);
        int min = Math.min(Math.abs(i - this.f16933p[1]), Math.abs((this.f16933p[1] + getHeight()) - i));
        int i2 = i - this.f16933p[1];
        this.f16927j.set(0, i2 - min, getWidth(), i2 + min);
        float[] j = m22650j(c.x <= c.y ? com.rkgroup.advanceqrscanner.StringPreference.SCAN_AREA_SIZE_PORTRAIT : com.rkgroup.advanceqrscanner.StringPreference.SCAN_AREA_SIZE_LANDSCAPE);
        m22651l(this.f16927j.exactCenterX(), this.f16927j.exactCenterY(), j[0], j[1]);
        this.f16932o.set(this.mFramingRect);
        notifyOnFrameChange();

    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final Rect frame = mFramingRect;
        if (frame == null) {
            return;
        }
        final int width = getWidth();
        final int height = getHeight();
        final float top = frame.top;
        final float left = frame.left;
        final float right = frame.right;
        final float bottom = frame.bottom;
        final float frameCornersSize = mFrameCornersSize;
        final float frameCornersRadius = mFrameCornersRadius;
        final Path path = mPath;
        path.reset();
        if (frameCornersRadius > 0) {
            final float normalizedRadius = Math.min(frameCornersRadius, Math.max(frameCornersSize - 1, 0));
            path.moveTo(left, top + normalizedRadius);
            path.quadTo(left, top, left + normalizedRadius, top);
            path.lineTo(right - normalizedRadius, top);
            path.quadTo(right, top, right, top + normalizedRadius);
            path.lineTo(right, bottom - normalizedRadius);
            path.quadTo(right, bottom, right - normalizedRadius, bottom);
            path.lineTo(left + normalizedRadius, bottom);
            path.quadTo(left, bottom, left, bottom - normalizedRadius);
            path.lineTo(left, top + normalizedRadius);
            path.moveTo(0, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.lineTo(0, 0);
            canvas.drawPath(path, mMaskPaint);
            path.reset();
            path.moveTo(left, top + frameCornersSize);
            path.lineTo(left, top + normalizedRadius);
            path.quadTo(left, top, left + normalizedRadius, top);
            path.lineTo(left + frameCornersSize, top);
            path.moveTo(right - frameCornersSize, top);
            path.lineTo(right - normalizedRadius, top);
            path.quadTo(right, top, right, top + normalizedRadius);
            path.lineTo(right, top + frameCornersSize);
            path.moveTo(right, bottom - frameCornersSize);
            path.lineTo(right, bottom - normalizedRadius);
            path.quadTo(right, bottom, right - normalizedRadius, bottom);
            path.lineTo(right - frameCornersSize, bottom);
            path.moveTo(left + frameCornersSize, bottom);
            path.lineTo(left + normalizedRadius, bottom);
            path.quadTo(left, bottom, left, bottom - normalizedRadius);
        } else {
            path.moveTo(left, top);
            path.lineTo(right, top);
            path.lineTo(right, bottom);
            path.lineTo(left, bottom);
            path.lineTo(left, top);
            path.moveTo(0, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.lineTo(0, 0);
            canvas.drawPath(path, mMaskPaint);
            path.reset();
            path.moveTo(left, top + frameCornersSize);
            path.lineTo(left, top);
            path.lineTo(left + frameCornersSize, top);
            path.moveTo(right - frameCornersSize, top);
            path.lineTo(right, top);
            path.lineTo(right, top + frameCornersSize);
            path.moveTo(right, bottom - frameCornersSize);
            path.lineTo(right, bottom);
            path.lineTo(right - frameCornersSize, bottom);
            path.moveTo(left + frameCornersSize, bottom);
            path.lineTo(left, bottom);
        }
        path.lineTo(left, bottom - frameCornersSize);
        canvas.drawPath(path, mFramePaint);
        if (this.touchPoint != null) {
            canvas.drawRect(this.mFramingRect, this.insideFramePaint);
        }
        if (!isFrameFixed) {
            int intrinsicWidth = this.frameScalarImage.getIntrinsicWidth();
            int intrinsicHeight = this.frameScalarImage.getIntrinsicHeight();
            Rect rect = this.mFramingRect;
            int i = rect.right;
            int i2 = rect.bottom;
            this.frameScalarImage.setBounds(i - ((intrinsicWidth * 3) / 2), i2 - ((intrinsicHeight * 3) / 2), i - (intrinsicWidth / 2), i2 - (intrinsicHeight / 2));
            this.frameScalarImage.draw(canvas);
        }
        if (mIsLaserEnabled) {
            drawLaser(canvas);
        }

    }

    /**
     * currently laser color
     *
     * @return laser color
     */
    int getLaserColor() {
        return mDefaultLaserColor;
    }

    /**
     * set the laser color
     *
     * @param color color of laser
     */
    void setLaserColor(@ColorInt int color) {
        mLaserPaint.setColor(color);
        invalidate();
    }

    /**
     * check weather laser is enabled or not
     *
     * @return true if laser id enabled, false otherwise
     */
    boolean ismIsLaserEnabled() {
        return mIsLaserEnabled;
    }

    /**
     * set laser enable to show scanner laser
     *
     * @param mIsLaserEnabled true to enable the laser, false otherwise
     */

    void setmIsLaserEnabled(boolean mIsLaserEnabled) {
        this.mIsLaserEnabled = mIsLaserEnabled;
        invalidate();
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed && right > left && bottom > top) {
            mo16221k();
        }
        if (isFrameFixed) {
            invalidateFrameRect(right - left, bottom - top);
        }
    }

    boolean isFrameFixed() {
        return isFrameFixed;
    }

    void setFrameFixed(boolean isFrameFixed) {
        this.isFrameFixed = isFrameFixed;
        invalidate();
    }


    void setFrameAspectRatio(@FloatRange(from = 0, fromInclusive = false) final float ratioWidth,
                             @FloatRange(from = 0, fromInclusive = false) final float ratioHeight) {
        mFrameRatioWidth = ratioWidth;
        mFrameRatioHeight = ratioHeight;
        invalidateFrameRect();
        if (isLaidOut()) {
            invalidate();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(@NotNull MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (m22649h(y)) {
                float f = m22648g(x) ? ((float) this.mFramingRect.right) - x : Float.NaN;
                if (!Float.isNaN(f)) {
                    this.touchPoint = new PointF(f, ((float) this.mFramingRect.bottom) - y);
                    // m22652m();
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        invalidate();
                        return true;
                    }
                }
            }
        } else if (action != 1) {
            if (action != 2) {
                if (action == 3 && this.touchPoint != null) {
                    this.touchPoint = null;
                    invalidate();
                    return true;
                }
            } else if (this.touchPoint != null) {
                float exactCenterX = this.f16927j.exactCenterX();
                float exactCenterY = this.f16927j.exactCenterY();
                m22651l(exactCenterX, exactCenterY, (motionEvent.getX() + this.touchPoint.x) - exactCenterX, (motionEvent.getY() + this.touchPoint.y) - exactCenterY);
                invalidate();
                return true;
            }
        } else if (this.touchPoint != null) {
            this.touchPoint = null;
            this.f16932o.set(this.mFramingRect);
            Point c = DefaultSizeCalculator.getRealSizeOfScreen(getContext());
            StringPreference wVar = c.x <= c.y ? StringPreference.SCAN_AREA_SIZE_PORTRAIT : StringPreference.SCAN_AREA_SIZE_LANDSCAPE;
            Context context = getContext();
            wVar.mo16478q(context, (((float) this.f16932o.right) - this.f16927j.exactCenterX()) + " " + (this.f16927j.exactCenterY() - ((float) this.f16932o.top)));
            notifyOnFrameChange();
            invalidate();
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }


    @ColorInt
    int getMaskColor() {
        return mMaskPaint.getColor();
    }

    void setMaskColor(@ColorInt final int color) {
        mMaskPaint.setColor(color);
        if (isLaidOut()) {
            invalidate();
        }
    }

    @ColorInt
    int getFrameColor() {
        return mFramePaint.getColor();
    }

    void setFrameColor(@ColorInt final int color) {
        mFramePaint.setColor(color);
        if (isLaidOut()) {
            invalidate();
        }
    }

    @Px
    int getFrameThickness() {
        return (int) mFramePaint.getStrokeWidth();
    }

    void setFrameThickness(@Px final int thickness) {
        mFramePaint.setStrokeWidth(thickness);
        if (isLaidOut()) {
            invalidate();
        }
    }

    @Px
    int getFrameCornersSize() {
        return mFrameCornersSize;
    }

    void setFrameCornersSize(@Px final int size) {
        mFrameCornersSize = size;
        if (isLaidOut()) {
            invalidate();
        }
    }

    /**
     * set frame Scalar image color
     *
     * @param color drawable image
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void setFrameScalarImageColor(@ColorInt int color) {
        frameScalarImage.setTint(color);
        invalidate();
    }

    /**
     * set frame Scalar image
     *
     * @param frameScalarImage drawable image
     */
    void setFrameScalarImage(@NonNull Drawable frameScalarImage) {
        this.frameScalarImage = frameScalarImage;
        invalidate();
    }

    /**
     * set frame Scalar image
     *
     * @param frameScalarImage drawable resources id
     */
    void setFrameScalarImage(@DrawableRes int frameScalarImage) {
        setFrameScalarImage(getContext().getResources().getDrawable(frameScalarImage));
    }

    /**
     * set frame Scalar image
     *
     * @param frameScalarImage Bitmap image
     */
    void setFrameScalarImage(@NonNull Bitmap frameScalarImage) {
        setFrameScalarImage(new BitmapDrawable(getContext().getResources(), frameScalarImage));
    }

    @Px
    int getFrameCornersRadius() {
        return mFrameCornersRadius;
    }

    void setFrameCornersRadius(@Px final int radius) {
        mFrameCornersRadius = radius;
        if (isLaidOut()) {
            invalidate();
        }
    }

    @FloatRange(from = 0.1, to = 1.0)
    float getFrameSize() {
        return mFrameSize;
    }

    void setFrameSize(@FloatRange(from = 0.1, to = 1.0) final float size) {
        mFrameSize = size;
        invalidateFrameRect();
        if (isLaidOut()) {
            invalidate();
        }
    }


    @FloatRange(from = 0, fromInclusive = false)
    float getFrameAspectRatioWidth() {
        return mFrameRatioWidth;
    }

    void setFrameAspectRatioWidth(
            @FloatRange(from = 0, fromInclusive = false) final float ratioWidth) {
        mFrameRatioWidth = ratioWidth;
        invalidateFrameRect();
        if (isLaidOut()) {
            invalidate();
        }
    }

    @FloatRange(from = 0, fromInclusive = false)
    float getFrameAspectRatioHeight() {
        return mFrameRatioHeight;
    }

    void setFrameAspectRatioHeight(
            @FloatRange(from = 0, fromInclusive = false) final float ratioHeight) {
        mFrameRatioHeight = ratioHeight;
        invalidateFrameRect();
        if (isLaidOut()) {
            invalidate();
        }
    }

    void setOnScanAreaChangeListener(OnFrameChangeListener frameChangeListener) {
        this.onFrameChangeListener = frameChangeListener;
    }


    private void invalidateFrameRect(final int width, final int height) {
        if (width > 0 && height > 0) {
            final float viewAR = (float) width / (float) height;
            final float frameAR = mFrameRatioWidth / mFrameRatioHeight;
            final int frameWidth;
            final int frameHeight;
            if (viewAR <= frameAR) {
                frameWidth = Math.round(width * mFrameSize);
                frameHeight = Math.round(frameWidth / frameAR);
            } else {
                frameHeight = Math.round(height * mFrameSize);
                frameWidth = Math.round(frameHeight * frameAR);
            }
            final int frameLeft = (width - frameWidth) / 2;
            final int frameTop = (height - frameHeight) / 2;
            mFramingRect =
                    new Rect(frameLeft, frameTop, frameLeft + frameWidth, frameTop + frameHeight);
        }
    }


}

