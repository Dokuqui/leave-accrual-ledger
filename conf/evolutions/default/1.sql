# --- !Ups

CREATE TABLE employees (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    hire_date DATE NOT NULL,
    contract_type VARCHAR(50) NOT NULL
);

CREATE TABLE leave_balances (
    employee_id VARCHAR(36) PRIMARY KEY,
    accrued_days DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
    taken_days DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
    pending_days DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

# --- !Downs

DROP TABLE leave_balances;
DROP TABLE employees;