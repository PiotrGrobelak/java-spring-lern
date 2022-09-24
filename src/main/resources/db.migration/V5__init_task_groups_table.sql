create table tasks_groups(
    id int primary key auto_increment,
    description varchar(100) not null,
    done bit
)

ALTER TABLE tasks ADD COLUMN task_group_id int null;
ALTER TABLE tasks ADD foreign key (task_group_id) references tasks_groups(id);
