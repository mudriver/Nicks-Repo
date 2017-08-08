alter table te_pension modify column `employment_category` enum('Assistant Trainer','Blacksmith / Farrier','General Maintenance','Groom','Head Lad / Girl','Horse Box Driver','Manager','Office Administrator','Race Day Help','Rider','Security','Travelling Head Lad / Girl','Yard Person');

alter table te_pension modify column `employment_category` varchar(50);

alter table te_pension modify column `pension_date_joined_scheme` date;

alter table te_employment_history modify column `card_type` varchar(1);

alter table te_employment_history modify column `pension_type` varchar(1);

alter table te_employment_history modify column `hri_acc_number` varchar(30);

alter table te_trainers	add column trainer_license_type int(20);

update te_trainers set trainer_license_type=1;

update te_trainers set trainer_hunter_chase = "yes" where trainer_hunter_chase is null; 

update te_trainers set trainer_curragh = "yes" where trainer_curragh is null;