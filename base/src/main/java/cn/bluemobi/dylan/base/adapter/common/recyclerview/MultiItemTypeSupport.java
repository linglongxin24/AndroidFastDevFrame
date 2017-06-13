package cn.bluemobi.dylan.base.adapter.common.recyclerview;

public interface MultiItemTypeSupport<T>
{
	int getLayoutId(int itemType);

	int getItemViewType(int position, T t);
}