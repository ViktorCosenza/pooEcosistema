import javax.swing.*;

/**
 * This is the superclass of all entities in the pasture simulation
 * system. This interface <b>must</b> be implemented by all entities
 * that exist in the simulation of the pasture.
 */
public abstract class Entity {
	
	private final ImageIcon image;
	
	boolean alive;
    /** The position of this entity. */
    protected Pasture pasture;
    
    public Entity(Pasture pasture, ImageIcon image) {
    	this.pasture = pasture;
    	this.alive = true;
    	this.image = image;
    }
	
	public void tick() { 
		if(!alive) { 
			pasture.removeEntity(this); 
			return;
		}
	};

	/** 
     * Returns the icon of this entity, to be displayed by the pasture
     * gui. 
     * @see PastureGUI
     */
    public ImageIcon getImage(){ return image; };
    
    public abstract boolean isCompatible(Entity otherEntity);
    
    public boolean isAlive() {
    	return alive;
    }
    
    public void kill() { alive = false; }
}
