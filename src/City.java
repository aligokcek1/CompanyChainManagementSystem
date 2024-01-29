public class City {
    private static final int DEFAULT_TABLE_SIZE = 31;

    private Branch[] branches;
    private int occupied;
    private int size;

    City() {
        this(DEFAULT_TABLE_SIZE);
    }

    City(int size) {
        allocateArray(size);
        clear();
    }

    // CUSTOM HASHSET METHODS(CLOSED HASHING WITH QUADRATIC PROBING)
    private void allocateArray(int arraySize) {
        branches = new Branch[nextPrime(arraySize)];
    }

    private void clear() {
        occupied = 0;
        for (int i = 0; i < branches.length; i++) {
            branches[i] = null;
        }
    }

    private boolean insert(Branch branch) {
        int currentPos = findPos(branch.id);
        if (isActive(currentPos)) {
            return false;
        }

        if (branches[currentPos] == null) {
            occupied++;
        }
        branches[currentPos] = branch;
        size++;

        if (occupied > branches.length / 2) {
            rehash();
        }

        return true;

    }

    private int findPos(String id) {
        int offset = 1;
        int currentPos = hash(id);

        while (branches[currentPos] != null && !branches[currentPos].id.equals(id)) {
            currentPos += offset;
            offset += 2;
            if (currentPos >= branches.length) {
                currentPos -= branches.length;
            }
        }

        return currentPos;
    }

    private boolean contains(String id) {
        int currentPos = findPos(id);
        return isActive(currentPos);
    }

    private boolean isActive(int currentPos) {
        return branches[currentPos] != null && branches[currentPos].isActive;
    }

    private int hash(String id) {
        int hashVal = id.hashCode();
        hashVal = hashVal % branches.length;
        if (hashVal < 0) {
            hashVal += branches.length;
        }
        return hashVal;
    }

    private void rehash() {
        Branch[] oldArray = branches;
        allocateArray(2 * oldArray.length);
        occupied = 0;
        size = 0;
        for (Branch entry : oldArray) {
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

    private Branch findBranch(String id) {
        int offset = 1;
        int currentPos = hash(id);

        while (branches[currentPos] != null && !branches[currentPos].id.equals(id)) {
            currentPos += offset;
            offset += 2;
            if (currentPos >= branches.length) {
                currentPos -= branches.length;
            }
        }

        return branches[currentPos];
    }

    // --------------------------------------------
    public void printManager(String id) {
        Branch br = findBranch(id);
        String managerName = br.getManager().name;
        project2.out.println("Manager of the " + br.name + " branch is " + managerName + ".");
    }

    public void printMonthlyBonus(String id) {
        Branch br = findBranch(id);
        project2.out.println("Total bonuses for the " + br.name + " branch this month are: " + br.monthlyBonus);
    }

    public void printTotalBonus(String id) {
        Branch br = findBranch(id);
        project2.out.println("Total bonuses for the " + br.name + " branch are: " + br.totalBonus);
    }

    private boolean checkBranch(String id) {
        return contains(id);

    }

    public void initialAddEmployee(String branchId, String empName, String empType) {
        Branch branch;
        if (!checkBranch(branchId)) {
            branch = new Branch(branchId);
            insert(branch);
        } else {
            branch = findBranch(branchId);
        }
        branch.initialAddEmployee(empName, empType);
    }

    public void addEmployee(String branchId, String empName, String empType) {
        Branch branch = findBranch(branchId);
        branch.addEmployee(empName, empType);
    }

    public void performanceUpdate(String branchId, String empName, int score) {
        Branch branch = findBranch(branchId);
        branch.performanceUpdate(empName, score);
    }

    public void leave(String branchId, String empName) {
        Branch branch = findBranch(branchId);
        branch.leave(empName);
    }

    public void resetMonthlyBonus(){
        for(Branch branch:branches){
            if(branch == null){
                continue;
            }
            branch.monthlyBonus = 0;
        }
    }
}
