package cn.bluemobi.dylan.fastdev.adapter.common.recyclerview;

import android.view.View;
import android.view.ViewGroup;

public interface OnItemLongClickClickListener<T>
{
    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}