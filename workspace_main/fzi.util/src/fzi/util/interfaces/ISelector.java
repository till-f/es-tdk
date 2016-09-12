package fzi.util.interfaces;

public interface ISelector<E>
{

	/*
	 * Returns true if provided item shall be selected,
	 * otherwise false.
	 */
	public boolean toSelect(E item);

}
