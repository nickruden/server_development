package Lab2.car;

public abstract class Car {
    private String brand;
    private String model;
    private String color;
    private int fuelConsumption;
    private int maxSpeed;
    private int price;

    public Car(String brand, String model,  String color,  int fuelConsumption, int maxSpeed, int price) {
        this.model = model;
        this.brand = brand;
        this.color = color;
        this.fuelConsumption = fuelConsumption;
        this.maxSpeed = maxSpeed;
        this.price = price;
    }

    public void setbrand(String brand) {
        this.brand = brand;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public String getbrand() {
        return brand;
    }
    public String gettModel() {
        return model;
    }
    public String getColor() {
        return color;
    }
    public int getFuelConsumption() {
        return fuelConsumption;
    }
    public int getMaxSpeed() {
        return maxSpeed;
    }
    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return brand + 
        ", " + model + 
        ", " + color + 
        ", " + fuelConsumption + " л/100км" + 
        ", макс. скорость - " + maxSpeed + "км/ч" + 
        ", " + price;
    }
}
