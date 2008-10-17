use DBI;

use Getopt::Std;  
getopts('hUp:', \ my %opts);    # -ptake arg.  Sets opt_* as a side effect.

if($opts{h} ne ""){
    print "usage: ", $pgm, " [-U] [-p <password>]\n where -U->update mode (build AND run sql script),\n", "<password> is database password. Other params are read from perl.cfg file\n";
    exit 0;
 }


my $os = lc $^O;

my $configfile;
my $linux;
if($os =~ m/mswin/){
    $configfile = '..\perlscripts.windows.cfg';
    $islinux=0;
}
else{
     $configfile = '../perlscripts.linux.cfg';
    $islinux=1;
}

print "debug: configfile= ", $configfile, "\n"; 

if($islinux){
    $ENV{'ORACLE_HOME'}="/opt/fdbin/oracle/instantclient_11_1";
    BEGIN {
            push @INC,"../lib";
           }
    }
else{
        BEGIN {
            push @INC,"..\lib";
        }
}

use PerlConfig;
use Common;
use Runsql;
 
my $pgm = lc $0;        #lc->convert to lowercase
$pgm =~ s/\..*//;       #remove file extension

my $cfg = new PerlConfig;
my $cmn = new Common;

$cfg->read($configfile) or die "Couldn't read Config file: $!";
 
my $dbname = $cfg->get("$pgm.dbname");
my $dbuser = $cfg->get("$pgm.dbuser");
my $infile=$cfg->get("$pgm.infile");

my $sfx = $cmn->get_highest_suffix($infile);

$infile = $cmn->make_suffix_file($infile, $sfx);

my $outfile=$cfg->get("$pgm.outfile");
$outfile = $cmn->make_suffix_file($outfile, $sfx);

my $sqloutfile=$cfg->get("$pgm.sqloutfile");
$sqloutfile = $cmn->make_suffix_file($sqloutfile, $sfx);

my $pw   = $dbuser;

if ($opts{p} ne ""){
    $pw = $opts{p};
}

my $updatemode=0;

if($opts{U} ne ""){
    $updatemode=1;
}

print "debug: dbname= ", $dbname, "\n";
print "debug: dbuser= ", $dbuser, "\n";
print "debug: appuser= ", $appuser, "\n";
print "debug: infile= ", $infile, "\n";
print "debug: outfile= ", $outfile, "\n";
print "debug: sqloutfile= ", $sqloutfile, "\n";
print "debug: password=" ,  $pw, "\n"; #avoid displaying password





print "debug: dbname= ", $dbname, "\n";
     
my $dbh  = DBI->connect("dbi:Oracle:$dbname", $dbuser, $pw)
      or die "Couldn't connect to database: " . DBI->errstr;


my $tsale = $dbh->prepare('SELECT CUSTOMER_ID FROM CUST.sale where id=?')    
        or die "Couldn't prepare statement: " . $dbh->errstr;
        
my $tcomplaint = $dbh->prepare('SELECT 1 FROM CUST.Complaint where Sale_id=? and complaint_type = ? and status = ?')      
        or die "Couldn't prepare statement: " . $dbh->errstr;
                

my @data;
 
my $inrec, $outrec, $sqlrec;
my @outholder, @sqlholder;

my $sale_id, $amount, $comp_code, $comp_dept, $result;  #comp_Code->LATEDEL, #comp_dept->GDW
my $cust_id;
my $cnt=0;

my $status  = 'APP';   #tbr
 
open(FHIN, "<$infile") or die "Cannot open $file: $!";

my @content = <FHIN>;

foreach $inrec(@content) {
    
    chomp($inrec);
    
    if ($inrec eq "" || $inrec =~ m/^#/){     #record starting with '#' is ignored
       next;
    }
    
    (@warray) = split(",", $inrec);
    
    $outrec = $sale_id = $warray[0]; 
    $amount = $warray[1]; $comp_code = $warray[2]; $comp_dept = $warray[3];    
      
    $tsale->execute($sale_id)  or die "Couldn't execute statement: " . $tsale->errstr;
    @data = $tsale->fetchrow_array();
     
    if ($tsale->rows == 0) {
        $result = ",Invalid Sale ID\n"; 
        $outrec .= $result;
        push @outholder, $outrec; 
        next;
    }  
    $cust_id = $data[0];     
    
    $tcomplaint->execute($sale_id, $comp_dept, $status)  or die "Couldn't execute statement: " . $tcomplaint->errstr;    
    $tcomplaint->fetchrow_array();
    if ($tcomplaint->rows ne 0) {
        $result = ",Credit Already given\n";
        $outrec .= $result;
        push @outholder, $outrec; 
        next;   
    }
           
    $result = ",OK\n"; 
    
    $outrec .= $result; 

    push @outholder, $outrec;   
    
    $sqlrec = buildcomplaint($sale_id, $cust_id, $amount, $comp_code, $comp_dept, $appuser);
    push @sqlholder, $sqlrec;
    
    $sqlrec = buildcomplaintline($sale_id, $cust_id, $amount, $comp_code, $comp_dept, $appuser);
    push @sqlholder, $sqlrec;
    
    $sqlrec = buildcustomercredit($sale_id, $cust_id, $amount, $comp_code, $comp_dept, $appuser);
    $cnt++;    
    push @sqlholder, $sqlrec;    
}

my $totcnt = 3 * $cnt;
$sqlrec = '-- 3 x '; $sqlrec .= $cnt; $sqlrec .=  ' =  '; $sqlrec .= $totcnt ; $sqlrec .= ' records will be inserted.';
push @sqlholder, $sqlrec;

close FHIN;

open (FHOUT, ">$outfile") or die "Cannot open $file: $!";
print FHOUT @outholder;
close FHOUT;

open (FHSQL, ">$sqloutfile") or die "Cannot open $file: $!";
print FHSQL @sqlholder;
close FHSQL;

$tsale->finish;
$tcomplaint->finish;

$dbh->disconnect;

if($updatemode){
    my $runsql = new Runsql;
    print "debug: calling runsql.pm -f $sqloutfile -d $dbname -u $dbuser -p ******", "\n";
    $runsql->run($sqloutfile, $dbname, $dbuser, $pw);
}

print "\n";

sub buildcomplaint
{
        #$sqlrec = buildsql($sale_id, $cust_id, $amount, $comp_code, $comp_dept);
        
    #INSERT INTO cust.complaint (id, create_date, created_by, status, approved_Date, approved_by, amount, note, sale_id,complaint_type) 
    #VALUES(cust.SYSTEM_SEQ.NEXTVAL, SYSDATE, 'abhatia', 'APP', SYSDATE, 'abhatia', 10, 'Mass credit', '<sale_id>','FDC');

    my $sale_id   = $_[0];
    my $cust_id   = $_[1];
    my $amount    = $_[2];
    my $comp_code =  $_[3];    #eg LATEDEL
    my $comp_dept =  $_[4];    #eg GDW
    my $appuser   =  $_[5];
    my $status = 'APP';        #tbr
    my $notes = 'Mass Credit for late order';
    my $result='INSERT INTO CUST.COMPLAINT(id, create_date, created_by, status, approved_Date, approved_by, amount, note, sale_id,complaint_type) ';
    $result .= 'VALUES(cust.SYSTEM_SEQ.NEXTVAL, SYSDATE, "';
    $result .= $appuser;    $result .= '", "';
    $result .= $status;  $result .= '", ';
    $result .= 'SYSDATE'; $result .= ', "';
    $result .= $appuser;    $result .= '", ';
    $result .= $amount;  $result .= ', "';
    $result .= $notes;   $result .= '", "';
    $result .= $sale_id; $result .= '", "';  
    $result .= $comp_dept;  
    $result .= '");';
    
    $result =~ tr/\"/\'/;
    
    $result .= "\n\n";
  
    return $result;    
}

sub buildcomplaintline
{
        #$sqlrec = buildsql($sale_id, $cust_id, $amount, $comp_code, $comp_dept);
        
  #INSERT INTO cust.complaintline(id, complaint_id, amount, method, complaint_type, complaint_comp_dept_code_id) 
  #VALUES (cust.SYSTEM_SEQ.NEXTVAL, 
  #             (SELECT ID FROM cust.complaint WHERE created_by='abhatia' AND create_date > TO_DATE('09/10/2008 05:00:00 PM','MM/DD/YYYY HH:MI:SS PM') 
  #              AND sale_id= '<sale_id>'), 
  #    10, 'FDC', 'GDW', '1416617116');

    my $sale_id   = $_[0];
    my $cust_id   = $_[1];
    my $amount    =  $_[2];
    my $comp_code =  $_[3];    #eg LATEDEL
    my $comp_dept =  $_[4];    #eg GDW
    my $appuser   =  $_[5];
    my $status = 'APP';
    my $notes = 'Mass Credit for late order';
    my $method = 'FDC';
    my $complaint_comp_dept_code_id = '41924113';  #tbr get it from database
    
    my $result='INSERT INTO CUST.COMPLAINTLINE(id, complaint_id, amount, method, complaint_type, complaint_dept_code_id) ';
    $result .= 'VALUES(cust.SYSTEM_SEQ.NEXTVAL, ';
    $result .= '(SELECT id FROM CUST.COMPLAINT where created_by = "';
    $result .= $appuser;    $result .= '"';     
    $result .= ' AND create_date >= (select SYSDATE-1 from dual)';          #minus 1 just in case this is run around midnight
    $result .= ' AND sale_id ="';
    $result .= $sale_id; $result .= '")'; $result .= ', ';
    $result .= $amount; $result .= ', "';
    $result .= $method; $result .= '", "';    
    $result .= $comp_dept; $result .= '", "';    
    $result .= $complaint_comp_dept_code_id; 
    $result .= '");';
    
    $result =~ tr/\"/\'/;
    
    $result .= "\n\n";  
  
    return $result;    
}

sub buildcustomercredit
{
        #$sqlrec = buildsql($sale_id, $cust_id, $amount, $comp_code, $comp_dept);

#INSERT INTO cust.customercredit (id, amount, original_amount, DEPARTMENT, customer_id, complaint_id,create_date) 
#VALUES (cust.SYSTEM_SEQ.NEXTVAL,10,10,'GDW',
    #(SELECT customer_id FROM cust.sale WHERE id='<sale_id>'), 
    #(SELECT ID FROM cust.complaint WHERE created_by='abhatia' AND create_date >= SYSDATE-1 AND sale_id= '<sale_id>'), 
#SYSDATE);

    my $sale_id   = $_[0];
    my $cust_id   = $_[1];
    my $amount    =  $_[2];
    my $comp_code =  $_[3];    #eg LATEDEL
    my $comp_dept =  $_[4];    #eg GDW
    my $appuser   =  $_[5];
    my $status = 'APP';
    my $notes = 'Mass Credit for late order';
    my $method = 'FDC';
    #my $complaint_comp_dept_code_id = '1416617116';  #tbD get it from database
    
    my $result='INSERT INTO CUST.CUSTOMERCREDIT(id, amount, original_amount, DEPARTMENT, customer_id, complaint_id,create_date) ';
    $result .= 'VALUES(cust.SYSTEM_SEQ.NEXTVAL, ';
    $result .= $amount; $result .= ', ';
    $result .= $amount; $result .= ', "';
    
    $result .= $comp_dept; $result .= '", ';    
 
    $result .= '(SELECT ID FROM CUST.COMPLAINT_DEPT_CODE where COMP_CODE = "';    
    $result .= $comp_code;    $result .= '"';     
    $result .= ' AND COMP_DEPT = "';
    $result .= $comp_dept; $result .= '"';
    $result .= '), "';    
    
    $result .= $cust_id;    $result .= '", ';   
    $result .= '(SELECT id FROM CUST.COMPLAINT where created_by ="';    
    $result .= $appuser;    $result .= '"';     
    $result .= ' AND create_date >= (select SYSDATE-1 from dual)';          #minus 1 just in case this is run around midnight
    $result .= ' AND sale_id ="';
    $result .= $sale_id; $result .= '")'; $result .= ', ';
    $result .= SYSDATE;
    $result .= ');';
    
    $result =~ tr/\"/\'/;
    
    $result .= "\n\n";  
  
    return $result;    
}  

