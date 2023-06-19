-- V1_1__create_tables.sql

-- Create the Department table
CREATE TABLE Department (
    ID SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    CONSTRAINT UK_Department_Name UNIQUE (Name)
);

-- Insert default departments
INSERT INTO Department (Name) VALUES ('Engineering');
INSERT INTO Department (Name) VALUES ('HR');

-- Create the User table
CREATE TABLE "User" (
    ID SERIAL PRIMARY KEY,
    Role VARCHAR(50) NOT NULL,
    Username VARCHAR(50) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Name VARCHAR(255) NOT NULL,
    Salary FLOAT NOT NULL,
    DepartmentID INT NOT NULL,
    CONSTRAINT FK_User_Department FOREIGN KEY (DepartmentID)
    REFERENCES Department (ID)
);