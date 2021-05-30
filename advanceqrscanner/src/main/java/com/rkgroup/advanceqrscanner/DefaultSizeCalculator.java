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

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;


class DefaultSizeCalculator {

    public static void getDefaultSize(@NonNull Context context, @NotNull PointF pointF) {
        Point c = getRealSizeOfScreen(context);
        float min = (float) Math.min(c.x, c.y);
        pointF.x = 0.5f * min;
        pointF.y = min * 0.44f;
    }

    @NonNull
    public static Point getRealSizeOfScreen(@NonNull Context context) {
        Point point = new Point();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        defaultDisplay.getRealSize(point);
        return point;
    }
}
