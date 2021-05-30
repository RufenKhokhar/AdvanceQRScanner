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


enum StringPreference {
    SCAN_AREA_SIZE_PORTRAIT("scan_area_size_portrait"),
    SCAN_AREA_SIZE_LANDSCAPE("scan_area_size_landscape");
    public final String f17290m;

    StringPreference(String str) {
        this.f17290m = str;
    }

    String mo16476o(Context context, String str) {
        return FrameSetting.m23111e(context, this.f17290m, str);
    }

    void mo16478q(Context context, String str) {
        FrameSetting.m23119m(context, this.f17290m, str);
    }
}
