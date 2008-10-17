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
print "debug: activity_id= ", $activity_id, "\n";
print "debug: source= ", $source, "\n";
print "debug: initiator= ", $initiator, "\n";
print "debug: note= ", $note, "\n";
print "debug: reason= ", $reason, "\n";

print "debug: password=" ,  $pw, "\n";





my $dbh  = DBI->connect("dbi:Oracle:$dbname", $dbuser, $pw)
        or die "Couldn't connect to database: " . DBI->errstr;
        
my $tsale = $dbh->prepare('SELECT CUSTOMER_ID FROM CUST.sale where id=?')    
        or die "Couldn't prepare statement: " . $dbh->errstr;
        
my $tactivitylog = $dbh->prepare('SELECT 1 FROM CUST.ACTIVITY_LOG where Sale_id=?')      
        or die "Couldn't prepare statement: " . $dbh->errstr;
                

my @data;
 
my $inrec, $outrec, $sqlrec;
my @outholder, @sqlholder;

my $sale_id, $ndays, $dlvpassid, $result;  #comp_Code->LATEDEL, #comp_dept->GDW
my $cust_id;
my $inscnt=0; 
my $updcnt=0;



open(FHIN, "<$infile") or die "Cannot open $file: $!";

my @content = <FHIN>;

foreach $inrec(@content) {
    
    chomp($inrec);
    
    if ($inrec eq "" || $inrec =~ m/^#/){     #record starting with '#' is ignored
       next;
    }
    
    (@warray) = split(",", $inrec);
    
    $outrec = $sale_id = $warray[0]; 
    $ndays = $warray[1]; $dlvpassid = $warray[2]; 
         
    if($ndays%7 ne 0) {
        $result = ",#days not a multiple of 7\n"; 
        $outrec .= $result;
        push @outholder, $outrec; 
        next;
    }

    $tsale->execute($sale_id)  or die "Couldn't execute statement: " . $tsale->errstr;    
    @data = $tsale->fetchrow_array();
    if ($tsale->rows == 0) {
        $result = ",Invalid Sale ID\n"; 
        $outrec .= $result;
        push @outholder, $outrec; 
        next;
    }
    $cust_id = $data[0];    
    
    $tactivitylog->execute($sale_id)  or die "Couldn't execute statement: " . $tactivitylog->errstr;    
    $tactivitylog->fetchrow_array();        
   
    if ($tactivitylog->rows ne 0) {
        $result = ",Duplicate Delivery pass extension\n";
        $outrec .= $result;
        push @outholder, $outrec; 
        next;   
    }
    
    $result = ",OK\n";
    $outrec .= $result; 

    push @outholder, $outrec;
    
    $sqlrec = builddlvpass($dlvpassid, $ndays);
    push @sqlholder, $sqlrec;
    $updcnt++; 
 
    while($ndays > 0)   {
        $sqlrec = buildactivitylog($sale_id, $cust_id, $dlvpassid, $activity_id, $source, $initiator, $note, $reason);
        push @sqlholder, $sqlrec;    
        $ndays -= 7;
        $inscnt++;
    }     
}

$sqlrec = '-- '; $sqlrec .= $updcnt ; $sqlrec .= ' records will be updated.';
$sqlrec .= "\n\n";
push @sqlholder, $sqlrec;

$sqlrec = '-- '; $sqlrec .= $inscnt ; $sqlrec .= ' records will be inserted.';
$sqlrec .= "\n";
push @sqlholder, $sqlrec;

close FHIN;

open (FHOUT, ">$outfile") or die "Cannot open $file: $!";
print FHOUT @outholder;
close FHOUT;

open (FHSQL, ">$sqloutfile") or die "Cannot open $file: $!";
print FHSQL @sqlholder;
close FHSQL;

$tsale->finish;
$tactivitylog->finish;

print "\n";

if($updatemode){
    my $runsql = new Runsql;
    print "debug: calling runsql.pm -f $sqloutfile -d $dbname -u $dbuser -p ******", "\n";
    $runsql->run($sqloutfile, $dbname, $dbuser, $pw);
}

$dbh->disconnect;

sub buildactivitylog
{
        #                               0       1           2           3           4           5       6       7
        #$sqlrec = buildactivitylog($sale_id, $cust_id, $dlvpassid, $activity_id, $source, $initiator, $note, $reason);
  
    my $sale_id   = $_[0];
    my $cust_id   = $_[1];
    my $dlvpassid    = $_[2];
    my $activity_id =  $_[3];       #'Extd Dlv Pass'
    my $source =  $_[4];             #'SYS'
    my $initiator   =  $_[5];       #'SYSTEM'
    my $note = $_[6];               #'Extd DP by a week'
    my $reason = $_[7];             #'LATEDLV'
      
    my $result='INSERT INTO CUST.ACTIVITY_LOG(TIMESTAMP, CUSTOMER_ID, ACTIVITY_ID, SOURCE, INITIATOR, NOTE, SALE_ID, DLV_PASS_ID, REASON) ';
    $result .= 'VALUES( ';
    $result .= 'SYSDATE, "';
    $result .= $cust_id;    $result .= '", "';
    $result .= $activity_id;  $result .= '", "';
    $result .= $source;    $result .= '", "';
    $result .= $initiator;  $result .= '", "';
    $result .= $note;   $result .= '", "';
    $result .= $sale_id; $result .= '", "';  
    $result .= $dlvpassid; $result .= '", "';  
    $result .= $reason;  
    $result .= '");';
    
    $result =~ tr/\"/\'/;
    
    $result .= "\n\n";
  
    return $result;    
}

sub builddlvpass
{
        #                               0       
        #$sqlrec = builddlvpass($sdlvpassid, $ndays);
  
    my $dlvpassid   = $_[0];
    my $ndays = $_[1];
     
    my $result = 'UPDATE CUST.DELIVERY_PASS ';
    $result .= 'SET EXP_DATE = EXP_DATE + ';
    $result .= $ndays;
    $result .= ' WHERE ID = "';
    $result .= $dlvpassid;
    $result .= '";';
    
    $result =~ tr/\"/\'/;
    
    $result .= "\n\n";
  
    return $result;    
}