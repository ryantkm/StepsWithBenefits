package com.eventdee.stepswithbenefits;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import static java.lang.Math.cos;

public class ArcProgress extends View {

    private Paint textPaint;
    private Paint knobPaint;
    private Paint baseArcPaint;
    private Paint fillArcPaint;

    // stroke related variables
    private float finishedStrokeWidth;
    private float unfinishedStrokeWidth;

    private RectF rectF = new RectF();
    float radius;

    private float suffixTextSize;
    private float bottomTextSize;
    private String bottomText;
    private float unitTextSize;
    private String unitText;
    private int unitTextColor;
    private float topTextSize;
    private String topText;
    private int topTextColor;
    private float textSize;
    private int textColor;
    private int progress = 0;
    private int max;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private float arcAngle;
    private String suffixText = "%";
    private float suffixTextPadding;

    private float arcBottomHeight;

    // add knob size, color
    private int knobRadius;
    private int knobFillColor;

    private final int default_finished_color = Color.WHITE;
    private final int default_unfinished_color = Color.BLACK;
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final float default_suffix_text_size;
    private final float default_suffix_padding;
    private final float default_bottom_text_size;
    private final float default_unit_text_size;
    private final float default_top_text_size;
    private final float default_stroke_width;
    private final String default_suffix_text;
    private final int default_max = 100;
    private final float default_arc_angle = 360 * 0.8f;
    private float default_text_size;
    private final int min_size;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size";
    private static final String INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding";
    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";
    private static final String INSTANCE_UNIT_TEXT_SIZE = "unit_text_size";
    private static final String INSTANCE_UNIT_TEXT_COLOR = "unit_text_color";
    private static final String INSTANCE_UNIT_TEXT = "unit_text";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TOP_TEXT_SIZE = "top_text_size";
    private static final String INSTANCE_TOP_TEXT_COLOR = "top_text_color";
    private static final String INSTANCE_TOP_TEXT = "top_text";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_ARC_ANGLE = "arc_angle";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_FINISHED_STROKE_WIDTH = "finished_stroke_width";
    private static final String INSTANCE_UNFINISHED_STROKE_WIDTH = "unfinished_stroke_width";
    private static final String INSTANCE_KNOB_RADIUS = "knob_radius";
    private static final String INSTANCE_KNOB_COLOR = "knob_color";

    public ArcProgress(Context context) {
        this(context, null);
    }

    public ArcProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null); // need to disable hardware acceleration for blur mask to work

        default_text_size = Utils.sp2px(getResources(), 18);
        min_size = (int) Utils.dp2px(getResources(), 100);
        default_text_size = Utils.sp2px(getResources(), 48);
        default_suffix_text_size = Utils.sp2px(getResources(), 15);
        default_suffix_padding = Utils.dp2px(getResources(), 4);
        default_suffix_text = "%";
        default_bottom_text_size = Utils.sp2px(getResources(), 10);
        default_unit_text_size = Utils.sp2px(getResources(), 16);
        default_top_text_size = Utils.sp2px(getResources(), 20);
        default_stroke_width = Utils.dp2px(getResources(), 4);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);
        textColor = attributes.getColor(R.styleable.ArcProgress_arc_text_color, default_text_color);
        unitTextColor = attributes.getColor(R.styleable.ArcProgress_arc_unit_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.ArcProgress_arc_text_size, default_text_size);
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, default_arc_angle);
        setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, default_max));
        setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, 0));
        finishedStrokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_finishedStrokeWidth, default_stroke_width);
        unfinishedStrokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_unfinishedStrokeWidth, default_stroke_width);
        suffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_size, default_suffix_text_size);
        suffixText = TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_suffix_text)) ? default_suffix_text : attributes.getString(R.styleable.ArcProgress_arc_suffix_text);
        suffixTextPadding = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_padding, default_suffix_padding);
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, default_bottom_text_size);
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text);
        unitTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_unit_text_size, default_unit_text_size);
        unitText = attributes.getString(R.styleable.ArcProgress_arc_unit_text);
        topTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_top_text_size, default_top_text_size);
        topText = attributes.getString(R.styleable.ArcProgress_arc_top_text);
        topTextColor = attributes.getColor(R.styleable.ArcProgress_arc_top_text_color, default_text_color);
        // add knob size, color
        knobRadius = attributes.getDimensionPixelSize(R.styleable.ArcProgress_arc_knobRadius, 0);
        knobFillColor = attributes.getColor(R.styleable.ArcProgress_arc_knobFillColor, default_finished_color);
    }

    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        fillArcPaint = new Paint();
        fillArcPaint.setColor(finishedStrokeColor);
        fillArcPaint.setAntiAlias(true);
        fillArcPaint.setStrokeWidth(finishedStrokeWidth);
        fillArcPaint.setStyle(Paint.Style.STROKE);
        fillArcPaint.setStrokeCap(Paint.Cap.ROUND);
        fillArcPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));

        baseArcPaint = new Paint();
        baseArcPaint.setColor(default_unfinished_color);
        baseArcPaint.setAntiAlias(true);
        baseArcPaint.setStrokeWidth(unfinishedStrokeWidth);
        baseArcPaint.setStyle(Paint.Style.STROKE);
        baseArcPaint.setStrokeCap(Paint.Cap.ROUND);
        baseArcPaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));

        knobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        knobPaint.setColor(knobFillColor);
        knobPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    public float getUnfinishedStrokeWidth() {
        return unfinishedStrokeWidth;
    }

    public void setUnfinishedStrokeWidth(float strokeWidth) {
        unfinishedStrokeWidth = strokeWidth;
        this.invalidate();
    }

    public float getFinishedStrokeWidth() {
        return finishedStrokeWidth;
    }

    public void setFinishedStrokeWidth(float strokeWidth) {
        finishedStrokeWidth = strokeWidth;
        this.invalidate();
    }

    public float getSuffixTextSize() {
        return suffixTextSize;
    }

    public void setSuffixTextSize(float suffixTextSize) {
        this.suffixTextSize = suffixTextSize;
        this.invalidate();
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        this.invalidate();
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
        this.invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress >= getMax()) {
            finishedStrokeColor = Color.rgb(66, 145, 241);
        } else {
            finishedStrokeColor = default_finished_color;
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getBottomTextSize() {
        return bottomTextSize;
    }

    public void setBottomTextSize(float bottomTextSize) {
        this.bottomTextSize = bottomTextSize;
        this.invalidate();
    }

    public float getUnitTextSize() {
        return unitTextSize;
    }

    public void setUnitTextSize(float unitTextSize) {
        this.unitTextSize = unitTextSize;
        this.invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    public int getFinishedStrokeColor() {
        return finishedStrokeColor;
    }

    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.finishedStrokeColor = finishedStrokeColor;
        this.invalidate();
    }

    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
        this.invalidate();
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
        this.invalidate();
    }

    public String getSuffixText() {
        return suffixText;
    }

    public void setSuffixText(String suffixText) {
        this.suffixText = suffixText;
        this.invalidate();
    }

    public float getSuffixTextPadding() {
        return suffixTextPadding;
    }

    public void setSuffixTextPadding(float suffixTextPadding) {
        this.suffixTextPadding = suffixTextPadding;
        this.invalidate();
    }

    public int getUnitTextColor() {
        return unitTextColor;
    }

    public void setUnitTextColor(int unitTextColor) {
        this.unitTextColor = unitTextColor;
    }

    public float getTopTextSize() {
        return topTextSize;
    }

    public void setTopTextSize(float topTextSize) {
        this.topTextSize = topTextSize;
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public int getTopTextColor() {
        return topTextColor;
    }

    public void setTopTextColor(int topTextColor) {
        this.topTextColor = topTextColor;
    }

    public int getKnobRadius() {
        return knobRadius;
    }

    public void setKnobRadius(int knobRadius) {
        this.knobRadius = knobRadius;
    }

    public int getKnobFillColor() {
        return knobFillColor;
    }

    public void setKnobFillColor(int knobFillColor) {
        this.knobFillColor = knobFillColor;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return min_size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSizeAndState(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec, 0), resolveSizeAndState(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec, 0));
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(finishedStrokeWidth / 2f + knobRadius, finishedStrokeWidth / 2f + knobRadius, width - knobRadius, MeasureSpec.getSize(heightMeasureSpec) - knobRadius);
        radius = width / 2f;
        float angle = (360 - arcAngle) / 2f;
        arcBottomHeight = radius * (float) (1 - cos(angle / 180 * Math.PI));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 270 - arcAngle / 2f;
        float finishedSweepAngle;

        if (progress >= getMax()) {
            finishedSweepAngle = arcAngle;
        } else {
            finishedSweepAngle = progress / (float) getMax() * arcAngle;
        }

        float finishedStartAngle = startAngle;
        if(progress == 0) finishedStartAngle = 0.01f;

        canvas.drawArc(rectF, startAngle, arcAngle, false, baseArcPaint);
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, fillArcPaint);

        double endX = Math.cos(Math.toRadians(finishedStartAngle + finishedSweepAngle)) * (radius - knobRadius) + rectF.centerX();
        double endY = Math.sin(Math.toRadians(finishedStartAngle + finishedSweepAngle)) * (radius - knobRadius) + rectF.centerY();
        canvas.drawCircle((float) endX, (float) endY, knobRadius ,knobPaint);

        String text = String.valueOf(getProgress());
        if (!TextUtils.isEmpty(text)) {
            textPaint.setColor(textColor);
            textPaint.setTextSize(textSize);
            float textHeight = textPaint.descent() + textPaint.ascent();
            float textBaseline = (getHeight() - textHeight) / 2.0f;
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, textBaseline + 10f, textPaint);
            textPaint.setTextSize(suffixTextSize);
            float suffixHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(suffixText, getWidth() / 2.0f  + textPaint.measureText(text) + suffixTextPadding, textBaseline + textHeight - suffixHeight, textPaint);
            // unit text below main text
            textPaint.setTextSize(unitTextSize);
            textPaint.setColor(unitTextColor);
            canvas.drawText(unitText, (getWidth() - textPaint.measureText(unitText)) / 2.0f, textBaseline + 75f, textPaint);

            if (!TextUtils.isEmpty(topText)) {
                // unit text above main text
                textPaint.setTextSize(topTextSize);
                textPaint.setColor(topTextColor);
                canvas.drawText(topText, (getWidth() - textPaint.measureText(topText)) / 2.0f, textBaseline - 150f, textPaint);
            }
        }

        if(arcBottomHeight == 0) {
            float radius = getWidth() / 2f;
            float angle = (360 - arcAngle) / 2f;
            arcBottomHeight = radius * (float) (1 - cos(angle / 180 * Math.PI));
        }

        if (!TextUtils.isEmpty(getBottomText())) {
            textPaint.setTextSize(bottomTextSize);
            float bottomTextBaseline = getHeight() - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(getBottomText(), (getWidth() - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_FINISHED_STROKE_WIDTH, getFinishedStrokeWidth());
        bundle.putFloat(INSTANCE_UNFINISHED_STROKE_WIDTH, getUnfinishedStrokeWidth());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding());
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize());
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText());
        bundle.putFloat(INSTANCE_UNIT_TEXT_SIZE, getUnitTextSize());
        bundle.putString(INSTANCE_UNIT_TEXT, getUnitText());
        bundle.putInt(INSTANCE_UNIT_TEXT_COLOR, getUnitTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle());
        bundle.putString(INSTANCE_SUFFIX, getSuffixText());
        bundle.putInt(INSTANCE_KNOB_RADIUS, getKnobRadius());
        bundle.putInt(INSTANCE_KNOB_COLOR, getKnobFillColor());
        bundle.putFloat(INSTANCE_TOP_TEXT_SIZE, getTopTextSize());
        bundle.putString(INSTANCE_TOP_TEXT, getTopText());
        bundle.putInt(INSTANCE_TOP_TEXT_COLOR, getTopTextColor());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            finishedStrokeWidth = bundle.getFloat(INSTANCE_FINISHED_STROKE_WIDTH);
            unfinishedStrokeWidth = bundle.getFloat(INSTANCE_UNFINISHED_STROKE_WIDTH);
            suffixTextSize = bundle.getFloat(INSTANCE_SUFFIX_TEXT_SIZE);
            suffixTextPadding = bundle.getFloat(INSTANCE_SUFFIX_TEXT_PADDING);
            bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE);
            bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT);
            unitTextSize = bundle.getFloat(INSTANCE_UNIT_TEXT_SIZE);
            unitTextColor = bundle.getInt(INSTANCE_UNIT_TEXT_COLOR);
            unitText = bundle.getString(INSTANCE_UNIT_TEXT);
            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            suffixText = bundle.getString(INSTANCE_SUFFIX);
            knobRadius = bundle.getInt(INSTANCE_KNOB_RADIUS);
            knobFillColor = bundle.getInt(INSTANCE_KNOB_COLOR);
            arcAngle = bundle.getFloat(INSTANCE_ARC_ANGLE);
            topTextSize = bundle.getFloat(INSTANCE_TOP_TEXT_SIZE);
            topTextColor = bundle.getInt(INSTANCE_TOP_TEXT_COLOR);
            topText = bundle.getString(INSTANCE_TOP_TEXT);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
