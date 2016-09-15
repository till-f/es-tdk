package fzi.mottem.runtime.rtgraph.views;

import org.eclipse.swt.widgets.Composite;

import fzi.mottem.runtime.interfaces.IObservableView;
import fzi.mottem.runtime.interfaces.IRepresentable;

public abstract class ObservableView<R> extends Composite implements IObservableView, IRepresentable<R> {
	
	public ObservableView(Composite parent, int style) {
		super(parent, style);
	}
	
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

}
