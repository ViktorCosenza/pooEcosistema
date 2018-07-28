import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;


public class Param extends JPanel implements ActionListener{

    Param() {
    }

    void init () {
	setLayout(new GridLayout(m.size()+1, 2));
    }

    private Map<String, JTextField> m = new HashMap<String, JTextField> ();
    // The values for all parameters are stored in the map m
    
    public void add (String param, String description, String _default) {
	JLabel label = new JLabel(description);
	JTextField jtf = new JTextField(_default);

	add(label);
	add(jtf);

	m.put(param, jtf);

	setLayout(new GridLayout(m.size()+1, 2, 10, 0));
    }

    public void actionPerformed(ActionEvent e) {
	for (Map.Entry<String, JTextField> entry : m.entrySet()) {
	    System.out.print(entry.getKey());
	    System.out.print(" : ");
	    System.out.print(entry.getValue().getText());
	    System.out.println();
	}
	int width = Integer.parseInt(m.get("Largura").getText());
	int height = Integer.parseInt(m.get("Altura").getText());
    int plants = Integer.parseInt(m.get("Plantas").getText());
    int sheeps = Integer.parseInt(m.get("Ovelhas").getText());
    int wolves = Integer.parseInt(m.get("Lobos").getText()); 
    int fences = Integer.parseInt(m.get("Cercas").getText());
	new Pasture(width, height, plants, sheeps, wolves, fences);
    }
    

    public static void main (String [] arg) {
	JFrame f = new JFrame();
	Param p = new Param();
	p.add("Largura", "Largura do pasto", "15");
	p.add("Altura", "Altura do pasto", "15");
	p.add("Ovelhas", "Quantidade de ovelhas", "10");
	p.add("Lobos", "Quantidade de lobos", "10");
	p.add("Plantas", "Quantidade de plantas", "10");
	p.add("Cercas", "Quantidade de cercas", "10");

	JButton klar = new JButton("Começar!");

	p.add(klar);
	klar.addActionListener(p);

	f.add(p);
	f.pack();

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	f.setLocation(screenSize.width/2 - f.getWidth()/2,
		      screenSize.height/2 - f.getHeight()/2);


	f.setVisible(true);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}