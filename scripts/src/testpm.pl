use DBI;

use PerlConfig;

use foo;

use Getopt::Std;

my $os = lc $^O;
my $configfile = 'perlscripts.linux.cfg';
if($os =~ m/MSWin/){
    $configfile = 'perlscripts.windows.cfg';
}
print "debug: configfile= ", $configfile, "\n"; 

getopts('hp:', \ my %opts);    # -ptake arg.  Sets opt_* as a side effect.

if($opts{h} ne ""){
    print "usage: ", $pgm, " [-p <password>]\n where <password> is database password. Other params are read from perl.cfg file\n";
    exit 0;
 }
 
my $pgm = $0;
$pgm =~ s/\..*//;

my $cfg = new PerlConfig;

$cfg->read('$configfile') or die "Couldn't read Config file: $!";
 
my $dbname = $cfg->get("$pgm.dbname");
my $dbuser = $cfg->get("$pgm.dbuser");
my $appuser = $cfg->get("$pgm.appuser");
my $infile=$cfg->get("$pgm.infile");
my $outfile=$cfg->get("$pgm.outfile");
my $sqloutfile=$cfg->get("$pgm.sqloutfile");

my $pw   = $dbuser;

if ($opts{p} ne ""){
    $pw = $opts{p};
}

print "debug: dbname= ", $dbname, "\n";
print "debug: dbuser= ", $dbuser, "\n";
print "debug: appuser= ", $appuser, "\n";
print "debug: infile= ", $infile, "\n";
print "debug: outfile= ", $outfile, "\n";
print "debug: sqloutfile= ", $sqloutfile, "\n";

#print "debug: password=" ,  $pw, "\n"; 

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
    my $status = 'APP';
    my $notes = 'Mass Credit for late order';
    my $result='INSERT INTO CUST.COMPLAINT(id, create_date, created_by, status, approved_Date, approved_by, amount, note, sale_id,complaint_type) ';
    $result .= 'VALUES(cust.SYSTEM_SEQ.NEXTVAL, SYSDATE, "';
    $result .= $appuser;    $result .= '", "';
    $result .= $status;  $result .= '", ';
    $result .= 'SYSDATE'; $result .= ', "';
    $result .= $dbuser;    $result .= '", ';
    $result .= $amount;  $result .= ', "';
    $result .= $notes;   $result .= '", "';
    $result .= $sale_id; $result .= '", "';  
    $result .= $comp_dept;  
    $result .= '");';
    
    $result =~ tr/\"/\'/;
    
    $result .= "\n\n";
  
    return $result;    
}

# db->DBI:Oracle:DEVINT_10G

#my $db = $cfg->get("$pgm.dbname");
   #print "The db name is ", 
             #$db, 
             #".\n";
             
#my $db = 'DBI:Oracle:';

        

#my $dbh  = DBI->connect($connect_str, $dbuser, $pw)
        #or die "Couldn't connect to database: " . DBI->errstr;
        
#my $tsale = $dbh->prepare('SELECT CUSTOMER_ID FROM CUST.sale where id=?')    
        #or die "Couldn't prepare statement: " . $dbh->errstr;
        
#my $tcomplaint = $dbh->prepare('SELECT 1 FROM CUST.Complaint where Sale_id=? and complaint_type = ? and status = ?')      
        #or die "Couldn't prepare statement: " . $dbh->errstr;
                

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
      
    #$tsale->execute($sale_id)  or die "Couldn't execute statement: " . $tsale->errstr; 
    
    #@data = $tsale->fetchrow_array();
    
    $cust_id = $sale_id;
    
    $result = ",OK\n";
     
    $outrec .= $result; 

    push @outholder, $outrec;   
    
    $sqlrec = buildcomplaint($sale_id, $cust_id, $amount, $comp_code, $comp_dept, $appuser);
    push @sqlholder, $sqlrec;   
    $cnt++;
  
}

my $totcnt = 1 * $cnt;
$sqlrec = '-- 1 x '; $sqlrec .= $cnt; $sqlrec .=  ' =  '; $sqlrec .= $totcnt ; $sqlrec .= ' records will be inserted.';
push @sqlholder, $sqlrec;

close FHIN;

open (FHOUT, ">$outfile") or die "Cannot open $file: $!";
print FHOUT @outholder;
close FHOUT;

open (FHSQL, ">$sqloutfile") or die "Cannot open $file: $!";

print FHSQL @sqlholder;
close FHSQL;

#$tsale->finish;
#$tcomplaint->finish;

print "\n";

#$dbh->disconnect;