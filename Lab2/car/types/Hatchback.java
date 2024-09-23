package Lab2.car.types;

import Lab2.car.Car;

public class Hatchback extends Car {
    public Hatchback(String model,  String type,  String color,  int fuelConsumption, int maxSpeed, int price) {
        super(model, type, color, fuelConsumption, maxSpeed, price);
    }

    @Override
    public String toString() {
        return "HATCHBACK: " + super.toString();
    }
}
