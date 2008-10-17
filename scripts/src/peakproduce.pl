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
print "debug: infile= ", $infile, "\n";
print "debug: outfile= ", $outfile, "\n";
print "debug: sqloutfile= ", $sqloutfile, "\n";
print "debug: updatemode=" ,  $updatemode, "\n";
print "debug: password=" ,  $pw, "\n"; #avoid displaying password

print "debug: trying DBI->connect('dbi:Oracle:$dbname', $dbuser, $pw)...\n";
     
my $dbh  = DBI->connect("dbi:Oracle:$dbname", $dbuser, $pw)
      or die "Couldn't connect to database: " . DBI->errstr;

my $tcust = $dbh->prepare('SELECT 1 FROM CUST.CUSTOMER where id=?')    
        or die "Couldn't prepare statement: " . $dbh->errstr;
        
my $tpromotion = $dbh->prepare('SELECT 1 FROM CUST.PROMOTION where id=?')    
        or die "Couldn't prepare statement: " . $dbh->errstr;        
        
my $tcustprom = $dbh->prepare('SELECT 1 FROM CUST.PROMO_CUSTOMER where Customer_id=? and Promotion_id=?')      
        or die "Couldn't prepare statement: " . $dbh->errstr;

my @data;
 
my $inrec, $outrec, $sqlrec;
my @outholder, @sqlholder;

my $cust_id, $promotion_id, $result;  #comp_Code->LATEDEL, #comp_dept->GDW

my $inscnt=0; 
my $delcnt=0;

print "debug2: infile= ", $infile, "\n";

open(FHIN, "<$infile") or die "Cannot open $file: $!";

my @content = <FHIN>;

foreach $inrec(@content) {
    
    chomp($inrec);
    
    if ($inrec eq "" || $inrec =~ m/^#/){     #record starting with '#' is ignored
       next;
    }
    
    (@warray) = split(",", $inrec);
    
    $outrec = $cust_id = $warray[0]; 
    $promotion_id = $warray[1];
    $outrec .= ','; $outrec .= $promotion_id;
 
    $tcust->execute($cust_id)  or die "Couldn't execute statement: " . $tcust->errstr;    
    $tcust->fetchrow_array();
    if ($tcust->rows == 0) {
        $result = ",Invalid Cust ID\n"; 
        $outrec .= $result;
        push @outholder, $outrec; 
        next;
    }
    
    $tpromotion->execute($promotion_id)  or die "Couldn't execute statement: " . $tpromotion->errstr;    
    $tpromotion->fetchrow_array();
    if ($tpromotion->rows == 0) {
        $result = ",Invalid Promotion ID\n"; 
        $outrec .= $result;
        push @outholder, $outrec; 
        next;
    }
    
    $tcustprom->execute($cust_id, $promotion_id)  or die "Couldn't execute statement: " . $tcustprom->errstr;

    print "debug: cust_id= ", $cust_id, " promotion_id= ", $promotion_id, "\n";
    $tcustprom->fetchrow_array();   

    print "debug: tcustprom->rows= ", $tcustprom->rows, "\n";
   
    if ($tcustprom->rows ne 0) {
        $result = ",Duplicate Customer Promotion record\n";
        $outrec .= $result;
        push @outholder, $outrec; 
        next;   
    }
    
    $result = ",OK\n";
    $outrec .= $result; 

    push @outholder, $outrec;
    
    $sqlrec = buildcustprom($cust_id, $promotion_id);
    push @sqlholder, $sqlrec;
    $inscnt++; 
}

$sqlrec = '-- '; $sqlrec .= $inscnt ; $sqlrec .= ' records will be inserted.';
$sqlrec .= "\n\n";
push @sqlholder, $sqlrec;

$sqlrec = builddeletecustprom($dbh);
push @sqlholder, $sqlrec;


my $tcount = $dbh->prepare('SELECT count(*) FROM CUST.PROMO_CUSTOMER where EXPIRATION_DATE is not null and SYSDATE >= EXPIRATION_DATE')    
        or die "Couldn't prepare statement: " . $dbh->errstr;   

$tcount->execute()  or die "Couldn't execute statement: " . $tcount->errstr;    
@data = $tcount->fetchrow_array();
if ($tcount->rows == 0) {
    $delcnt=0;
}
$delcnt = @data[0]; 

$sqlrec = '-- '; $sqlrec .= $delcnt ; $sqlrec .= ' records will be deleted.';
$sqlrec .= "\n\n";
push @sqlholder, $sqlrec;


close FHIN;

open (FHOUT, ">$outfile") or die "Cannot open $file: $!";
print FHOUT @outholder;
close FHOUT;

open (FHSQL, ">$sqloutfile") or die "Cannot open $file: $!";
print FHSQL @sqlholder;
close FHSQL;

$tcust->finish;
$tpromotion->finish;
$tcustprom->finish;
$tcount->finish;

$dbh->disconnect;

if($updatemode){
    my $runsql = new Runsql;
    print "debug: calling runsql.pm -f $sqloutfile -d $dbname -u $dbuser -p ******", "\n";
    $runsql->run($sqloutfile, $dbname, $dbuser, $pw);
}

print "\n";

sub buildcustprom
{
        #                               0       1           2           3           4           5       6       7
        #$sqlrec = buildactivitylog($cust_id, $promotion_id);
  
    my $cust_id   = $_[0];
    my $promotion_id   = $_[1];
       
    my $result='INSERT INTO CUST.PROMO_CUSTOMER(CUSTOMER_ID, PROMOTION_ID, USAGE_CNT, EXPIRATION_DATE) ';
    $result .= 'VALUES( "';
    $result .= $cust_id;    $result .= '", "';
    $result .= $promotion_id;    $result .= '", ';
    $result .= '0, SYSDATE+70';
    $result .= ');';    
    $result =~ tr/\"/\'/;
    
    $result .= "\n\n";
  
    return $result;    
}

sub builddeletecustprom
{
    
    my $result = 'DELETE FROM CUST.PROMO_CUSTOMER where EXPIRATION_DATE is not null and SYSDATE >= EXPIRATION_DATE; '; #tbr
    
    $result .= "\n\n";
  
    return $result;    
}


