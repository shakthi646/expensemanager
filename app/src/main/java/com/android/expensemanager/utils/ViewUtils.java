package com.android.expensemanager.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

public class ViewUtils
{
    public static final String SERVICE_PREFS        = "ServicePrefs";

    private static Typeface rubikRegularTypeFace;
    private static Typeface rubikMediumTypeFace;
    private static Typeface rubikLiteTypeFace;
    private static Typeface rubikLightItalicTypeFace;


//    public static int getDrawableForNotifications(String notification_type)
//    {
//        int drawableId = R.drawable.attachment;
//
//        // TODO: 05/07/18 Add Notification Icon Selection
//        return drawableId;
//    }
//
//    public static int getColor(String status)
//    {
//        int color = R.color.amber_primary_dark_theme_color;
//
//        if(!TextUtils.isEmpty(status))
//        {
//            // TODO: 05/07/18 Add Notification Colors
//
//            Log.d("",""); // No I18N
//        }
//        return color;
//    }

//    public static int getPrimaryColorFromTheme(final Context context)
//    {
//        int theme = context.getSharedPreferences(FinanceUtil.USER_PREFS, 0).getInt(ZFPrefConstants.THEME, 0);
//        switch(theme)
//        {
//            case 0:
//                return R.color.colorPrimary;
//            case 1:
//                return R.color.red_theme_color;
//            case 2:
//                return R.color.green_theme_color;
//            case 3:
//                return R.color.violet_theme_color;
//            case 4:
//                return R.color.colorPrimary_black;
//
//            default:
//                return R.color.colorPrimary;
//        }
//    }

//    public static int getPrimaryDarkColorFromTheme(final Context context)
//    {
//        Resources rsrc  =   context.getResources();
//        int theme = context.getSharedPreferences(FinanceUtil.USER_PREFS, 0).getInt(ZFPrefConstants.THEME, 0);
//        switch(theme)
//        {
//            case 0:
//                return rsrc.getColor(R.color.colorPrimaryDark);
//            case 1:
//                return rsrc.getColor(R.color.red_primary_dark_theme_color);
//            case 2:
//                return rsrc.getColor(R.color.green_primary_dark_theme_color);
//            case 3:
//                return rsrc.getColor(R.color.violet_primary_dark_theme_color);
//            case 4:
//                return rsrc.getColor(R.color.colorPrimaryDark_black);
//
//            default:
//                return rsrc.getColor(R.color.green_primary_dark_theme_color);
//        }
//    }

//
//    public static int getNonDrawerTheme(final Context context)
//    {
//        int theme = context.getSharedPreferences(FinanceUtil.USER_PREFS, 0).getInt(ZFPrefConstants.THEME, 0);
//
//        switch (theme)
//        {
//            case 0:
//                return R.style.Blue_Theme_NoActionBarTheme;
//            case 1:
//                return R.style.Red_Theme_NoActionBarTheme;
//            case 2:
//                return R.style.Green_Theme_NoActionBarTheme;
//            case 3:
//                return R.style.Violet_Theme_NoActionBarTheme;
//            case 4:
//                return R.style.Black_Theme_NoActionBarTheme;
//            default:
//                return R.style.Blue_Theme_NoActionBarTheme;
//        }
//    }

//    public static int getNonDrawerThemeActionBar(final Context context)
//    {
//        int theme = context.getSharedPreferences(FinanceUtil.USER_PREFS, 0).getInt(ZFPrefConstants.THEME, 0);
//
//        switch (theme)
//        {
//            case 0:
//                return R.style.Blue_Theme;
//            case 1:
//                return R.style.Red_Theme;
//            case 2:
//                return R.style.Green_Theme;
//            case 3:
//                return R.style.Violet_Theme;
//            case 4:
//                return R.style.Black_Theme;
//            default:
//                return R.style.Blue_Theme;
//        }
//    }

    public static String removeTrailingZero(String value)
    {
        return value.contains(".")? value.replaceAll("0*$", "").replaceAll("\\.$", "") : value;
    }

    public static Typeface getRubikRegularTypeface(Context context)
    {
        if(rubikRegularTypeFace == null)
        {
            rubikRegularTypeFace 	=	Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Regular.ttf"); // No I18N
        }

        return rubikRegularTypeFace;
    }

    public static Typeface getRubikLiteTypeface(Context context)
    {
        if(rubikLiteTypeFace == null)
        {
            rubikLiteTypeFace 	=	Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Light.ttf"); // No I18N
        }

        return rubikLiteTypeFace;
    }

    public static Typeface getRubikMediumTypeface(Context context)
    {
        if(rubikMediumTypeFace == null)
        {
            rubikMediumTypeFace 	=	Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Medium.ttf"); // No I18N
        }

        return rubikMediumTypeFace;
    }

    public static Typeface getRubikLightItalicTypeface(Context context)
    {
        if(rubikLightItalicTypeFace == null)
        {
            rubikLightItalicTypeFace =	Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-LightItalic.ttf"); // No I18N
        }

        return rubikLightItalicTypeFace;
    }



    public static void expand(final View v)
    {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
                v.getLayoutParams().height = interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds()
            {
                return true;
            }
        };


        a.setDuration(500);
        v.startAnimation(a);
    }

    public static void collapse(final View v)
    {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
                if(interpolatedTime == 1)
                {
                    v.setVisibility(View.GONE);
                }
                else
                {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds()
            {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static void slideDown(final View view)
    {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(150);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void slideIntoVisibility(final View view)
    {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(150);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
}
