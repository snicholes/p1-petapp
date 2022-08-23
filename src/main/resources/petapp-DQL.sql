-- DQL
select * from pet;

-- inner join
select * from pet join status on status_id = status.id where species='cat';
select * from status join pet on status_id = status.id where species='cat';
select * from pet join status on pet.status_id = status.id where species='cat' order by pet.name;
select * from pet join status on pet.status_id = status.id where species='cat' order by pet.name desc;

-- outer joins
select * from pet full outer join status on pet.status_id=status.id;
select * from pet right join status on pet.status_id=status.id;
select * from pet left join status on pet.status_id=status.id;

-- it doesn't have to be on a foreign key, we can join on any column
select * from pet join person on age=person.id;

-- cross join
select * from pet cross join status;

-- aliases
/* we're giving a column a temporary name for the purpose of this result set
   in order to remove ambiguity */
select pet.id,pet.name,species,description,age,status.name as status_name
	from pet join status on pet.status_id=status.id;
	

-- scalar functions
select upper(name) as capitalized_name, species, description, age from pet;
select concat(, ', ', species) from pet;
select substring(name,1,3) from pet;
select substring(upper(name), 1,1) from pet;

select name from pet;
select upper(name) from pet;

-- aggregate functions
select count(name) from pet;
select count(species) from pet;
select count(distinct species) from pet;
-- group by clause is for use with aggregate functions
select count(name), species from pet group by species;
select avg(age), species from pet group by species;
-- select round(avg(age)), species from pet group by species;
-- using the having clause allows this to be more readable
select count(name), species from pet group by species, status_id having status_id=2;
--select count(name), species from pet where status_id=2 group by species, status_id;


-- set operations
select * from pet where species='bird' union select * from pet where age=1; -- bird OR age 1, no duplicates
select * from pet where species='bird' union all select * from pet where age=1; -- bird OR age 1, with duplicates allowed
select * from pet where species='bird' intersect select * from pet where age=1; -- bird AND age 1
select * from pet where species='bird' except select * from pet where age=1; -- bird NOT age 1

-- it doesn't have to be the same table, the columns just have to be the same types
select name,age from pet union select full_name,id from person;