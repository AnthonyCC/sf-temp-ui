
use Common;
my $cmn = new Common;

my $sfx = $cmn->get_highest_suffix("c:\\junk\\foo.txt");

my $file = $cmn->make_suffix_file("c:\\junk\\foo.txt", $sfx);

print "file= ", $file, "\n";

print "\n";

