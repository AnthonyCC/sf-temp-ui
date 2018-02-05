update cust.address
	set UNATTENDED_FLAG = 0
	where UNATTENDED_FLAG is null; 

update cust.address
	set UNATTENDED_INSTR = 'OK'
	where UNATTENDED_INSTR is null;

