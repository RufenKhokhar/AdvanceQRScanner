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
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;


/**
 * A view to display  scanner preview
 *
 * @see Scanner
 */

public final class ScannerView extends FrameLayout {

    public static final boolean DEFAULT_IS_FRAME_FIXED = false;
    public static final boolean DEFAULT_INSIDE_FRAME_COLOR_ENABLED = true;
    private static final int DEFAULT_MASK_COLOR = 0x77000000;
    private static final int DEFAULT_FRAME_COLOR = Color.WHITE;
    private static final float DEFAULT_FRAME_THICKNESS_DP = 2f;
    private static final float DEFAULT_FRAME_ASPECT_RATIO_WIDTH = 1f;
    private static final float DEFAULT_FRAME_ASPECT_RATIO_HEIGHT = 1f;
    private static final float DEFAULT_FRAME_CORNER_SIZE_DP = 50f;
    private static final float DEFAULT_FRAME_CORNERS_RADIUS_DP = 0f;
    private static final float DEFAULT_FRAME_SIZE = 0.75f;
    private static final float FOCUS_AREA_SIZE_DP = 20f;
    private SurfaceView mPreviewView;
    private ScanAreaControlView mViewFinderView;
    private com.rkgroup.advanceqrscanner.Point mPreviewSize;
    private SizeListener mSizeListener;
    private Scanner mScanner;
    private int mFocusAreaSize;

    /**
     * A view to display  scanner preview
     *
     * @see Scanner
     */
    public ScannerView(@NonNull final Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    /**
     * A view to display  scanner preview
     *
     * @see Scanner
     */
    public ScannerView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
    }

    /**
     * A view to display  scanner preview
     *
     * @see Scanner
     */
    public ScannerView(@NonNull final Context context, @Nullable final AttributeSet attrs,
                       @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    /**
     * A view to display  scanner preview
     *
     * @see Scanner
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public ScannerView(final Context context, final AttributeSet attrs,
                       @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * check weather laser is enabled or not
     *
     * @return true if laser id enabled, false otherwise
     */
    public boolean ismIsLaserEnabled() {
        return getViewFinderView().ismIsLaserEnabled();
    }

    /**
     * set laser enable to show scanner laser
     *
     * @param mIsLaserEnabled true to enable the laser, false otherwise
     */

    public void setmIsLaserEnabled(boolean mIsLaserEnabled) {
        getViewFinderView().setmIsLaserEnabled(mIsLaserEnabled);

    }

    /**
     * currently laser color
     *
     * @return laser color
     */
    public int getLaserColor() {
        return getViewFinderView().getLaserColor();
    }

    /**
     * set the laser color
     *
     * @param color color of laser
     */
    public void setLaserColor(@ColorInt int color) {
        getViewFinderView().setLaserColor(color);
    }

    private void initialize(@NonNull final Context context, @Nullable final AttributeSet attrs,
                            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        mPreviewView = new SurfaceView(context);
        mPreviewView.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mViewFinderView = new ScanAreaControlView(context);
        mViewFinderView.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        final float density = context.getResources().getDisplayMetrics().density;
        mFocusAreaSize = Math.round(density * FOCUS_AREA_SIZE_DP);

        if (attrs == null) {
            mViewFinderView.setFrameAspectRatio(DEFAULT_FRAME_ASPECT_RATIO_WIDTH, DEFAULT_FRAME_ASPECT_RATIO_HEIGHT);
            mViewFinderView.setMaskColor(DEFAULT_MASK_COLOR);
            mViewFinderView.setFrameColor(DEFAULT_FRAME_COLOR);
            mViewFinderView.setFrameThickness(Math.round(DEFAULT_FRAME_THICKNESS_DP * density));
            mViewFinderView.setFrameCornersSize(Math.round(DEFAULT_FRAME_CORNER_SIZE_DP * density));
            mViewFinderView.setFrameCornersRadius(Math.round(DEFAULT_FRAME_CORNERS_RADIUS_DP * density));
            mViewFinderView.setFrameSize(DEFAULT_FRAME_SIZE);

        } else {
            TypedArray a = null;
            try {
                a = context.getTheme()
                        .obtainStyledAttributes(attrs, R.styleable.ScannerView, defStyleAttr,
                                defStyleRes);
                setMaskColor(a.getColor(R.styleable.ScannerView_maskColor, DEFAULT_MASK_COLOR));
                setFrameColor(
                        a.getColor(R.styleable.ScannerView_frameColor, DEFAULT_FRAME_COLOR));
                setFrameThickness(
                        a.getDimensionPixelOffset(R.styleable.ScannerView_frameThickness,
                                Math.round(DEFAULT_FRAME_THICKNESS_DP * density)));
                setFrameCornersSize(
                        a.getDimensionPixelOffset(R.styleable.ScannerView_frameCornersSize,
                                Math.round(DEFAULT_FRAME_CORNER_SIZE_DP * density)));
                setFrameCornersRadius(
                        a.getDimensionPixelOffset(R.styleable.ScannerView_frameCornersRadius,
                                Math.round(DEFAULT_FRAME_CORNERS_RADIUS_DP * density)));
                setFrameAspectRatio(a.getFloat(R.styleable.ScannerView_frameAspectRatioWidth,
                        DEFAULT_FRAME_ASPECT_RATIO_WIDTH),
                        a.getFloat(R.styleable.ScannerView_frameAspectRatioHeight,
                                DEFAULT_FRAME_ASPECT_RATIO_HEIGHT));
                setFrameSize(a.getFloat(R.styleable.ScannerView_frameSize, DEFAULT_FRAME_SIZE));
                setFrameFixed(a.getBoolean(R.styleable.ScannerView_frameFixedSize, DEFAULT_IS_FRAME_FIXED));
                setInsideFrameColorEnable(a.getBoolean(R.styleable.ScannerView_insideFrameColorEnable, DEFAULT_INSIDE_FRAME_COLOR_ENABLED));
                setInsideFrameColor(a.getInt(R.styleable.ScannerView_insideFrameColor, Color.WHITE));

                Drawable drawable = a.getDrawable(R.styleable.ScannerView_frameScalarImage);
                if (drawable != null) {
                    setFrameScalarImage(drawable);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setFrameScalarImageColor(a.getColor(R.styleable.ScannerView_frameScalarImageColor, Color.WHITE));
                }
                setmIsLaserEnabled(a.getBoolean(R.styleable.ScannerView_enableLaser, true));
                setLaserColor(a.getColor(R.styleable.ScannerView_laserColor, Color.RED));

            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }
        addView(mPreviewView);
        addView(mViewFinderView);
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right,
                            final int bottom) {
        performLayout(right - left, bottom - top);
    }

    @Override
    protected void onSizeChanged(final int width, final int height, final int oldWidth,
                                 final int oldHeight) {
        performLayout(width, height);
        final SizeListener listener = mSizeListener;
        if (listener != null) {
            listener.onSizeChanged(width, height);
        }
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(@NonNull final MotionEvent event) {
        final Scanner scanner = mScanner;
        final com.rkgroup.advanceqrscanner.Rect frameRect = getFrameRect();
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        if (scanner != null && frameRect != null &&
                scanner.isAutoFocusSupportedOrUnknown() && scanner.isTouchFocusEnabled() &&
                event.getAction() == MotionEvent.ACTION_DOWN && frameRect.isPointInside(x, y)) {
            final int areaSize = mFocusAreaSize;
            scanner.performTouchFocus(
                    new com.rkgroup.advanceqrscanner.Rect(x - areaSize, y - areaSize, x + areaSize, y + areaSize)
                            .fitIn(frameRect));
        }
        return super.onTouchEvent(event);
    }

    /**
     * set frame Scalar image color
     *
     * @param color drawable image
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setFrameScalarImageColor(@ColorInt int color) {
        getViewFinderView().setFrameScalarImageColor(color);
    }

    /**
     * get inside frame color during scaling the frame
     *
     * @return color inside frame during scaling the frame
     */
    @ColorInt
    public int getInsideFrameColor() {
        return getViewFinderView().getInsideFrameColor();
    }

    /**
     * set inside frame color during scaling the frame
     *
     * @param color set inside frame color
     */
    void setInsideFrameColor(@ColorInt final int color) {
        getViewFinderView().setInsideFrameColor(color);
    }

    /**
     * view finder frame is fixed or not
     *
     * @return true if fixed size, false otherwise
     */
    public boolean isFrameFixed() {
        return getViewFinderView().isFrameFixed();
    }

    /**
     * set view finder frame fixed size
     *
     * @param isFrameFixed true if fixed size, false otherwise
     */
    public void setFrameFixed(boolean isFrameFixed) {
        getViewFinderView().setFrameFixed(isFrameFixed);
    }

    /**
     * InsideFrameColorEnable during the scaling the frame
     *
     * @return true if InsideFrameColorEnable, false otherwise
     */
    boolean isInsideFrameColorEnable() {
        return getViewFinderView().isInsideFrameColorEnable();
    }

    void setInsideFrameColorEnable(boolean enabled) {
        getViewFinderView().setInsideFrameColorEnable(enabled);
    }

    /**
     * Get current mask color
     *
     * @see #setMaskColor
     */
    @ColorInt
    public int getMaskColor() {
        return mViewFinderView.getMaskColor();
    }

    /**
     * Set color of the space outside of the framing rect
     *
     * @param color Mask color
     */
    public void setMaskColor(@ColorInt final int color) {
        mViewFinderView.setMaskColor(color);
    }

    /**
     * Get current frame color
     *
     * @see #setFrameColor
     */
    @ColorInt
    public int getFrameColor() {
        return mViewFinderView.getFrameColor();
    }

    /**
     * Set color of the frame
     *
     * @param color Frame color
     */
    public void setFrameColor(@ColorInt final int color) {
        mViewFinderView.setFrameColor(color);
    }

    /**
     * Get current frame thickness
     *
     * @see #setFrameThickness
     */
    @Px
    public int getFrameThickness() {
        return mViewFinderView.getFrameThickness();
    }

    /**
     * Set frame thickness
     *
     * @param thickness Frame thickness in pixels
     */
    public void setFrameThickness(@Px final int thickness) {
        if (thickness < 0) {
            throw new IllegalArgumentException("Frame thickness can't be negative");
        }
        mViewFinderView.setFrameThickness(thickness);
    }

    /**
     * Get current frame corners size
     *
     * @see #setFrameCornersSize
     */
    @Px
    public int getFrameCornersSize() {
        return mViewFinderView.getFrameCornersSize();
    }

    /**
     * Set size of the frame corners
     *
     * @param size Size in pixels
     */
    public void setFrameCornersSize(@Px final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Frame corners size can't be negative");
        }
        mViewFinderView.setFrameCornersSize(size);
    }

    /**
     * Get current frame corners radius
     *
     * @see #setFrameCornersRadius
     */
    @Px
    public int getFrameCornersRadius() {
        return mViewFinderView.getFrameCornersRadius();
    }

    /**
     * Set current frame corners radius
     *
     * @param radius Frame corners radius in pixels
     */
    public void setFrameCornersRadius(@Px final int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Frame corners radius can't be negative");
        }
        mViewFinderView.setFrameCornersRadius(radius);
    }

    /**
     * Get current frame size
     *
     * @see #setFrameSize
     */
    @FloatRange(from = 0.1, to = 1.0)
    public float getFrameSize() {
        return mViewFinderView.getFrameSize();
    }

    /**
     * Set relative frame size where 1.0 means full size
     *
     * @param size Relative frame size between 0.1 and 1.0
     */
    public void setFrameSize(@FloatRange(from = 0.1, to = 1) final float size) {
        if (size < 0.1 || size > 1) {
            throw new IllegalArgumentException(
                    "Max frame size value should be between 0.1 and 1, inclusive");
        }
        mViewFinderView.setFrameSize(size);
    }

    /**
     * Get current frame aspect ratio width
     *
     * @see #setFrameAspectRatioWidth
     * @see #setFrameAspectRatio
     */
    @FloatRange(from = 0, fromInclusive = false)
    public float getFrameAspectRatioWidth() {
        return mViewFinderView.getFrameAspectRatioWidth();
    }

    /**
     * Set frame aspect ratio width
     *
     * @param ratioWidth Frame aspect ratio width
     * @see #setFrameAspectRatio
     */
    public void setFrameAspectRatioWidth(
            @FloatRange(from = 0, fromInclusive = false) final float ratioWidth) {
        if (ratioWidth <= 0) {
            throw new IllegalArgumentException(
                    "Frame aspect ratio values should be greater than zero");
        }
        mViewFinderView.setFrameAspectRatioWidth(ratioWidth);
    }

    /**
     * Get current frame aspect ratio height
     *
     * @see #setFrameAspectRatioHeight
     * @see #setFrameAspectRatio
     */
    @FloatRange(from = 0, fromInclusive = false)
    public float getFrameAspectRatioHeight() {
        return mViewFinderView.getFrameAspectRatioHeight();
    }

    /**
     * Set frame aspect ratio height
     *
     * @param ratioHeight Frame aspect ratio width
     * @see #setFrameAspectRatio
     */
    public void setFrameAspectRatioHeight(
            @FloatRange(from = 0, fromInclusive = false) final float ratioHeight) {
        if (ratioHeight <= 0) {
            throw new IllegalArgumentException(
                    "Frame aspect ratio values should be greater than zero");
        }
        mViewFinderView.setFrameAspectRatioHeight(ratioHeight);
    }

    /**
     * Set frame aspect ratio (ex. 1:1, 15:10, 16:9, 4:3)
     *
     * @param ratioWidth  Frame aspect ratio width
     * @param ratioHeight Frame aspect ratio height
     */
    public void setFrameAspectRatio(
            @FloatRange(from = 0, fromInclusive = false) final float ratioWidth,
            @FloatRange(from = 0, fromInclusive = false) final float ratioHeight) {
        if (ratioWidth <= 0 || ratioHeight <= 0) {
            throw new IllegalArgumentException(
                    "Frame aspect ratio values should be greater than zero");
        }
        mViewFinderView.setFrameAspectRatio(ratioWidth, ratioHeight);
    }


    /**
     * Whether if mask is currently visible
     *
     * @see #setMaskVisible
     */
    public boolean isMaskVisible() {
        return mViewFinderView.getVisibility() == VISIBLE;
    }

    /**
     * Set whether mask is visible or not
     *
     * @param visible Visibility
     */
    public void setMaskVisible(final boolean visible) {
        mViewFinderView.setVisibility(visible ? VISIBLE : INVISIBLE);
    }


    @NonNull
    SurfaceView getPreviewView() {
        return mPreviewView;
    }

    @NonNull
    ScanAreaControlView getViewFinderView() {
        return mViewFinderView;
    }

    @Nullable
    com.rkgroup.advanceqrscanner.Rect getFrameRect() {
        return mViewFinderView.getmFramingRect();
    }

    void setPreviewSize(@Nullable final com.rkgroup.advanceqrscanner.Point previewSize) {
        mPreviewSize = previewSize;
        requestLayout();
    }

    void setSizeListener(@Nullable final SizeListener sizeListener) {
        mSizeListener = sizeListener;
    }

    void setScanner(@NonNull final Scanner scanner) {
        if (mScanner != null) {
            throw new IllegalStateException("Code scanner has already been set");
        }
        mScanner = scanner;
    }


    private void performLayout(final int width, final int height) {
        final com.rkgroup.advanceqrscanner.Point previewSize = mPreviewSize;
        if (previewSize == null) {
            mPreviewView.layout(0, 0, width, height);
        } else {
            int frameLeft = 0;
            int frameTop = 0;
            int frameRight = width;
            int frameBottom = height;
            final int previewWidth = previewSize.getX();
            if (previewWidth > width) {
                final int d = (previewWidth - width) / 2;
                frameLeft -= d;
                frameRight += d;
            }
            final int previewHeight = previewSize.getY();
            if (previewHeight > height) {
                final int d = (previewHeight - height) / 2;
                frameTop -= d;
                frameBottom += d;
            }
            mPreviewView.layout(frameLeft, frameTop, frameRight, frameBottom);
        }
        mViewFinderView.layout(0, 0, width, height);

    }

    /**
     * set frame Scalar image
     *
     * @param frameScalarImage drawable image
     */
    public void setFrameScalarImage(@NonNull Drawable frameScalarImage) {
        getViewFinderView().setFrameScalarImage(frameScalarImage);
    }

    /**
     * set frame Scalar image
     *
     * @param frameScalarImage drawable resources id
     */
    public void setFrameScalarImage(@DrawableRes int frameScalarImage) {
        getViewFinderView().setFrameScalarImage(frameScalarImage);
    }

    /**
     * set frame Scalar image
     *
     * @param frameScalarImage Bitmap image
     */
    public void setFrameScalarImage(@NonNull Bitmap frameScalarImage) {
        getViewFinderView().setFrameScalarImage(frameScalarImage);
    }

    interface SizeListener {
        void onSizeChanged(int width, int height);
    }


}
