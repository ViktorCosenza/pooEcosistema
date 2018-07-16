import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;

public class Plant extends Entity {
    /** The number of ticks this entity should get before reproducing. */
    protected int reproduceDelay;
    
    protected int delayCount;
    
    protected int life;

    /**
     * Creates a new instance of this class, with the given pasture as
     * its pasture.
     * @param pasture the pasture this entity should belong to.
     */
    public Plant(Pasture pasture) {
    	super(pasture, new ImageIcon("plant.gif"));
        reproduceDelay = 100 + ThreadLocalRandom.current().nextInt(0, 100);
        delayCount = 200;
        life = 300 + ThreadLocalRandom.current().nextInt(0, 200);
    }
    
    public Plant(Pasture pasture, int delay) {
    	super(pasture, new ImageIcon("plant.gif"));
        reproduceDelay = delay;
        delayCount = delay;
        life = 25;
    }

    /**
     * Performs the relevant actions of this entity, depending on what
     * kind of entity it is.
     */
    
    @Override
    public void tick() {
    	super.tick();
    	if(!alive)
    		return;
        delayCount--;
        life--;
        
        if(life == 0) {
        	alive = false;
        }
        
        if(delayCount <= 0) {
        	reproduce();
        }
    }
    
    public void reproduce() {
    	Point neighbour = 
                (Point)getRandomMember
                (pasture.getFreeNeighbours(this));
            if(neighbour != null) { 
                pasture.addEntity(new Plant(pasture), neighbour);
            	delayCount = reproduceDelay;
            }
            else {
            	pasture.removeEntity(this);
            	System.out.println("A plant died from lack of space");
            }
    }

    /**
     * Tests if this entity can be on the same position in the pasture
     * as the given one.
     */
    public boolean isCompatible(Entity otherEntity) { 
    	if(otherEntity instanceof Fence || otherEntity instanceof Plant)
    		return false;
    	else
    		return true;
    }
    
    protected static <X> X getRandomMember(Collection<X> c) {
        if (c.size() == 0)
            return null;
        
        Iterator<X> it = c.iterator();
        int n = (int)(Math.random() * c.size());

        while (n-- > 0) {
            it.next();
        }

        return it.next();
    }
}
