package com.dragon.ide.ui.view;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.dragon.ide.R;
import com.dragon.ide.objects.Block;
import com.dragon.ide.objects.ComplexBlock;
import com.dragon.ide.objects.DoubleComplexBlock;
import com.dragon.ide.ui.activities.EventEditorActivity;
import com.dragon.ide.utils.BlockContentLoader;
import com.dragon.ide.utils.DropTargetUtils;

public class BlockDefaultView extends LinearLayout {
  public String returns;
  public Block block;
  public boolean enableEdit = false;
  public String language;
  public Activity activity;

  public BlockDefaultView(Activity context) {
    super(context);
    setOrientation(LinearLayout.HORIZONTAL);
    this.activity = context;
  }

  public void setBlock(Block mBlock) {
    try {
      this.block = mBlock.clone();
    } catch (CloneNotSupportedException e) {
      this.block = new Block();
    }

    returns = new String(block.getReturns());

    if (!(block instanceof DoubleComplexBlock) && !(block instanceof ComplexBlock)) {
      if (block instanceof Block) {
        if (block.getBlockType() == Block.BlockType.defaultBlock) {
          setBackgroundResource(R.drawable.block_default);

          Drawable backgroundDrawable = getBackground();
          backgroundDrawable.setTint(Color.parseColor(block.getColor()));
          backgroundDrawable.setTintMode(PorterDuff.Mode.SRC_IN);
          setBackground(backgroundDrawable);
        } else if (block.getBlockType() == Block.BlockType.returnWithTypeBoolean) {
          setBackgroundResource(R.drawable.block_boolean);

          Drawable backgroundDrawable = getBackground();
          backgroundDrawable.setTint(Color.parseColor(block.getColor()));
          backgroundDrawable.setTintMode(PorterDuff.Mode.SRC_IN);
          setBackground(backgroundDrawable);
        }
      }
    }
    setGravity(Gravity.CENTER_VERTICAL);
    BlockContentLoader.loadBlockContent(
        block.getBlockContent(), this, block.getColor(), getLanguage(), activity, getEnableEdit());
    if (activity instanceof EventEditorActivity) {
      setOnLongClickListener(
          (view) -> {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(this);

            DropTargetUtils.addDragTarget(
                ((EventEditorActivity) activity).binding.relativeBlockListEditorArea,
                (EventEditorActivity) activity,
                block.getReturns(),
                block.getBlockType());

            if (Build.VERSION.SDK_INT >= 24) {
              startDragAndDrop(data, shadow, this, 1);
            } else {
              startDrag(data, shadow, this, 1);
            }
            return false;
          });
    }
    invalidate();
  }

  public String getReturns() {
    if (returns != null) {
      return this.returns;
    }
    return new String("");
  }

  public Block getBlock() {
    return block;
  }

  public boolean getEnableEdit() {
    return this.enableEdit;
  }

  public void setEnableEdit(boolean enableEdit) {
    this.enableEdit = enableEdit;
  }

  public String getLanguage() {
    return this.language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent arg0) {
    return !getEnableEdit();
  }
}
