insert into user values(101, 'darth.vader@gmail.com', 'darth', 'vader', 'admin', 'ssn-01-0000', 'darth.vader');
insert into user values(102, 'super.man@gmail.com', 'super', 'man', 'admin', 'ssn-02-0000', 'super.man');
insert into user values(103, 'thor@gmail.com', 'thor', 'odinson', 'admin', 'ssn-03-0000', 'thor.odinson');

-- one way to create the data for order
--insert into orders values( 2001, 'order11', 101);
--insert into orders values( 2002, 'order12', 101);
--insert into orders values( 2003, 'order13', 101);
--insert into orders values( 2004, 'order21', 102);
--insert into orders values( 2005, 'order22', 102);
--insert into orders values( 2006, 'order31', 103);

-- 2nd way to create data
insert into orders (order_id, order_description, user_id) values( 2001, 'order11', 101);
insert into orders (order_id, order_description, user_id) values( 2002, 'order12', 101);
insert into orders (order_id, order_description, user_id) values( 2003, 'order13', 101);
insert into orders (order_id, order_description, user_id) values( 2004, 'order21', 102);
insert into orders (order_id, order_description, user_id) values( 2005, 'order22', 102);
insert into orders (order_id, order_description, user_id) values( 2006, 'order31', 103);