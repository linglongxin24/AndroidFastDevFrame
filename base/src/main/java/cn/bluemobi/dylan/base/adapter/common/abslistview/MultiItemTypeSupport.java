package cn.bluemobi.dylan.base.adapter.common.abslistview;

public interface MultiItemTypeSupport<T>
{
	int getLayoutId(int position, T t);

	int getViewTypeCount();

	int getItemViewType(int position, T t);
}