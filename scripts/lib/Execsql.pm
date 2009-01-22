package Execsql;
use DBI;

my $os = lc $^O;
my $linux;
if($os =~ m/mswin/){
    $islinux=0;
}
else{
    $islinux=1;
}

if($islinux){
    $ENV{'ORACLE_HOME'}="/opt/fdbin/oracle/instantclient_11_1";
}

use DBD::Oracle ;

sub new {
        my ($class_name) = @_;

        my ($self) = {};
        #warn "We just created our new variable...\n ";

        bless ($self, $class_name);
        #warn "and now it's a $class_name object!\n";

        $self->{'_created'} = 1;
        return $self;
    }

sub run{
    my $self = shift;
    my ($infile, $dbname, $dbuser, $pw) = @_;

    print "debug: dbname= ", $dbname, "\n";
    print "debug: dbuser= ", $dbuser, "\n";
    print "debug: infile= ", $infile, "\n";
    #print "debug: password=" ,  $pw, "\n";  #avoid displaying password

    my $dbh  = DBI->connect("dbi:Oracle:$dbname", $dbuser, $pw, {AutoCommit => 1})
            or die "Couldn't connect to database: " . DBI->errstr;
            
    my @data;
     
    my $sqlrec;

    my $cnt=0;

    open(FHIN, "<$infile") or die "Cannot open $file: $!";

    my @content = <FHIN>;

    foreach $sqlrec(@content) {
        
       while($sqlrec =~ m/[\r\n;]$/) {      #remove trailing new line char(s) and semicolon
          chop($sqlrec);
       }
         
       if ($sqlrec eq "" || $sqlrec =~ m/^-/){     #ignore starting with '-'
           next;
        } 
        
        print "debug: sqlrec*= ", $sqlrec, "\n";    

        
        $sqlrec =~ s/\;;*//;
     
        print "debug: sqlrec**= ", $sqlrec, "\n";    
          
        $dbh->do($sqlrec) or die "Couldn't execute statement: " . $dbh->errstr; 
        
        $cnt++; 
    }

    close FHIN;

    print "debug: #sql stmts executed=", $cnt, "\n";

    print "\n";

    $dbh->disconnect;
}

warn "Execsql is successfully loaded!\n";
1;