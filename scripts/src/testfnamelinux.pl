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
use Common;
my $cmn = new Common;

#my $sfx = $cmn->get_highest_suffix("/home/fdadmin/scripts/data/peakproduce_inductees.dat");

#my $file = $cmn->make_suffix_file("/home/fdadmin/scripts/data/peakproduce_inductees.dat", $sfx);

my $sfx = $cmn->get_highest_suffix("../data/peakproduce_inductees.dat");

my $file = $cmn->make_suffix_file("../data/peakproduce_inductees.dat", $sfx);

print "file= ", $file, "\n";

print "\n";

