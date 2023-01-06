package kg.attractor.java.homework.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.*;
import static kg.attractor.java.Main.getInput;

public class Order {
    // Этот блок кода менять нельзя! НАЧАЛО!
    private final Customer customer;
    private final List<Item> items;
    private final boolean homeDelivery;
    private transient double total = 0.0d;

    public Order(Customer customer, List<Item> orderedItems, boolean homeDelivery) {
        this.customer = customer;
        this.items = List.copyOf(orderedItems);
        this.homeDelivery = homeDelivery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return homeDelivery == order.homeDelivery &&
                Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, homeDelivery);
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isHomeDelivery() {
        return homeDelivery;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotal() {
        return total;
    }
    // Этот блок кода менять нельзя! КОНЕЦ!

    //----------------------------------------------------------------------
    //------   Реализация ваших методов должна быть ниже этой линии   ------
    //----------------------------------------------------------------------

    public void calculateTotal() {
        total = items.stream()
                .mapToDouble(e -> e.getPrice() * e.getAmount())
                .sum();
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void displayListOfOrders() {
        System.out.println("+---------------------------+------------+-----------------+------------+");
        System.out.println("|            Name           |    Price   |       Type      |   Amount   |");
        System.out.println("+---------------------------+------------+-----------------+------------+");
        items.forEach(Item::printItem);
    }


    public void displayTotalPriceOfAllOrders() {
        displayListOfOrders();
        System.out.println("+---------------------------+------------+-----------------+------------+");
        System.out.printf("| Home Delivery             | %-10s | Total           | %-10.2f |%n", homeDelivery, total);
        System.out.println("+---------------------------+------------+-----------------+------------+");
    }

    public void displayOrderCustomer() {
        customer.printCustomer();
        displayTotalPriceOfAllOrders();
    }

    @Override
    public String toString() {
        return String.format("%nCustomer: %s%nOrder: %s%nHome delivery: %s%nTotal: %.2f%n",
                customer,
                items,
                homeDelivery,
                total);
    }

    public static List<Order> displayListMinOrders(List<Order> orders) {
        int num = getInput(orders);
        return orders.stream()
                .sorted(comparingDouble(Order::getTotal))
                .limit(num)
                .collect(toList());
    }

    public static List<Order> displayListMaxOrders(List<Order> orders) {
        int num = getInput(orders);
        return orders.stream()
                .sorted(comparingDouble(Order::getTotal))
                .skip(orders.size() - num)
                .collect(toList());
    }

    public static List<Order> displayAllListOrdersToHomeDelivery(List<Order> orders) {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .collect(toList());
    }

    public static Order chooseMaxOrderTotal(List<Order> orders) {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .max(comparingDouble(Order::getTotal)).get();
    }

    public static Order chooseMinOrderTotal(List<Order> orders) {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .min(comparingDouble(Order::getTotal)).get();
    }


    public static List<Order> getOrdersFromTotalRange(List<Order> orders) {
        double maxOrderTotal = 100.0;
        double minOrderTotal = 50.0;
        return orders.stream()
                .sorted(comparingDouble(Order::getTotal))
                .dropWhile(e -> e.getTotal() < minOrderTotal)
                .takeWhile(e -> e.getTotal() <= maxOrderTotal)
                .collect(toList());
    }

    public static double getTotalPriceOfAllOrders(List<Order> orders) {
        return orders.stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }

    public static TreeSet<String> getUniqueEmails(List<Order> orders) {
        return orders.stream()
                .map(e -> e.getCustomer().getEmail())
                .collect(toCollection(TreeSet::new));
    }

    public static Map<Customer, List<Order>> getUniqueCustomerOrders(List<Order> orders) {
        return orders.stream()
                .collect(groupingBy(Order::getCustomer));
    }

    public static Map<Customer, Double> getUniqueCustomersOrdersAndTotal(List<Order> orders) {
        return orders.stream()
                .collect(groupingBy(Order::getCustomer, summingDouble(Order::getTotal)));
    }

    public static Map<Customer, Double> getMaxOrdersTotalCustomer(List<Order> orders) {
        return orders.stream()
                .collect(groupingBy(Order::getCustomer, summingDouble(Order::getTotal)))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<Customer, Double> getMinOrdersTotalCustomer(List<Order> orders) {
        return orders.stream()
                .collect(groupingBy(Order::getCustomer, summingDouble(Order::getTotal)))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<Integer, List<String>> getSoldItems(List<Order> orders) {
        var soldItems = orders.stream()
                .flatMap(e -> e.getItems().stream())
                .collect(toList());
        return soldItems.stream()
                .collect(groupingBy(Item::getName, summingInt(Item::getAmount)))
                .entrySet()
                .stream()
                .collect(groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, toList())));
    }
}
