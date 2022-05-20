package com.itant.md.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 监听光标移动的EditText
 */
public class CursorEditText extends androidx.appcompat.widget.AppCompatEditText {

    public interface OnCursorMoveListener {
        void onCursorMove();
    }

    private OnCursorMoveListener cursorMoveListener;

    public CursorEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CursorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CursorEditText(Context context) {
        super(context);
    }

    public OnCursorMoveListener getCursorMoveListener() {
        return cursorMoveListener;
    }

    public void setCursorMoveListener(OnCursorMoveListener cursorMoveListener) {
        this.cursorMoveListener = cursorMoveListener;
    }

    @Override
     protected void onSelectionChanged(int selStart, int selEnd) {
         super.onSelectionChanged(selStart, selEnd);
         if (cursorMoveListener != null) {
             cursorMoveListener.onCursorMove();
         }
     }
}