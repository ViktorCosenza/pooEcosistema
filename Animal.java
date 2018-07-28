import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Entity {
	
	protected final int hungerDelay;
	
	protected final int moveDelay;
	
	protected final int reproduceDelay;
	
	protected int currentHungerDelay;
	
	protected int currentMoveDelay;
	
	protected int currentReproduceDelay;
	
	public Animal(Pasture pasture, ImageIcon image) {
		super(pasture, image);
		this.hungerDelay = 300 + ThreadLocalRandom.current().nextInt(100);
		this.moveDelay = 50 + ThreadLocalRandom.current().nextInt(50);
		this.reproduceDelay = 300 + ThreadLocalRandom.current().nextInt(200);
		this.currentMoveDelay = 1;
		this.currentReproduceDelay = reproduceDelay;	
		this.currentHungerDelay = hungerDelay;
	}
	
	public abstract void reproduce();
	
	@Override
	public void tick() {
		super.tick();
		if(!alive)
			return;
		
		currentMoveDelay--;
		currentReproduceDelay--;
		currentHungerDelay--;
		
		//Move entidade
		if(currentMoveDelay == 0) {
			Point neighbour = 
					(Point)getRandomMember
					(pasture.getFreeNeighbours(this));
			
	        if(neighbour != null) 
	            pasture.moveEntity(this, neighbour);
	            currentMoveDelay = moveDelay;
		}
		
		//Reproduz se está fertil e não está com fome
		if(currentReproduceDelay == 0 && currentHungerDelay > 50) {
			reproduce();
		}
		
		//Procura comida nos arredores
		if(currentHungerDelay <= 100)
			eat();
		
		//Se fome < 0 ,animal morre de fome;
		if(currentHungerDelay < 0) {
			System.out.println("An animal died from starvation");
			alive = false;
		}
	}
	
	public abstract void eat();
	
	public boolean isFertile() {
		if(currentReproduceDelay <= 0)
			return true;
		return false;
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
