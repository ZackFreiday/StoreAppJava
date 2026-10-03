# Store Management Application

## 1. Project Description

Store Management Application is a console-based Java application that simulates the core operations of a retail store.

The system manages goods, cashiers, customers, sales, receipts, inventory, and store finances. It supports different types of products, automatic price calculation, receipt generation, file persistence, and financial statistics.

---

## 2. Technologies Used

- Java 17
- Maven
- JUnit 5
- Mockito
- Java Serialization
- File I/O

---

## 3. Main Features

### Store Management
- Manage store inventory
- Load goods into the store
- Track available quantities
- Calculate store expenses and profit

### Product Management
- Food and non-food products
- Product codes and names
- Delivery prices
- Available quantities
- Expiration dates
- Different markup percentages depending on product type

### Cashier Management
- Register multiple cashiers
- Store cashier identifiers, names, and salaries
- Include cashier salaries in store expenses

### Customer Transactions
- Process customer purchases
- Validate requested product quantities
- Check customer funds
- Update inventory after successful sales
- Handle insufficient quantities through custom exceptions

### Receipt Management
- Generate receipts for completed transactions
- Print receipts to the console
- Save receipt information to text files
- Serialize receipts to files
- Load previously serialized receipts
- Track the number of issued receipts

### Financial Statistics
- Total turnover
- Delivery costs
- Salary expenses
- Store profit

---

## 4. Project Structure

- **Model Layer** – Store entities such as goods, customers, cashiers, and receipts
- **Service Layer** – Business logic for store operations and transactions
- **Exception Layer** – Custom exception handling
- **Utility Layer** – Receipt file persistence and serialization
- **Main Class** – Demonstrates the application's functionality
- **Test Layer** – Unit tests using JUnit 5 and Mockito

---

## 5. Testing

The project contains automated tests using:

- JUnit 5
- Mockito

Tests cover the store service and its business logic.

Run the tests with:

```bash
mvn test