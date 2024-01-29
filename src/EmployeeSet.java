public class EmployeeSet {
    private static final int DEFAULT_TABLE_SIZE = 997;

    private Employee[] employees;
    private int occupied;
    private int size;

    EmployeeSet() {
        this(DEFAULT_TABLE_SIZE);
    }

    EmployeeSet(int size) {
        allocateArray(size);
        clear();
    }

    private void allocateArray(int arraySize) {
        employees = new Employee[nextPrime(arraySize)];
    }

    private void clear() {
        occupied = 0;
        for (int i = 0; i < employees.length; i++) {
            employees[i] = null;
        }
    }

    private boolean insert(Employee employee) {
        int currentPos = findPos(employee.name);
        if (isActive(currentPos)) {
            return false;
        }

        if (employees[currentPos] == null) {
            occupied++;
        }
        employees[currentPos] = employee;
        this.size++;

        if (occupied > employees.length / 2) {
            rehash();
        }

        return true;

    }

    private int findPos(String name) {
        int offset = 1;
        int currentPos = hash(name);

        while (employees[currentPos] != null && !employees[currentPos].name.equals(name)) {
            currentPos += offset;
            offset += 2;
            if (currentPos >= employees.length) {
                currentPos -= employees.length;
            }
        }

        return currentPos;
    }

    public boolean contains(String name) {
        int currentPos = findPos(name);
        return isActive(currentPos);
    }

    private boolean isActive(int currentPos) {
        return employees[currentPos] != null && employees[currentPos].isActive;
    }

    private int hash(String name) {
        int hashVal = name.hashCode();
        hashVal = hashVal % employees.length;
        if (hashVal < 0) {
            hashVal += employees.length;
        }
        return hashVal;
    }

    private void rehash() {
        Employee[] oldArray = employees;
        allocateArray(2 * oldArray.length);
        occupied = 0;
        size = 0;
        for (Employee entry : oldArray) {
            if (entry != null && entry.isActive) {
                insert(entry);
            }
        }
    }

    private static int nextPrime(int currentPrime) {
        if (currentPrime % 2 == 0) {
            currentPrime++;
        }
        while (!isPrime(currentPrime)) {
            currentPrime += 2;
        }
        return currentPrime;
    }

    private static boolean isPrime(int n) {
        if (n == 2 || n == 3) {
            return true;
        }
        if (n == 1 || n % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;

    }

    public Employee findEmployee(String name) {
        int offset = 1;
        int currentPos = hash(name);

        while (employees[currentPos] != null && !employees[currentPos].name.equals(name)) {
            currentPos += offset;
            offset += 2;
            if (currentPos >= employees.length) {
                currentPos -= employees.length;
            }
        }

        return employees[currentPos];
    }

    public void addEmployee(Employee employee) {
        insert(employee);
    }

    public boolean remove(Employee employee) {
        int currentPos = findPos(employee.name);
        if (isActive(currentPos)) {
            employees[currentPos].isActive = false;
            size--;
            return true;
        }
        else {
            return false;
        }
    }


}
