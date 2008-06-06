#!/usr/bin/env ruby

require 'optparse'
require 'csv'

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
WHERE PARENT_CONTENTNODE_ID='#{product}'
AND CHILD_CONTENTNODE_ID='#{value}';
__SQL

		$stdout.puts <<__SQL
INSERT INTO CMS.RELATIONSHIP(ID,PARENT_CONTENTNODE_ID,DEF_NAME,CHILD_CONTENTNODE_ID,ORDINAL,DEF_CONTENTTYPE)
VALUES(CMS.SYSTEM_SEQ.NEXTVAL, '#{product}','#{attribute}','#{value}', 0, '#{value.match(/^(\w+):/)[1]}');
__SQL
	when "MEDIA"
		## insert MEDIA item
		#
		$stdout.puts <<__SQL
DELETE FROM CMS.RELATIONSHIP
WHERE PARENT_CONTENTNODE_ID='#{product}'
AND CHILD_CONTENTNODE_ID=(
  SELECT TYPE || ':' || ID
  FROM CMS.MEDIA
  WHERE URI='#{value}'
);
__SQL

		$stdout.puts <<__SQL
INSERT INTO CMS.RELATIONSHIP(ID,PARENT_CONTENTNODE_ID,DEF_NAME,CHILD_CONTENTNODE_ID,ORDINAL,DEF_CONTENTTYPE)
SELECT CMS.SYSTEM_SEQ.NEXTVAL, '#{product}','#{attribute}', TYPE || ':' || ID, 0, TYPE
FROM CMS.MEDIA
WHERE URI='#{value}';
__SQL
	else
		## insert simple value
		#
		$stdout.puts <<__SQL
INSERT INTO CMS.ATTRIBUTE(ID,CONTENTNODE_ID,DEF_NAME,VALUE,ORDINAL,DEF_CONTENTTYPE)
VALUES(CMS.SYSTEM_SEQ.NEXTVAL, '#{product}','#{attribute}','#{value}', 0, '#{product.match(/^(\w+):/)[1]}');
__SQL
	end
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
reader.shift if $options[:skip_header]
reader.each do |row|
	next if row.compact.length == 0 # skip empty lines
	
	# simple field validation
	## next unless "^Product:" =~ row[0] # skip row if first field does not start with 'Product:' prefix

	# DEBUG
	if $options[:debug]
		$stderr.puts "--"
		$stderr.puts "-- #{row[2]} := '#{row[0]}','#{row[1]}','#{row[3]}' --"
		$stderr.puts "--"
	end
	
	process_line(row[0], row[2], row[1], row[3])
end

reader.close

# bye
exit(0)
