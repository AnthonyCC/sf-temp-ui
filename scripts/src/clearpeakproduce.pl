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

print "debug: dbname= ", $dbname, "\n";
print "debug: dbuser= ", $dbuser, "\n";
print "debug: infile= ", $infile, "\n";
print "debug: outfile= ", $outfile, "\n";
print "debug: sqloutfile= ", $sqloutfile, "\n";
print "debug: updatemode=" ,  $updatemode, "\n";
#print "debug: password=" ,  $pw, "\n"; #avoid displaying password

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
    
    $result = ",OK\n";
    $outrec .= $result; 

    push @outholder, $outrec;
    
    $sqlrec = buildclearcustprom($cust_id, $promotion_id);
    push @sqlholder, $sqlrec;
    $inscnt++; 
}

$sqlrec = '-- '; $sqlrec .= $inscnt ; $sqlrec .= ' records will be deleted.';
$sqlrec .= "\n\n";
push @sqlholder, $sqlrec;

close FHIN;

open (FHSQL, ">$sqloutfile") or die "Cannot open $file: $!";
print FHSQL @sqlholder;
close FHSQL;

my $runsql = new Runsql;
print "debug: calling runsql.pm -f $sqloutfile -d $dbname -u $dbuser -p ******", "\n";
$runsql->run($sqloutfile, $dbname, $dbuser, $pw);

print "\n";

sub buildclearcustprom
{
        #                               0       1           2           3           4           5       6       7
        #$sqlrec = buildactivitylog($cust_id, $promotion_id);
  
    my $cust_id   = $_[0];
    my $promotion_id   = $_[1];
       
    my $result='DELETE FROM CUST.PROMO_CUSTOMER Where CUSTOMER_ID = "';
     $result .= $cust_id;    $result .= '" and PROMOTION_ID = "';
    $result .= $promotion_id;    $result .= '";';
    $result =~ tr/\"/\'/;
    
    $result .= "\n\n";
  
    return $result;    
}

