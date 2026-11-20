package banking.dao;

import banking.model.CompanyCustomer;
import banking.model.Customer;
import java.util.UUID;

public class DatabaseTest {
    public static void main(String[] args) throws Exception {
        Database.initialize();
        CustomerDAO dao = new CustomerDAO();

        String id = "TEST-" + UUID.randomUUID();
        CompanyCustomer customer = new CompanyCustomer(
                id, "Gaborone", "71234567",
                "Database Test Company", "TEST-REG"
        );

        try {
            dao.create(customer);

            Customer loaded = dao.findById(id);

            if (!(loaded instanceof CompanyCustomer company)
                    || !company.getCompanyName()
                            .equals("Database Test Company")) {
                throw new AssertionError("Create/read failed.");
            }

            System.out.println("Create and read passed.");

            loaded.setAddress("Francistown");
            dao.update(loaded);

            if (!"Francistown".equals(
                    dao.findById(id).getAddress())) {
                throw new AssertionError("Update failed.");
            }

            System.out.println("Update passed.");

            boolean found = dao.findAll().stream()
                    .anyMatch(item -> id.equals(item.getCustomerId()));

            if (!found) {
                throw new AssertionError("Find all failed.");
            }

            System.out.println("Find all passed.");

            dao.delete(id);

            if (dao.findById(id) != null) {
                throw new AssertionError("Delete failed.");
            }

            System.out.println("Delete passed.");
            System.out.println("Customer database test passed.");
        } finally {
            if (dao.findById(id) != null) {
                dao.delete(id);
            }
        }
    }
}