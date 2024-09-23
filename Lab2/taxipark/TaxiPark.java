package Lab2.taxipark;

import java.util.ArrayList;
import java.util.List;

import Lab2.car.Car;

public class TaxiPark {
    private List<Car> cars;

    public TaxiPark() {
        this.cars = new ArrayList<>();
    }

    public void addCar(Car obj) {
        cars.add(obj);
    }

     public List<Car> getCars() {
        return cars;
    }

    public void sortByFuelConsumption() {
        int n = cars.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (cars.get(j).getFuelConsumption() > cars.get(j + 1).getFuelConsumption()) {
                    Car temp = cars.get(j);
                    cars.set(j, cars.get(j + 1));
                    cars.set(j + 1, temp);
                }
            }
        }
    }

    public List<Car> findCarsBySpeedRange(int minSpeed, int maxSpeed) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars) {
            if (car.getMaxSpeed() >= minSpeed && car.getMaxSpeed() <= maxSpeed) {
                result.add(car);
            }
        }
        return result;
    }

    public void displayCars(List<Car> cars) {
        for (Car car : cars) {
            System.out.println(car);
        }
    }
    
    public void displayAllCars() {
        displayCars(cars);
    }
}
