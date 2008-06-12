#!/usr/bin/env ruby

require 'optparse'
require 'csv'

class String
	def escaped
		self.gsub(/'/,"''")
	end
end

# settings, options
#
$options = {}



# process a line
#
def process_line(product, atype, attribute, value)
	case atype
	when "RELATIONSHIP"
		## insert relationship type value
		#
		$stdout.puts <<__SQL
DELETE FROM CMS.RELATIONSHIP
WHERE PARENT_CONTENTNODE_ID='#{product.escaped}'
AND CHILD_CONTENTNODE_ID='#{value.escaped}';
__SQL

		$stdout.puts <<__SQL
INSERT INTO CMS.RELATIONSHIP(ID,PARENT_CONTENTNODE_ID,DEF_NAME,CHILD_CONTENTNODE_ID,ORDINAL,DEF_CONTENTTYPE)
VALUES(CMS.SYSTEM_SEQ.NEXTVAL, '#{product.escaped}','#{attribute.escaped}','#{value.escaped}', 0, '#{value.match(/^(\w+):/)[1].escaped}');
__SQL
	when "MEDIA"
		## insert MEDIA item
		#

		# remove old link
		$stdout.puts <<__SQL
DELETE FROM CMS.RELATIONSHIP
WHERE PARENT_CONTENTNODE_ID='#{product.escaped}'
AND CHILD_CONTENTNODE_ID=(
  SELECT TYPE || ':' || ID
  FROM CMS.MEDIA
  WHERE URI='#{value.escaped}'
);
__SQL

		# insert child content node if missing
		$stdout.puts <<__SQL
INSERT INTO CMS.CONTENTNODE(ID,CONTENTTYPE_ID)
SELECT M.TYPE || ':' || M.ID, M.TYPE
FROM CMS.MEDIA M
WHERE URI='#{value.escaped}'
AND NOT EXISTS
(
  SELECT C.ID,C.CONTENTTYPE_ID
  FROM CMS.CONTENTNODE C
  WHERE C.ID = M.TYPE || ':' || M.ID
  AND C.CONTENTTYPE_ID = M.TYPE
);
__SQL

		# create link between media and content nodes
		if $options[:debug]
			# debug
			$stdout.puts <<__SQL
BEGIN
  INSERT INTO CMS.RELATIONSHIP(ID,PARENT_CONTENTNODE_ID,DEF_NAME,CHILD_CONTENTNODE_ID,ORDINAL,DEF_CONTENTTYPE)
  SELECT CMS.SYSTEM_SEQ.NEXTVAL, '#{product.escaped}','#{attribute.escaped}', TYPE || ':' || ID, 0, TYPE
  FROM CMS.MEDIA
  WHERE URI='#{value.escaped}';
EXCEPTION
  WHEN OTHERS THEN
    SELECT 'Row #{$line_counter}; MEDIA ''#{value.escaped}''; ATTRIBUTE ''#{attribute.escaped}'': Missing content node ''' || TYPE || ':' || ID || '''' AS ERROR
    INTO myerror
    FROM CMS.MEDIA
    WHERE URI='#{value.escaped}';
    DBMS_OUTPUT.PUT_LINE(myerror);
END;
__SQL
		else
			# nodebug
			$stdout.puts <<__SQL
INSERT INTO CMS.RELATIONSHIP(ID,PARENT_CONTENTNODE_ID,DEF_NAME,CHILD_CONTENTNODE_ID,ORDINAL,DEF_CONTENTTYPE)
SELECT CMS.SYSTEM_SEQ.NEXTVAL, '#{product.escaped}','#{attribute.escaped}', TYPE || ':' || ID, 0, TYPE
FROM CMS.MEDIA
WHERE URI='#{value.escaped}';
__SQL
		end
	else
		## insert simple value
		#
		$stdout.puts <<__SQL
INSERT INTO CMS.ATTRIBUTE(ID,CONTENTNODE_ID,DEF_NAME,VALUE,ORDINAL,DEF_CONTENTTYPE)
VALUES(CMS.SYSTEM_SEQ.NEXTVAL, '#{product.escaped}','#{attribute.escaped}','#{value.escaped}', 0, '#{product.match(/^(\w+):/)[1].escaped}');
__SQL
	end
	
	
	$stdout.puts "ROLLBACK;" if $options[:rollback]
end






# parse parameters
OptionParser.new do |opts|
	opts.banner = <<__BANNER
This utility transforms product attribute rows to a bulk of SQL commands.
The input is a CSV file containing 4-tuples:

  product ID, attribute name, attribute type, value

Use quote mark to quote fields in CSV and separate them with comma.
A sample line should be like this:

  "Product:apl_mac","WINE_IMPORTER","STRING","Hungarovin"
 
Usage: imp.rb [options]

__BANNER

	opts.on("-s", "--skip-header", "Skip first line") do |v|
		$options[:skip_header] = true
	end

	opts.on("-d", "--debug", "Enable debug messages") do |v|
		$options[:debug] = true
	end

	opts.on("-i", "--input [FILE]", String, "Input CSV file") do |fname|
		unless File.exist?(fname)
			$stderr.puts "Input file #{fname} does not exist, exiting ..."
			exit(1)
		else
			$options[:input_file] = fname
		end
	end

	opts.on_tail("-h", "--help", "Show this message") do
		$stderr.puts opts
		exit
	end
end.parse!


if $options.length == 0
	exit(0)
end


unless $options[:input_file]
	$stderr.puts "No input file, exiting ..."
	exit(0)
end


# -- Process CSV file --
#
reader = CSV.open($options[:input_file], "r")
# skip first line
$line_counter = 0

if $options[:skip_header]
	reader.shift 
	$line_counter = $line_counter + 1
end



if $options[:debug]
	$stdout.puts <<__SQL
DECLARE
  myerror VARCHAR2(256);
BEGIN
  dbms_output.enable(1000000);
__SQL
end



reader.each do |row|
	$line_counter = $line_counter + 1
	next if row.compact.length == 0 # skip empty lines
	
	# simple field validation
	## next unless "^Product:" =~ row[0] # skip row if first field does not start with 'Product:' prefix

	# DEBUG
	if $options[:debug]
		$stdout.puts "--"
		$stdout.puts "-- Line #{$line_counter}, #{row[2]} := '#{row[0]}','#{row[1]}','#{row[3]}' --"
		$stdout.puts "--"
	end
	
	process_line(row[0], row[2], row[1], row[3])
end



if $options[:debug]
	$stdout.puts <<__SQL
END;
__SQL
end



reader.close

# bye
exit(0)
