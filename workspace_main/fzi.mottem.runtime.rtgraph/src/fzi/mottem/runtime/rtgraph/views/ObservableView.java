package fzi.mottem.runtime.rtgraph.views;

import org.eclipse.swt.widgets.Composite;

import fzi.mottem.runtime.interfaces.IObservableView;
import fzi.mottem.runtime.interfaces.IRepresentable;

public abstract class ObservableView<R> extends Composite implements IObservableView, IRepresentable<R> {
	
	public ObservableView(Composite parent, int style) {
		super(parent, style);
	}
	
	//ArrayList<T> observers = new ArrayList<T>();
	
	boolean hasChanged;

	@Override
	public boolean hasChanged() {
		return hasChanged;
	}

	@Override
	public void clearChanged() {
		hasChanged = false;
	}

	@Override
	public void setChanged() {
		hasChanged = true;
		//notifyObservers();
	}
/*
	@Override
	public void addObserver(T o) {
		observers.add(o);
	}

	@Override
	public void deleteObserver(T o) {
		observers.remove(o);	
	}

	@Override
	public void deleteObservers() {
		observers.removeAll(observers);
	}*/

}
