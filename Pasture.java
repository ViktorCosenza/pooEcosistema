import java.util.*;
import java.awt.Point;


/**
 * A pasture contains sheep, wolves, fences, plants, and possibly
 * other entities. These entities move around in the pasture and try
 * to find food, other entities of the same kind and run away from
 * possible enimies. 
 */
public class Pasture {

    private int         width = 50;
    private int         height = 50;

    private int         dummys = 0;
    private int         wolves;
    private int         sheep = 10;
    private int         plants = 64;
    private int         fences = 10;

    private Set<Entity> world = 
        new HashSet<Entity>();
    private Map<Point, List<Entity>> grid = 
        new HashMap<Point, List<Entity>>();
    private Map<Entity, Point> point 
        = new HashMap<Entity, Point>();

    private PastureGUI gui;

    /** 
     * Creates a new instance of this class and places the entities in
     * it on random positions.
     */
    public Pasture() {
        Engine engine = new Engine(this);
        gui = new PastureGUI(width, height, engine);
        drawFence();
    	spawnEntities();
        refresh();
    }
    
    public Pasture(int height, int width, int plants, int sheeps, int wolves, int fences) {
    	Engine engine = new Engine(this);
    	this.height = height;
    	this.width = width;
    	this.plants = plants;
    	this.sheep = sheeps;
    	this.wolves = wolves;
    	this.fences = fences;
    	gui = new PastureGUI(width, height, engine);
    	drawFence();
    	spawnEntities();
    	refresh();
    }
    
    /* The pasture is surrounded by a fence. Replace Dummy for
     * Fence when you have created that class */
    public void drawFence() {
        for (int i = 0; i < width; i++) {
            addEntity(new Fence(this), new Point(i,0));
            addEntity(new Fence(this), new Point(i, height - 1));
        }
        for (int i = 1; i < height-1; i++) {
            addEntity(new Fence(this), new Point(0,i));
            addEntity(new Fence(this), new Point(width - 1,i));
        }
    }
    
    public void spawnEntities() {
        /* 
         * Now insert the right number of different entities in the
         * pasture.
         */
        
        for (int i = 0; i < dummys; i++) {
        	Entity dummy = new Dummy(this, true);
        	addEntity(dummy, getFreePosition(dummy));
        }
        
        for (int i = 0; i < plants; i++) {
        	Entity plant = new Plant(this);
        	addEntity(plant, getFreePosition(plant));
        }
        
        for (int i = 0; i < sheep; i++) {
        	Entity sheep = new Sheep(this);
        	addEntity(sheep, getFreePosition(sheep));
        }
        
        for (int i = 0; i < fences; i++) {
        	Entity fence = new Fence(this);
        	addEntity(fence, getFreePosition(fence));
        }
    }

    public void refresh() {
        gui.update();
    }

    /**
     * Returns a random free position in the pasture if there exists
     * one.
     * 
     * If the first random position turns out to be occupied, the rest
     * of the board is searched to find a free position. 
     */
    private Point getFreePosition(Entity toPlace) 
        throws MissingResourceException {
        Point position = new Point((int)(Math.random() * width),
                                   (int)(Math.random() * height)); 
        int startX = (int)position.getX();
        int startY = (int)position.getY();

        int p = startX+startY*width;
        int m = height * width;
        int q = 97; //any large prime will do

        for (int i = 0; i<m; i++) {
            int j = (p+i*q) % m;
            int x = j % width;
            int y = j / width;

            position = new Point(x,y);
            boolean free = true;

            Collection <Entity> c = getEntitiesAt(position);
            if (c != null) {
                for (Entity thisThing : c) {
                    if(!toPlace.isCompatible(thisThing)) { 
                        free = false; break; 
                    }
                }
            }
            if (free) return position;
        }
        throw new MissingResourceException(
                  "There is no free space"+" left in the pasture",
                  "Pasture", "");
    }
    
            
    public Point getPosition (Entity e) {
        return point.get(e);
    }

    /**
     * Add a new entity to the pasture.
     */
    public void addEntity(Entity entity, Point pos) {

        world.add(entity);

        List<Entity> l = grid.get(pos);
        if (l == null) {
            l = new  ArrayList<Entity>();
            grid.put(pos, l);
        }
        l.add(entity);

        point.put(entity,pos);

        gui.addEntity(entity, pos);
    }
    
    public void moveEntity(Entity e, Point newPos) {
        
        Point oldPos = point.get(e);
        List<Entity> l = grid.get(oldPos);
        if (!l.remove(e)) 
            throw new IllegalStateException("Inconsistent stat in Pasture");
        /* We expect the entity to be at its old position, before we
           move, right? */
                
        l = grid.get(newPos);
        if (l == null) {
            l = new  ArrayList<Entity>();
            grid.put(newPos, l);
        }
        l.add(e);

        point.put(e, newPos);

        gui.moveEntity(e, oldPos, newPos);
    }

    /**
     * Remove the specified entity from this pasture.
     */
    public void removeEntity(Entity entity) { 

        Point p = point.get(entity);
        world.remove(entity); 
        grid.get(p).remove(entity);
        point.remove(entity);
        gui.removeEntity(entity, p);

    }

    /**
     * Various methods for getting information about the pasture
     */

    public List<Entity> getEntities() {
        return new ArrayList<Entity>(world);
    }
        
    public Collection<Entity> getEntitiesAt(Point lookAt) {

        Collection<Entity> l = grid.get(lookAt);
        
        if (l==null) {
            return null;
        }
        else {
            return new ArrayList<Entity>(l);
        }
    }
    
    public Collection<Entity> getNeighbours(Entity entity){
    	Set<Entity> neighbours = new HashSet<Entity>();
        Point p;
        Collection<Entity> entities;
        
        if(entity == null)
        	return null;
        int entityX = (int)getEntityPosition(entity).getX();
        int entityY = (int)getEntityPosition(entity).getY();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
            	p = new Point(entityX + x,
            				  entityY + y);
            	entities = getEntitiesAt(p); 
            	if(entities != null)
            		neighbours.addAll(entities);
            }
        }
        return new ArrayList<Entity>(neighbours);
    }


    public Collection<Point> getFreeNeighbours(Entity entity) {
        Set<Point> free = new HashSet<Point>();
        Point p;
        if(entity == null)
        	return null;
        
        int entityX = (int)getEntityPosition(entity).getX();
        int entityY = (int)getEntityPosition(entity).getY();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
            p = new Point(entityX + x,
                          entityY + y);
            if (freeSpace(p, entity))
                free.add(p);
            }
        }
        return free;
    }

    private boolean freeSpace(Point p, Entity e) {
                              
        List <Entity> l = grid.get(p);
        if ( l == null  ) return true;
        for (Entity old : l ) 
            if (! old.isCompatible(e)) return false;
        return true;
    }

    public Point getEntityPosition(Entity entity) {
    	if(entity == null)
    		return null;
        return point.get(entity);
    }


    /** The method for the JVM to run. */
    public static void main(String[] args) {

        new Pasture();
    }


}


