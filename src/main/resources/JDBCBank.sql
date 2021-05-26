create table clients (
    client_id serial primary key,
    username varchar(40),
    passwordhash varchar(64)
);

create table accounts (
    account_id serial primary key,
    balance double precision,
    account_status varchar(10),
    account_name varchar(40)
);

create table clients_accounts (
    client_id integer references clients(client_id),
    account_id integer references accounts(account_id)
);

create table transactions (
    transaction_id serial primary key,
    source_id integer,
    destination_id integer,
    initiator integer,
    amount double precision,
    transaction_type varchar(10)
);

create table deleted_clients (
    client_id integer primary key,
    username varchar(40)
);

create table deleted_accounts (
    account_id integer primary key,
    balance double precision,
    account_name varchar(40)
);

create table logs (
	eventDate varchar(255),
	level varchar(255),
	logger varchar(255),
	message varchar(255),
	exception varchar(255)
);

CREATE OR REPLACE FUNCTION public.get_account(id integer, name_account text)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
    declare
    account integer;
    begin
        select accounts.account_id
			from ((clients_accounts
			inner join clients on clients.client_id = clients_accounts.client_id)
			inner join accounts on accounts.account_id = clients_accounts.account_id)
			where clients_accounts.client_id = id and accounts.account_name = name_account
 			into account;
       return account;
    end;
    $function$;