use DBI;

use Getopt::Std;  
getopts('hf:k:p:', \ my %opts);    # -ptake arg.  Sets opt_* as a side effect.

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

#print "debug: configfile= ", $configfile, "\n"; 

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
#use Runsql;
 
my $pgm = lc $0;        #lc->convert to lowercase
$pgm =~ s/\..*//;       #remove file extension

my $cfg = new PerlConfig;
my $cmn = new Common;

$cfg->read($configfile) or die "Couldn't read Config file: $!";
 
my $dbname = $cfg->get("$pgm.dbname");
my $dbuser = $cfg->get("$pgm.dbuser");
my $infile=$opts{f};
my $key = $opts{k};

my $pw   = $dbuser;

if ($opts{p} ne ""){
    $pw = $opts{p};
}



#print "debug: dbname= ", $dbname, "\n";
#print "debug: dbuser= ", $dbuser, "\n";
#print "debug: infile= ", $infile, "\n";
#print "debug: key= ", $key, "\n";

#print "debug: password=" ,  $pw, "\n";


my $dbh  = DBI->connect("dbi:Oracle:$dbname", $dbuser, $pw)
        or die "Couldn't connect to database: " . DBI->errstr;
        
my $tp = $dbh->prepare('SELECT * FROM DLV.GEO_RESTRICTION where id=?')    
        or die "Couldn't prepare statement: " . $dbh->errstr;
        
my $tc = $dbh->prepare('SELECT * FROM DLV.GEO_RESTRICTION_DAYS where Restriction_id=?')      
        or die "Couldn't prepare statement: " . $dbh->errstr;
                

my @data;

$tp->execute($key)  or die "Couldn't execute statement: " . $tp->errstr;    
@data = $tp->fetchrow_array();
print "\n@data\n\n";
$tp->finish;

$tc->execute($key)  or die "Couldn't execute statement: " . $tc->errstr;    

my @row;
        while ( @row = $tc->fetchrow ) {
                print "@row\n";
         }

$tc->finish;

print "\n";




