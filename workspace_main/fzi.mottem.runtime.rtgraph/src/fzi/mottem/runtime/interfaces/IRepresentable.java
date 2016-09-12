package fzi.mottem.runtime.interfaces;

public interface IRepresentable<T> {
	
	public T getRepresentation();
	public void setRepresentation(T representation);
	public void applyRepresentation();
	public void updateRepresentation();
	
}
