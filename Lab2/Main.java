package Lab2;

import Lab2.car.types.Hatchback;
import Lab2.car.types.Minivan;
import Lab2.car.types.Sedan;

import java.util.List;

import Lab2.car.Car;
import Lab2.car.types.Cuv;
import Lab2.taxipark.TaxiPark;

public class Main {
    public static void main(String[] args) {
        TaxiPark taxiPark = new TaxiPark();

        taxiPark.addCar(new Sedan("Toyota", "Camry", "Черный", 7, 200, 25000));
        taxiPark.addCar(new Hatchback("Ford", "Escape", "Синий", 8, 180, 30000));
        taxiPark.addCar(new Cuv("Honda", "Civic", "Красный", 6, 190, 22000));
        taxiPark.addCar(new Minivan("Dodge", "Grand Caravan", "Серый", 7, 170, 28000));
        taxiPark.addCar(new Sedan("BMW", "3 Series", "Белый", 5, 230, 40000));


        System.out.println("Все машины таксопарка:");
        taxiPark.displayAllCars();

        taxiPark.sortByFuelConsumption();

        System.out.println("\nПосле сортировки по расходу топлива:");
        taxiPark.displayAllCars();

        System.out.println("\nМашины с максимальной скоростью от 180 до 200 км/ч:");
        List<Car> carsBySpeedRange = taxiPark.findCarsBySpeedRange(180, 200);
        taxiPark.displayCars(carsBySpeedRange);
    }
}
