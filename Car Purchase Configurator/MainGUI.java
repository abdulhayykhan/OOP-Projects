import java.awt.*;
import java.awt.event.*;

class Car 
    String model;
    long price;
    int horsepower;
    String fuelType;

    public void showDetails(TextArea output) {
        output.setText("Model: " + model + "\nBase Price: " + price + " PKR\nHorsepower: " + horsepower
                + " HP\nFuel Type: " + fuelType + "\n");
    }
}

class EClass extends Car {
    public EClass(String type) {
        model = "E-Class";
        price = type.equalsIgnoreCase("New") ? 25000000 : 15000000;
        horsepower = 300;
        fuelType = "Petrol";
    }
}

class SClass extends Car {
    public SClass(String type) {
        model = "S-Class";
        price = type.equalsIgnoreCase("New") ? 45000000 : 25000000;
        horsepower = 400;
        fuelType = "Petrol";
    }
}

class GWagon extends Car {
    public GWagon(String type) {
        model = "G-Wagon";
        price = type.equalsIgnoreCase("New") ? 80000000 : 50000000;
        horsepower = 500;
        fuelType = "Diesel";
    }
}

class CLA extends Car {
    public CLA(String type) {
        model = "CLA";
        price = type.equalsIgnoreCase("New") ? 18000000 : 12000000;
        horsepower = 250;
        fuelType = "Petrol";
    }
}

class AMGGT extends Car {
    public AMGGT(String type) {
        model = "AMG GT";
        price = type.equalsIgnoreCase("New") ? 65000000 : 45000000;
        horsepower = 550;
        fuelType = "Petrol";
    }
}

class Modification {
    long tirePrice = 0, paintPrice = 0, exhaustPrice = 0, sunroofPrice = 0, spoilerPrice = 0;

    public void setTire(String tire) {
        if (tire.equals("Sport Tire"))
            tirePrice = 500000;
        else if (tire.equals("Off-Road Tire"))
            tirePrice = 700000;
        else
            tirePrice = 0;
    }

    public void setPaint(String paint) {
        if (paint.equals("Matte"))
            paintPrice = 1000000;
        else if (paint.equals("Glossy"))
            paintPrice = 500000;
        else
            paintPrice = 0;
    }

    public void setExhaust(String exhaust) {
        if (exhaust.equals("Sport"))
            exhaustPrice = 800000;
        else
            exhaustPrice = 0;
    }

    public void setSunroof(boolean addSunroof) {
        sunroofPrice = addSunroof ? 300000 : 0;
    }

    public void setSpoiler(boolean addSpoiler) {
        spoilerPrice = addSpoiler ? 200000 : 0;
    }

    public long getTotal() {
        return tirePrice + paintPrice + exhaustPrice + sunroofPrice + spoilerPrice;
    }
}

class CarSelectorGUI extends Frame implements ActionListener {
    Choice carChoice, typeChoice, tireChoice, paintChoice, exhaustChoice;
    Checkbox sunroofBox, spoilerBox;
    Button showBtn;
    TextArea output;

    public CarSelectorGUI() {
        setLayout(new FlowLayout());
        setTitle("Car Selector");
        setSize(400, 600);

        carChoice = new Choice();
        carChoice.add("E-Class");
        carChoice.add("S-Class");
        carChoice.add("G-Wagon");
        carChoice.add("CLA");
        carChoice.add("AMG GT");

        typeChoice = new Choice();
        typeChoice.add("New");
        typeChoice.add("Old");

        tireChoice = new Choice();
        tireChoice.add("None");
        tireChoice.add("Sport Tire");
        tireChoice.add("Off-Road Tire");

        paintChoice = new Choice();
        paintChoice.add("None");
        paintChoice.add("Matte");
        paintChoice.add("Glossy");

        exhaustChoice = new Choice();
        exhaustChoice.add("None");
        exhaustChoice.add("Sport");

        sunroofBox = new Checkbox("Add Sunroof");
        spoilerBox = new Checkbox("Add Spoiler");

        showBtn = new Button("Show Details");
        showBtn.addActionListener(this);

        output = new TextArea(15, 50);

        add(new Label("Select Car: "));
        add(carChoice);
        add(new Label("Condition: "));
        add(typeChoice);
        add(new Label("Tires: "));
        add(tireChoice);
        add(new Label("Paint: "));
        add(paintChoice);
        add(new Label("Exhaust: "));
        add(exhaustChoice);
        add(sunroofBox);
        add(spoilerBox);
        add(showBtn);
        add(output);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String car = carChoice.getSelectedItem();
        String type = typeChoice.getSelectedItem();

        Car selectedCar = switch (car) {
            case "E-Class" -> new EClass(type);
            case "S-Class" -> new SClass(type);
            case "G-Wagon" -> new GWagon(type);
            case "CLA" -> new CLA(type);
            case "AMG GT" -> new AMGGT(type);
            default -> null;
        };

        Modification mod = new Modification();
        mod.setTire(tireChoice.getSelectedItem());
        mod.setPaint(paintChoice.getSelectedItem());
        mod.setExhaust(exhaustChoice.getSelectedItem());
        mod.setSunroof(sunroofBox.getState());
        mod.setSpoiler(spoilerBox.getState());

        assert selectedCar != null;
        selectedCar.showDetails(output);
        output.append("Tire: " + tireChoice.getSelectedItem() + "\n");
        output.append("Paint: " + paintChoice.getSelectedItem() + "\n");
        output.append("Exhaust: " + exhaustChoice.getSelectedItem() + "\n");
        if (sunroofBox.getState())
            output.append("Sunroof: Yes\n");
        if (spoilerBox.getState())
            output.append("Spoiler: Yes\n");
        output.append("Modification Cost: " + mod.getTotal() + " PKR\n");
        output.append("Total Price: " + (selectedCar.price + mod.getTotal()) + " PKR\n");
    }
}

class MainGUI {
    public static void main(String[] args) {
        new CarSelectorGUI();
    }
}
