package kg.attractor.java;

// import static java.util.stream.Collectors.*;
// import static java.util.Comparator.*;

// используя статические imports
// мы импортируем *всё* из Collectors и Comparator.
// теперь нам доступны такие операции как
// toList(), toSet() и прочие, без указания уточняющего слова Collectors. или Comparator.
// вот так было до импорта Collectors.toList(), теперь стало просто toList()


import kg.attractor.java.homework.RestaurantOrders;
import kg.attractor.java.homework.domain.Order;

import java.util.List;
import java.util.Scanner;

import static kg.attractor.java.homework.domain.Order.*;

public class Main {

    public static void main(String[] args) {

        // это для домашки
        // выберите любое количество заказов, какое вам нравится.

        var orders = RestaurantOrders.read("orders_100.json").getOrders();
        //var orders = RestaurantOrders.read("orders_1000.json").getOrders();
        //var orders = RestaurantOrders.read("orders_10_000.json").getOrders();

        // протестировать ваши методы вы можете как раз в этом файле (или в любом другом, в котором вам будет удобно)
        getAction(orders);

    }

    public static void printOrders(List<Order> orders) {
        orders.forEach(Order::displayOrderCustomer);
    }

    public static int getInput(List<Order> orders) {
        String input;
        while (true) {
            System.out.print("ENTER NUMBER OF ORDERS: ");
            try {
                input = new Scanner(System.in).nextLine();
                checkInput(input, orders);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return Integer.parseInt(input);
    }

    public static void checkInput(String str, List<Order> orders) throws Exception {
        int input = Integer.parseInt(str);
        if (input < 0 || input > orders.size())
            throw new Exception("INCORRECT CHOOSE");
    }

    public static int getChoice() {
        String input;
        printActions();
        while (true) {
            System.out.print("\nENTER THE NUMBER OF THE FOLLOWING ACTION: ");
            try {
                input = new Scanner(System.in).nextLine();
                return checkAction(input);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static int checkAction(String str) throws Exception {
        int input = Integer.parseInt(str);
        if (input < 0 || input > 15)
            throw new Exception("INCORRECT ACTION");
        return input;
    }

    public static void printActions() {
        System.out.println("\nChoose Action: " +
                "\n[ 1 ]  - Print orders" +
                "\n[ 2 ]  - Get max orders (Enter number of orders)" +
                "\n[ 3 ]  - Get min orders (Enter number of orders)" +
                "\n[ 4 ]  - Get home delivery orders" +
                "\n[ 5 ]  - Get max home delivery orders" +
                "\n[ 6 ]  - Get min home delivery orders" +
                "\n[ 7 ]  - Get orders from total range(50..100)" +
                "\n[ 8 ]  - Get orders total" +
                "\n[ 9 ]  - Get unique emails" +
                "\n[ 10 ] - Get unique customer orders" +
                "\n[ 11 ] - Get unique customer orders total" +
                "\n[ 12 ] - Get customer of max orders total" +
                "\n[ 13 ] - Get customer of min orders total" +
                "\n[ 14 ] - Get sold items amount");
    }

    public static void getAction(List<Order> orders) {
        while (true) {
            switch (getChoice()) {
                case 1:
                    printOrders(orders);
                case 2: {
                    var printMaxOrders = displayListMaxOrders(orders);
                    printMaxOrders.forEach(Order::displayOrderCustomer);
                }
                case 3: {
                    var printMinOrders = displayListMinOrders(orders);
                    printMinOrders.forEach(Order::displayOrderCustomer);
                }
                case 4: {
                    var printHomeDelivery = displayAllListOrdersToHomeDelivery(orders);
                    printHomeDelivery.forEach(Order::displayOrderCustomer);
                }
                case 5: {
                    var printMaxOrder = chooseMaxOrderTotal(orders);
                    printMaxOrder.displayOrderCustomer();
                }
                case 6: {
                    var printMinOrder = chooseMinOrderTotal(orders);
                    printMinOrder.displayOrderCustomer();
                }
                case 7: {
                    var printFromRange = getOrdersFromTotalRange(orders);
                    printFromRange.forEach(Order::displayOrderCustomer);
                }
                case 8: {
                    var ordersTotal = getTotalPriceOfAllOrders(orders);
                    System.out.printf("%nORDERS TOTAL: %.2f%n%n", ordersTotal);
                }
                case 9: {
                    var printEmails = getUniqueEmails(orders);
                    printEmails.forEach(System.out::println);
                }
                case 10: {
                    var printCustomerOrders = getUniqueCustomerOrders(orders);
                    printCustomerOrders.forEach((k, v) -> {
                        k.printCustomer();
                        v.forEach(Order::displayTotalPriceOfAllOrders);
                    });
                }
                case 11: {
                    var printCustomerOrdersTotal = getUniqueCustomersOrdersAndTotal(orders);
                    printCustomerOrdersTotal.forEach((k, v) -> {
                        k.printCustomer();
                        System.out.println("*-----------------------------------*-----------------------------------*");
                        System.out.printf("|               TOTAL               | %-33s |%n", v);
                        System.out.println("*-----------------------------------*-----------------------------------*");
                    });
                }
                case 12: {
                    var printMaxOrdersTotalCustomer = getMaxOrdersTotalCustomer(orders);
                    printMaxOrdersTotalCustomer.forEach((k, v) -> {
                        k.printCustomer();
                        System.out.println("*-----------------------------------*-----------------------------------*");
                        System.out.printf("|               TOTAL               | %-33s |%n", v);
                        System.out.println("*-----------------------------------*-----------------------------------*");
                    });
                }
                case 13: {
                    var printMinOrdersTotalCustomer = getMinOrdersTotalCustomer(orders);
                    printMinOrdersTotalCustomer.forEach((k, v) -> {
                        k.printCustomer();
                        System.out.println("*-----------------------------------*-----------------------------------*");
                        System.out.printf("|               TOTAL               | %-33s |%n", v);
                        System.out.println("*-----------------------------------*-----------------------------------*");
                    });
                }
                case 14: {
                    var soldItemsAmount = getSoldItems(orders);
                    System.out.println("*-------*---------------------------*");
                    soldItemsAmount.forEach((k, v) -> {
                        System.out.printf("| %-5s ", k);
                        for (int i = 0; i < v.size(); i++) {
                            System.out.printf("| %-25s |%n", v.get(i));
                            if (v.size() - 1 != i) System.out.print("|       ");
                        }
                        System.out.println("*-------*---------------------------*");
                    });
                }
            }
        }
    }
}
