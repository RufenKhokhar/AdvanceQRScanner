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
import android.content.SharedPreferences;


public class FrameSetting {
    private static SharedPreferences preferences;

    static String m23111e(Context context, String str, String str2) {
        return m23114h(context).getString(str, str2);
    }

    private static SharedPreferences m23114h(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        }
        return preferences;
    }

    static void m23119m(Context context, String str, String str2) {
        m23114h(context).edit().putString(str, str2).apply();
    }


}
