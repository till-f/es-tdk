package fzi.util;

import java.util.LinkedList;

import fzi.util.interfaces.IQueryableList;
import fzi.util.interfaces.ISelector;

public class QList<E> extends LinkedList<E> implements IQueryableList<E>
{

	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = -541476573359343036L;

	@Override
	public IQueryableList<E> where(ISelector<E> selector)
	{
		QList<E> subList = new QList<E>();
		
		for (E item : this)
		{
			if (selector.toSelect(item))
			{
				subList.add(item);
			}
		}

		return subList;
	}

	@Override
	public E find(ISelector<E> selector)
	{
		for (E item : this)
		{
			if (selector.toSelect(item))
			{
				return item;
			}
		}

		return null;
	}

	@Override
	public E find(ISelector<E> selector, int startIndex)
	{
		for (int idx = startIndex; idx < this.size(); idx++)
		{
			E item = this.get(idx);
			
			if (selector.toSelect(item))
			{
				return item;
			}
		}

		return null;
	}

}
