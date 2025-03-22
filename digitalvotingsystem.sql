-- Create Database and select it
CREATE DATABASE IF NOT EXISTS DigitalVotingSystem;
USE DigitalVotingSystem;

-- Create Voter table
CREATE TABLE IF NOT EXISTS Voter (
    voter_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100)
    -- ...other fields...
);

-- Create Vote table
CREATE TABLE IF NOT EXISTS Vote (
    vote_id INT AUTO_INCREMENT PRIMARY KEY,
    vote_date DATE NOT NULL,
    voter_id INT,
    -- ...other fields...
    FOREIGN KEY (voter_id) REFERENCES Voter(voter_id)
);

-- Insert sample data into Voter
INSERT INTO Voter (name, email) VALUES 
('Surya', 'surya@gmail.com'),
('Sachin', 'sachin@gmail.com'),
('Hema', 'hema@gmail.com'),
('Arjun', 'arjun@gmail.com');

-- Insert sample data into Vote
INSERT INTO Vote (vote_date, voter_id) VALUES
('2023-10-01', 1),
('2023-10-02', 2),
('2023-10-03', 3),
('2023-10-04', 4);

-- SQL Join Query using the new table names
SELECT v.name, vt.vote_date 
FROM Voter v 
INNER JOIN Vote vt ON v.voter_id = vt.voter_id;
