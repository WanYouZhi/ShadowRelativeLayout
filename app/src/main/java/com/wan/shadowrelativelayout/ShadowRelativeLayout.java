package com.wan.shadowrelativelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 2018/7/25
 * Created by WanYouZhi .
 * 使用圆角时，应设置圆角相同的background
 */

public class ShadowRelativeLayout extends RelativeLayout {
    private static final int DEFAULT_BLUR = 30;
    private static final int DEFAULT_ALPHA = 90;
    /**
     * 阴影的颜色, 需要带透明
     */
    private int shadowColor = Color.argb(DEFAULT_ALPHA, 0, 0, 0);
    /**
     * 阴影的大小范围 shadowBlur越大越模糊，越小越清晰
     */
    private float shadowBlur = DEFAULT_BLUR;

    /**
     * 阴影的圆角，只支持四角相同
     */
    private float shadowRadius = 0;

    /**
     * 阴影的偏移
     */
    private float shadowDx = 0;
    private float shadowDy = 0;

    public ShadowRelativeLayout(@NonNull Context context) {
        this(context, null);
    }

    public ShadowRelativeLayout(@NonNull Context context,
                                @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private boolean xmlBackground = true;

    public ShadowRelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        xmlBackground = false;
        setBackground(originBackground);
        dealAttrs(context, attrs);
        setPaint();
    }

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(getRectF(), shadowRadius, shadowRadius, mPaint);
        super.draw(canvas);
    }

    private Drawable originBackground;

    public Drawable getInsetBackground() {
        return super.getBackground();
    }

    @Override
    public Drawable getBackground() {
        return originBackground;
    }

    @Override
    public void setBackground(Drawable background) {
        originBackground = background;
        if (xmlBackground) { //padding还没好
            return;
        }
        if (background != null && !(background instanceof InsetDrawable)) {
            InsetDrawable drawable =
                    new InsetDrawable(background, getPaddingLeft(), getPaddingTop(),
                            getPaddingRight(), getPaddingBottom());
            background = drawable;
        }
        super.setBackground(background);
    }

    private RectF getRectF() {
        return new RectF(getPaddingLeft() + shadowDx, getPaddingTop() + shadowDy,
                getWidth() - getPaddingRight() + shadowDx,
                getHeight() - getPaddingBottom() + shadowDy);
    }

    private void dealAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowRelativeLayout);
        if (typedArray != null) {
            shadowColor = typedArray.getColor(R.styleable.ShadowRelativeLayout_shadow_color, shadowColor);
            shadowRadius =
                    typedArray.getDimension(R.styleable.ShadowRelativeLayout_shadow_radius, shadowRadius);
            shadowBlur =
                    typedArray.getDimension(R.styleable.ShadowRelativeLayout_shadow_blur, shadowBlur);
            shadowDx = typedArray.getDimension(R.styleable.ShadowRelativeLayout_shadow_dx, shadowDx);
            shadowDy = typedArray.getDimension(R.styleable.ShadowRelativeLayout_shadow_dy, shadowDy);
            typedArray.recycle();
        }
    }

    private void setPaint() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);  // 关闭硬件加速,阴影才会绘制
        // todo 从AttributeSet获取设置的值
        mPaint.setAntiAlias(true);
        mPaint.setColor(shadowColor);
        mPaint.setMaskFilter(new BlurMaskFilter(shadowBlur, BlurMaskFilter.Blur.NORMAL));
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public float getShadowBlur() {
        return shadowBlur;
    }

    public void setShadowBlur(float shadowBlur) {
        this.shadowBlur = shadowBlur;
    }

    public float getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public float getShadowDx() {
        return shadowDx;
    }

    public void setShadowDx(float shadowDx) {
        this.shadowDx = shadowDx;
    }

    public float getShadowDy() {
        return shadowDy;
    }

    public void setShadowDy(float shadowDy) {
        this.shadowDy = shadowDy;
    }
}
