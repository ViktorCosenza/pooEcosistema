import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Collection;

public class Sheep extends Animal {
	
	private final ImageIcon image = new ImageIcon("sheep.gif");
	
	
	public Sheep(Pasture pasture){
		super(pasture);
	}
	
	public ImageIcon getImage() { return image; }
	
	public void resetReproduceDelay() { this.currentReproduceDelay = this.reproduceDelay; }
	
	public void tick() {
		super.tick();
		
		if(currentReproduceDelay == 0) {
			reproduce();
		}
	}
	
	public boolean isCompatible(Entity otherEntity) { 
		if(otherEntity instanceof Fence)
			return false;
		return true;
	}
	
	public ArrayList<Sheep> getPartners() {
		Collection<Entity> l = pasture.getEntitiesAt(pasture.getPosition(this));
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
				System.out.println("Grass Eaten");
				e.kill();
				this.currentHungerDelay = this.hungerDelay;
				return;
			}
		}
	}
	
	public void reproduce() {
		ArrayList<Sheep> partners = getPartners();
		for(Sheep s : partners) {
			if(s.isFertile()) {
				//Reproduzem
				Point neighbour = 
						(Point)getRandomMember
						(pasture.getFreeNeighbours(this));
				
		        if(neighbour != null) {
		        	System.out.println("New sheep added");
		            pasture.addEntity(new Sheep(pasture), neighbour);
		            s.resetReproduceDelay();
		            this.resetReproduceDelay();
		        }
			}
		}
	}
}
