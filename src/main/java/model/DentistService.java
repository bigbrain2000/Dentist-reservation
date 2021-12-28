package model;

import org.dizitart.no2.objects.Id;

import java.util.Objects;

public class DentistService {
    @Id
    private String name;

    private float price;

    public DentistService(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public DentistService() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DentistService that = (DentistService) o;
        return Float.compare(that.price, price) == 0 && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "DentistService{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
