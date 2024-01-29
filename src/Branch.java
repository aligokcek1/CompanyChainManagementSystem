import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Branch {
    public String id;
    String name;
    boolean isActive;
    EmployeeSet employees;
    Employee manager;
    LinkedList<Employee> cookPromoQueue;
    ArrayList<Employee> courierList;
    ArrayList<Employee> cookList;
    ArrayList<Employee> cashierList;
    int monthlyBonus;
    int totalBonus;
    ArrayList<Employee> courierToBeFired;
    ArrayList<Employee> cookToBeFired;
    ArrayList<Employee> cashierToBeFired;

    Branch(String id) {
        this.id = id;
        String strArray[] = id.split(",");
        this.name = strArray[1];
        this.isActive = isActive;
        this.courierList = new ArrayList<>();
        this.cookList = new ArrayList<>();
        this.cashierList = new ArrayList<>();
        this.employees = new EmployeeSet();
        this.isActive = true;
        this.monthlyBonus = 0;
        this.totalBonus = 0;
        this.cookPromoQueue = new LinkedList<>();
        this.cookToBeFired = new ArrayList<>();
        this.cashierToBeFired = new ArrayList<>();
        this.courierToBeFired = new ArrayList<>();
        this.manager = null;
    }

    public Employee getManager() {
        return manager;
    }

    public void addEmployee(String empName, String empType) {
        Employee employee = new Employee(empName, empType);
        employees.addEmployee(employee);
        switch (empType) {
            case "MANAGER":
                this.manager = employee;
                break;
            case "COOK":
                cookList.add(employee);
                break;
            case "COURIER":
                courierList.add(employee);
                break;
            case "CASHIER":
                cashierList.add(employee);
                break;
        }
        addCheck(empName);
    }

    public void initialAddEmployee(String empName, String empType) {
        Employee employee = new Employee(empName, empType);
        employees.addEmployee(employee);
        switch (empType) {
            case "MANAGER":
                this.manager = employee;
                break;
            case "COOK":
                cookList.add(employee);
                break;
            case "COURIER":
                courierList.add(employee);
                break;
            case "CASHIER":
                cashierList.add(employee);
                break;
        }
    }

    public void performanceUpdate(String empName, int score) {
        if (!employees.contains(empName)) {
            project2.out.println("There is no such employee.");
            return;
        }
        Employee employee = employees.findEmployee(empName);
        int proPoint = score / 200;
        int bonus = score % 200;
        employee.promotionPoint += proPoint;
        if (bonus > 0) {
            monthlyBonus += bonus;
            totalBonus += bonus;
        }
        checkPromoAndDismiss(employee);

    }

    private void addCheck(String name) {
        Iterator<Employee> iterator;
        Employee employee = employees.findEmployee(name);
        if (employee.type.equals("CASHIER")) {
            if (!cashierToBeFired.isEmpty()) {
                ArrayList<Employee> dumAl1 = new ArrayList<>();
                dumAl1.addAll(cashierToBeFired);
                iterator = dumAl1.iterator();
                while (iterator.hasNext()) {
                    Employee cashierToFire = iterator.next();
                    dismiss(cashierToFire);
                }
                iterator.remove();
            } else {
                ArrayList<Employee> dumAl2 = new ArrayList<>();
                dumAl2.addAll(cashierList);
                iterator = dumAl2.iterator();
                while (iterator.hasNext()) {
                    Employee employee1 = iterator.next();
                    if (employee1.promotionPoint >= 3) {
                        cashierList.remove(employee1);
                        employee1.type = "COOK";
                        employee1.promotionPoint -= 3;
                        cookList.add(employee1);
                        project2.out.println(employee1.name + " is promoted from Cashier to Cook.");
                        if (!cookToBeFired.isEmpty()) {
                            ArrayList<Employee> dumAl3 = new ArrayList<>();
                            dumAl3.addAll(cookToBeFired);
                            Iterator<Employee> iterator1 = dumAl3.iterator();
                            while (iterator1.hasNext()) {
                                Employee firedCook = iterator1.next();
                                dismiss(firedCook);
                            }
                            iterator1.remove();
                        }
                        if (employee1.promotionPoint >= 10) {
                            cookPromoQueue.add(employee1);
                        }
                        if (!cookPromoQueue.isEmpty() && manager.promotionPoint <= -5) {
                            dismiss(manager);
                            promoteToManager(employees.findEmployee(cookPromoQueue.poll().name));
                        }

                    }
                }
                iterator.remove();
            }

        } else if (employee.type.equals("COOK")) {
            if (!cookToBeFired.isEmpty()) {
                ArrayList<Employee> dumal1 = new ArrayList<>();
                dumal1.addAll(cookToBeFired);
                iterator = dumal1.iterator();
                while (iterator.hasNext()) {
                    Employee cookToFire = iterator.next();
                    dismiss(cookToFire);
                }
                iterator.remove();
            }
            if (!cookPromoQueue.isEmpty() && manager.promotionPoint <= -5) {
                dismiss(manager);
                promoteToManager(employees.findEmployee(cookPromoQueue.poll().name));
            }
        } else if (employee.type.equals("COURIER")) {
            if (!courierToBeFired.isEmpty()) {
                ArrayList<Employee> dumal = new ArrayList<>();
                dumal.addAll(cookToBeFired);
                iterator = dumal.iterator();
                while (iterator.hasNext()) {
                    Employee courierToFire = iterator.next();
                    dismiss(courierToFire);
                }
                iterator.remove();
            }
        }

    }

    //CHECKERS FOR PERFORMANCE_UPDATE
    private void checkPromoAndDismiss(Employee employee) {
        if (employee.type.equals("CASHIER")) {
            cashierCheck(employee);
        } else if (employee.type.equals("COOK")) {
            cookCheck(employee);
        } else if (employee.type.equals("COURIER")) {
            courierCheck(employee);
        } else if (employee.type.equals("MANAGER")) {
            managerCheck(employee);
        }
    }

    private void managerCheck(Employee employee) {
        if (employee.promotionPoint <= -5 && cookList.size() > 1 && !cookPromoQueue.isEmpty()) {
            dismiss(manager);
            promoteToManager(employees.findEmployee(cookPromoQueue.poll().name));
        }

    }

    private void courierCheck(Employee employee) {
        if (employee.promotionPoint > -5 && courierToBeFired.contains(employee)) {
            courierToBeFired.remove(employee);
        }
        if (employee.promotionPoint <= -5) {
            courierToBeFired.add(employee);
            if (courierList.size() > 1) {
                dismiss(employee);
            }
        }
    }

    private void cashierCheck(Employee employee) {
        if (employee.promotionPoint <= -5) {
            cashierToBeFired.add(employee);
            if (cashierList.size() > 1) {
                dismiss(employee);
            }
            return;
        }
        if (employee.promotionPoint > -5 && cashierToBeFired.contains(employee)) {
            cashierToBeFired.remove(employee);
        }
        if (employee.promotionPoint >= 3 && cashierList.size() > 1) {
            cashierList.remove(employee);
            employee.type = "COOK";
            employee.promotionPoint -= 3;
            cookList.add(employee);
            project2.out.println(employee.name + " is promoted from Cashier to Cook.");
            if (!cookToBeFired.isEmpty()) {
                ArrayList<Employee> dumal = new ArrayList<>();
                dumal.addAll(cookToBeFired);
                Iterator<Employee> iterator = dumal.iterator();
                while (iterator.hasNext()) {
                    Employee firedCook = iterator.next();
                    dismiss(firedCook);
                }
            }
            if (employee.promotionPoint >= 10) {
                cookPromoQueue.add(employee);
            }
            if (!cookPromoQueue.isEmpty() && manager.promotionPoint <= -5) {
                dismiss(manager);
                promoteToManager(employees.findEmployee(cookPromoQueue.poll().name));
            }
        }

    }

    private void cookCheck(Employee employee) {
        if (cookPromoQueue.contains(employee) && employee.promotionPoint < 10) {
            cookPromoQueue.remove(employee);
        }
        if (employee.promotionPoint <= -5) {
            cookToBeFired.add(employee);
            if (cookList.size() > 1) {
                dismiss(employee);
            }
            return;
        }
        if (employee.promotionPoint > -5 && cookToBeFired.contains(employee)) {
            cookToBeFired.remove(employee);
        }

        if (employee.promotionPoint >= 10) {
            if (!cookPromoQueue.contains(employee)) {
                cookPromoQueue.add(employee);
            }
            if (manager.promotionPoint <= -5 && cookList.size() > 1) {
                dismiss(manager);
                promoteToManager(employees.findEmployee(cookPromoQueue.poll().name));
            }
        }
    }

    private void promoteToManager(Employee employee) {
        cookList.remove(employee);
        employee.type = "MANAGER";
        employee.promotionPoint -= 10;
        manager = employee;
        project2.out.println(manager.name + " is promoted from Cook to Manager.");

    }

    private void dismiss(Employee employee) {
        if (employee.promotionPoint > -5) {
            return;
        }
        if (employee.isActive) {
            employees.remove(employee);
            project2.out.println(employee.name + " is dismissed from branch: " + name + ".");
            String type = employee.type;
            if (type.equals("COURIER")) {
                courierList.remove(employee);
                courierToBeFired.remove(employee);
            } else if (type.equals("CASHIER")) {
                cashierList.remove(employee);
                cashierToBeFired.remove(employee);
            } else if (type.equals("COOK")) {
                cookList.remove(employee);
                cookToBeFired.remove(employee);
            } else if (type.equals("MANAGER")) {
                manager = null;
            }
        }
    }

    public void leave(String empName) {
        if (!employees.contains(empName)) {
            project2.out.println("There is no such employee.");
            return;
        }
        Employee employee = employees.findEmployee(empName);
        leaveCheck(employee);
    }

    private void leaveCheck(Employee employee) {
        if (employee.type.equals("CASHIER")) {
            leaveCashier(employee);
        } else if (employee.type.equals("COOK")) {
            leaveCook(employee);
        } else if (employee.type.equals("COURIER")) {
            leaveCourier(employee);
        } else if (employee.type.equals("MANAGER")) {
            leaveManager(employee);
        }
    }

    //CHECKERS FOR EACH TYPE FOR LEAVE COMMAND

    private void leaveCourier(Employee employee) {
        if (courierList.size() == 1) {
            if (employee.promotionPoint > -5) {
                monthlyBonus += 200;
                totalBonus += 200;
            }
        } else {
            employees.remove(employee);
            project2.out.println(employee.name + " is leaving from branch: " + name + ".");
            courierList.remove(employee);
        }
    }

    private void leaveCashier(Employee employee) {
        if (cashierList.size() == 1) {
            if (employee.promotionPoint > -5) {
                monthlyBonus += 200;
                totalBonus += 200;
            }
        } else {
            employees.remove(employee);
            project2.out.println(employee.name + " is leaving from branch: " + name + ".");
            cashierList.remove(employee);
        }
    }

    private void leaveCook(Employee employee) {
        if (cookList.size() == 1) {
            if (employee.promotionPoint > -5) {
                monthlyBonus += 200;
                totalBonus += 200;
            }
        } else {
            if (cookPromoQueue.contains(employee)) {
                cookPromoQueue.remove(employee);
            }
            employees.remove(employee);
            project2.out.println(employee.name + " is leaving from branch: " + name + ".");
            cookList.remove(employee);
        }
    }

    private void leaveManager(Employee employee) {
        if (cookList.size() == 1) {
            if (employee.promotionPoint > -5) {
                monthlyBonus += 200;
                totalBonus += 200;
            }
        } else {
            if (!cookPromoQueue.isEmpty()) {
                project2.out.println(employee.name + " is leaving from branch: " + name + ".");
                employees.remove(employee);
                manager = null;
                Employee employee1 = employees.findEmployee(cookPromoQueue.poll().name);
                cookList.remove(employee1);
                employee1.type = "MANAGER";
                employee1.promotionPoint -= 10;
                manager = employee1;
                project2.out.println(manager.name + " is promoted from Cook to Manager.");
            } else {
                if (employee.promotionPoint > -5) {
                    monthlyBonus += 200;
                    totalBonus += 200;
                }
            }
        }
    }
}


