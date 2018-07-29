import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Collection;

public class Wolf extends Animal {
	
	public Wolf(Pasture pasture){
		super(pasture, new ImageIcon("wolf.gif"));
	}
	
	public void resetReproduceDelay() { this.currentReproduceDelay = this.reproduceDelay; }
	
	@Override
	public void tick() {
		super.tick();
		if(!alive)
			return;
		if(currentReproduceDelay == 0) {
			reproduce();
		}
	}
	
	public boolean isCompatible(Entity otherEntity) { 
		if(otherEntity instanceof Fence || otherEntity instanceof Animal) {
			return false;
		}
		
		return true;
	}
	
	public ArrayList<Wolf> getPartners() {
		Collection<Entity> l = pasture.getNeighbours(this);
		ArrayList<Wolf> partners = new ArrayList<>();
		for(Entity entity : l) {
			if((entity instanceof Wolf)) {
				partners.add((Wolf)entity);
			}
		}
		return partners;
	}
	
	public void eat() {
		Collection<Entity> neighbours = pasture.getNeighbours(this);
		if(neighbours.isEmpty())
			return;
		for(Entity e : neighbours) {
			if(e instanceof Sheep) {
				//Come a ovelha
				System.out.println("Sheep Eaten by Wolf");
				e.kill();
				this.currentHungerDelay += 400;
				return;
			}
		}
	}
	
	public void reproduce() {
		ArrayList<Wolf> partners = getPartners();
		for(Wolf w : partners) {
			if(w.isFertile()) {
				w.giveBirth();
				resetReproduceDelay();
			}
		}
	}
	
	public void giveBirth() {
		Point neighbour = 
				(Point)getRandomMember
				(pasture.getFreeNeighbours(this));
		
        if(neighbour != null) {
        	System.out.println("Wolf was born.");
            pasture.addEntity(new Wolf(pasture), neighbour);
            resetReproduceDelay();
            this.resetReproduceDelay();
        }
	}
}