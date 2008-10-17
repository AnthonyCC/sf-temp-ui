package Common;
sub new {
        my ($class_name) = @_;

        my ($self) = {};
        #warn "We just created our new variable...\n ";

        bless ($self, $class_name);
        #warn "and now it's a $class_name object!\n";

        $self->{'_created'} = 1;
        return $self;
    }
sub get_highest_suffix { #gets first highest suffix ie if dir has foo.xxx, foo_2.xxx foo_3.xxx, the function will return 3.
   my $self = shift;
   my ($file) = @_;
   my $ext = getext($file);
   print "debug: in get_highest_suffix_file: file= ", $file, ", ext= ", $ext, "\n";
     $file =~ s/\..*//;       #remove file extension
     for($i=2;1;$i++)
    {
      my $fullName = $file;
      $fullName .= '_';
      $fullName .= $i;
      $fullName .= '.';
      $fullName .= $ext;
      if(! -e $fullName){
        return $i-1;
      }
    }  
}

sub make_suffix_file {  #given foo.bar and a number n returns foo_n.bar if n=1, returns foo.bar
    print "**", @_, "\n";
    my $self = shift;
    my ($file, $i) = @_;
    my $ext = getext($file);
    
    print "debug: in make_suffix_file: file= ", $file, ", ext= ", $ext, " i= ", $i, "\n";

    if($i <= 1) {
        print "debug: in make_suffix_file: returning file= ", $file, "\n";
       return $file;
    }
    $file =~ s/\..*//;       #remove file extension
    print "debug: in make_suffix_file: file= ", $file, ", ext= ", $ext, "\n";
    my $fullName = $file;
    $fullName .= '_';
    $fullName .= $i;
    $fullName .= '.';
    $fullName .= $ext;
    
    print "debug: in make_suffix_file: file= ", $fullName, ", ext= ", $ext, "\n";

    return $fullName;
 
}

sub getext
{
	my ($name) = @_;
	my ($n);
 	($n) = $name =~ /\.([A-Za-z0-9]+)$/;
	return $n;
}

warn "Common is successfully loaded!\n";
1;
