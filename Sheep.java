import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Collection;

public class Sheep extends Animal {
	
	public Sheep(Pasture pasture){
		super(pasture, new ImageIcon("sheep.gif"));
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
	
	public ArrayList<Sheep> getPartners() {
		Collection<Entity> l = pasture.getNeighbours(this);
		ArrayList<Sheep> partners = new ArrayList<>();
		for(Entity entity : l) {
			if((entity instanceof Sheep)) {
				partners.add((Sheep)entity);
			}
		}
		return partners;
	}
	
	public void eat() {
		Collection<Entity> neighbours = pasture.getNeighbours(this);
		if(neighbours.isEmpty())
			return;
		for(Entity e : neighbours) {
			if(e instanceof Plant) {
				//Come a planta
				System.out.println("Grass Eaten by Sheep");
				e.kill();
				this.currentHungerDelay += 400;
				return;
			}
		}
	}
	
	public void reproduce() {
		ArrayList<Sheep> partners = getPartners();
		for(Sheep s : partners) {
			if(s.isFertile()) {
				s.giveBirth();
				resetReproduceDelay();
			}
		}
	}
	
	public void giveBirth() {
		Point neighbour = 
				(Point)getRandomMember
				(pasture.getFreeNeighbours(this));
		
        if(neighbour != null) {
        	System.out.println("Sheep was born.");
            pasture.addEntity(new Sheep(pasture), neighbour);
            resetReproduceDelay();
            this.resetReproduceDelay();
        }
	}
}
