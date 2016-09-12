package fzi.mottem.runtime.interfaces;


/**
 * A custom Interface corresponding to the java.util.Observable class;
 * It is being implemented by observable views which -have- to extend
 * the Composite class and therefore can not extend any other classes.
 */

public interface IObservableView<T> {
	
	/*void addObserver(T o);
	
	void deleteObserver(T o);
	
	void deleteObservers();
	
	void notifyObservers();
	*/
	
	boolean hasChanged();
	
	void clearChanged();
	
	void setChanged();
	
	
	
}
