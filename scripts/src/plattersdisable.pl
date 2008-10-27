use DBI;

use Getopt::Std;  
getopts('hUp:', \ my %opts);    # -ptake arg.  Sets opt_* as a side effect.

if($opts{h} ne ""){
    print "usage: ", $pgm, " -f <infile> [-U] [-p <password>]\n where -U->update mode (build AND run sql script),\n", "<password> is database password. Other params are read from perl.cfg file\n";
    exit 1;
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

print "debug: islinux= $islinux\n";

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
use Removeattrs;
 
my $pgm = lc $0;        #lc->convert to lowercase
$pgm =~ s/\..*//;       #remove file extension

my $cfg = new PerlConfig;

$cfg->read($configfile) or die "Couldn't read Config file: $!";
 
my $dbname = $cfg->get("$pgm.dbname");
my $dbuser = $cfg->get("$pgm.dbuser");
my $appuser = $cfg->get("$pgm.appuser");
my $reason = $cfg->get("$pgm.reason");
my $infile=$cfg->get("$pgm.infile");
my $sqloutfile1=$cfg->get("$pgm.sqloutfile1");
my $sqloutfile2=$cfg->get("$pgm.sqloutfile2"); 

my $pw   = $dbuser;

if ($opts{p} ne ""){
    $pw = $opts{p};
}

print "debug: dbname= ", $dbname, "\n";
print "debug: dbuser= ", $dbuser, "\n";
print "debug: reason= ", $reason, "\n";

print "debug: infile= ", $infile, "\n";
print "debug: outfile= ", $outfile, "\n";
print "debug: sqloutfile1= ", $sqloutfile1, "\n";
print "debug: sqloutfile2= ", $sqloutfile2, "\n";
print "debug: password=" ,  $pw, "\n"; #avoid displaying password

print "debug: trying DBI->connect('dbi:Oracle:$dbname', $dbuser, $pw)...\n";

my $removeattrs = new Removeattrs;

$removeattrs->run($infile, $sqloutfile1, $dbname, $dbuser, $pw);

open(FHIN, "<$infile") or die "Cannot open $file: $!";

my @content = <FHIN>;

my $cnt=0;
my $sqlrec;
my @sqlholder;

foreach $inrec(@content) {
    
    chomp($inrec);
    
    if ($inrec eq "" || $inrec =~ m/^#/){     #record starting with '#' is ignored
       next;
    }
    
    (@warray) = split(",", $inrec);

    $mat_id = '000000000'.$warray[0];
  
    $sqlrec = build_insert($mat_id, $reason);
    $cnt++;
    push @sqlholder, $sqlrec;
}

close FHIN;

$sqlrec = '-- '; $sqlrec .= $cnt; $sqlrec .= ' records will be inserted.'; $sqlrec .= "\n\n";
push @sqlholder, $sqlrec;

$sqlrec = build_update($reason);
push @sqlholder, $sqlrec;

$sqlrec = '-- 1 record will be updated.'; $sqlrec .= "\n\n";
push @sqlholder, $sqlrec;

open (FHSQL, ">$sqloutfile2") or die "Cannot open $file: $!";
print FHSQL @sqlholder;
close FHSQL;

sub build_insert
{
    my ($mat_id, $reason) = @_;
    $result = 'INSERT into erps.attributes (ID, ROOT_ID, CHILD1_ID, CHILD_ID2,';
    $result .= ' ATR_TYPE, ATR_NAME, ATR_VALUE, DATE_MODIFIED) ';
    $result .= ' VALUES (ERPS.SYSTEM_SEQ_NEXTVAL, "';
    $result .= $mat_id; $result .= '", " ", " ",';
    $result .= ' "S", "restrictions", "';
    $result .= $reason; $result .= '", SYSDATE);';     
    
    $result =~ tr/\"/\'/;
    $result .= "\n\n";

    return $result;    

}

sub build_update
{
    my ($reason) = @_;
    my $startdate = $cfg->get("$pgm.startdate");
    my $starttime = $cfg->get("$pgm.starttime");
    my $enddate = $cfg->get("$pgm.enddate");
    my $endtime = $cfg->get("$pgm.endtime");
    print "debug: startdate= $startdate\n";
    print "debug: starttime= $starttime\n";
    print "debug: enddate= $enddate\n";
    print "debug: endtime= $endtime\n";


    $result = 'update dlv.restricted_days set start_time = TO_DATE("';
    $result .= $startdate; $result .= ' '; $result .= $starttime; 
    $result .= '", "YYYY/MM/DD HH24:MI:SS"), end_time = TO_DATE("';
    $result .= $enddate; $result .= ' ';  $result .= $endtime; 
    $result .= '", "YYYY/MM/DD HH24:MI:SS") ';

    $result .= ' where reason = "'; $result .= $reason; 
    $result .= '";';
    
    $result =~ tr/\"/\'/;
    $result .= "\n\n";
    return $result;
}

 
