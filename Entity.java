import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * This is the superclass of all entities in the pasture simulation
 * system. This interface <b>must</b> be implemented by all entities
 * that exist in the simulation of the pasture.
 */
public abstract class Entity {
	boolean alive;
	
    public Entity() {
    	this.alive = true;
    }
	
	public abstract void tick();

    /** 
     * ImageIcon returns the icon of this entity, to be displayed by
     * the pasture gui.
     */
    public abstract ImageIcon getImage();
    
    public abstract boolean isCompatible(Entity otherEntity);
    
    public boolean isAlive() {
    	return this.alive;
    }

}
