package fzi.util.interfaces;

import java.util.List;

/*
 * A list that supports query-like subList creation
 */
public interface IQueryableList<E> extends List<E>
{
	/*
	 * Returns a queryable list containing only those elements of this
	 * list that are selected by Selector
	 */
	public IQueryableList<E> where(ISelector<E> selector);

	/*
	 * Returns the first element of this list that is selected by Selector
	 */
	public E find(ISelector<E> selector);

	/*
	 * Returns the first element starting at the provided index of this list
	 * that is selected by Selector
	 */
	public E find(ISelector<E> selector, int startIndex);
}
