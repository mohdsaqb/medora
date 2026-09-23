-- Demo data for Medora (PostgreSQL).
--
-- Run this after the application has started once, so Hibernate has created
-- the schema and sequences:
--   psql "postgresql://medora:medora@localhost:5432/medora" -f backend/seed.sql
--
-- City values must match the com.medora.entity.enums.City constants exactly,
-- because the column is mapped with @Enumerated(EnumType.STRING).
--
-- Contact numbers are deliberately fake placeholders (+91 90000 000NN),
-- so demo data is never mistaken for a real person's number.
--
-- status: 1 = active, 0 = archived.

INSERT INTO patient (patientid, name, lastname, phone_no, born_date, gender, city, email, status) VALUES
  (nextval('patient_seq'), 'Aarav',  'Sharma', '+91 90000 00001', DATE '1975-04-12', 'Male',   'DELHI',              'aarav.sharma@example.com',  1),
  (nextval('patient_seq'), 'Priya',  'Nair',   '+91 90000 00002', DATE '1988-09-03', 'Female', 'KOCHI',              'priya.nair@example.com',    1),
  (nextval('patient_seq'), 'Rohan',  'Mehta',  '+91 90000 00003', DATE '1992-01-22', 'Male',   'MUMBAI',             'rohan.mehta@example.com',   1),
  (nextval('patient_seq'), 'Ananya', 'Iyer',   '+91 90000 00004', DATE '1994-07-30', 'Female', 'CHENNAI',            'ananya.iyer@example.com',   1),
  (nextval('patient_seq'), 'Vikram', 'Singh',  '+91 90000 00005', DATE '1969-11-15', 'Male',   'JAIPUR',             'vikram.singh@example.com',  1),
  (nextval('patient_seq'), 'Meera',  'Reddy',  '+91 90000 00006', DATE '1983-02-08', 'Female', 'HYDERABAD',          'meera.reddy@example.com',   1),
  (nextval('patient_seq'), 'Arjun',  'Patel',  '+91 90000 00007', DATE '2001-06-19', 'Male',   'AHMEDABAD',          'arjun.patel@example.com',   1),
  (nextval('patient_seq'), 'Kavya',  'Menon',  '+91 90000 00008', DATE '2012-03-27', 'Female', 'THIRUVANANTHAPURAM', 'kavya.menon@example.com',   1),
  (nextval('patient_seq'), 'Rahul',  'Verma',  '+91 90000 00009', DATE '1991-08-05', 'Male',   'LUCKNOW',            'rahul.verma@example.com',   1),
  (nextval('patient_seq'), 'Sneha',  'Joshi',  '+91 90000 00010', DATE '1997-12-01', 'Female', 'PUNE',               'sneha.joshi@example.com',   1),
  (nextval('patient_seq'), 'Imran',  'Sheikh', '+91 90000 00011', DATE '1986-05-14', 'Male',   'BHOPAL',             'imran.sheikh@example.com',  1),
  (nextval('patient_seq'), 'Divya',  'Rao',    '+91 90000 00012', DATE '1979-10-23', 'Female', 'BENGALURU',          'divya.rao@example.com',     1);
