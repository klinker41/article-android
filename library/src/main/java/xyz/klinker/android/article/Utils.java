/*
 * Copyright (C) 2017 Jake Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.klinker.android.article;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.EdgeEffect;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Utilities we use, mostly for UI modification.
 */
final class Utils {

    /**
     * Changes the text selection handle colors.
     */
    static void changeTextSelectionHandleColors(Context context, TextView textView, int color) {
        textView.setHighlightColor(Color.argb(
                40, Color.red(color), Color.green(color), Color.blue(color)));

        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            if (!editorField.isAccessible()) {
                editorField.setAccessible(true);
            }

            Object editor = editorField.get(textView);
            Class<?> editorClass = editor.getClass();

            String[] handleNames = {
                    "mSelectHandleLeft",
                    "mSelectHandleRight",
                    "mSelectHandleCenter"
            };
            String[] resNames = {
                    "mTextSelectHandleLeftRes",
                    "mTextSelectHandleRightRes",
                    "mTextSelectHandleRes"
            };

            for (int i = 0; i < handleNames.length; i++) {
                Field handleField = editorClass.getDeclaredField(handleNames[i]);
                if (!handleField.isAccessible()) {
                    handleField.setAccessible(true);
                }

                Drawable handleDrawable = (Drawable) handleField.get(editor);

                if (handleDrawable == null) {
                    Field resField = TextView.class.getDeclaredField(resNames[i]);
                    if (!resField.isAccessible()) {
                        resField.setAccessible(true);
                    }
                    int resId = resField.getInt(textView);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        handleDrawable = context.getDrawable(resId);
                    } else {
                        handleDrawable = context.getResources().getDrawable(resId);
                    }
                }

                if (handleDrawable != null) {
                    Drawable drawable = handleDrawable.mutate();
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    handleField.set(editor, drawable);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Checks if there is an app installed on the device that requests
     * @{link ArticleActivity.PERMISSION_SAVED_ARTICLE}.
     */
    static boolean saveArticlePermissionAvailable(Context context) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : apps) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS);

                String[] requestedPermissions = packageInfo.requestedPermissions;
                if (requestedPermissions != null) {
                    for (String permission : requestedPermissions) {
                        if (permission.equals(ArticleActivity.PERMISSION_SAVED_ARTICLE)) {
                            return true;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
