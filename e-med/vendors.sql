-- Insert additional pharmacy vendors into seller table
INSERT INTO seller (sid, sname, pass, address, phno) VALUES 
('vendor03', 'HealthKart Pharmacy', 'health123', '303 Wellness Street, Bangalore', 7345678901),
('vendor04', 'Guardian Pharmacy', 'guard456', '404 Care Avenue, Chennai', 6456789012),
('vendor05', 'Netmeds Store', 'net789', '505 Digital Plaza, Hyderabad', 5567890123),
('vendor06', '1mg Pharmacy', 'onemg321', '606 Quick Lane, Pune', 4678901234),
('vendor07', 'Pharmeasy Store', 'easy654', '707 Express Road, Kolkata', 3789012345);

-- View all vendors
SELECT * FROM seller;